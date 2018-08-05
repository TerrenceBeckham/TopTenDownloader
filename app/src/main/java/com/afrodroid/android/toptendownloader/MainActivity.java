package com.afrodroid.android.toptendownloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: starting Asynctask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: done");

    }

        private class DownloadData extends AsyncTask<String, Void, String> {
            //The first String parameter is the url that will be passed in, Void is the place where a
            //progress bar would be displayed and the last String will contain all of the XML downloaded
            private static final String TAG = "DownloadData";
            @Override
            //This is where the result of the XMLParsing is passed back to the UI thread
            //This method is automatically called by the system after the doInBackground method has completed
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d(TAG, "onPostExecute: Parameter is " + s);
                ParseApplications parseApplications = new ParseApplications();
                parseApplications.parse(s);
            }

            @Override
            protected String doInBackground(String... strings) {
                Log.d(TAG, "doInBackground: starts with " + strings[0]);
                String rssFeed = downloadXML(strings[0]);
                if (rssFeed == null) {
                    //here log.e is used because it persists into the production app as opposed to
                    //log.d which is removed for production apps.
                    Log.e(TAG, "doInBackground: Error Downloading" );
                }
                return rssFeed;
            }
            private String downloadXML(String urlPath) {
                StringBuilder xmlResult = new StringBuilder();

                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    int response = connection.getResponseCode();
                    Log.d(TAG, "downloadXML: The response code was " + response);
                    //InputStream inputStream = connection.getInputStream();
                    //InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    //BufferedReader reader = new BufferedReader(inputStreamReader);
                    //The three lines above are the long way of writing a buffered reader
                    //This lines contains all of them using method chaining.
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    int charsRead;
                    char[] inputBuffer = new char[500];

                    while (true) {
                        charsRead = reader.read(inputBuffer);

                        if (charsRead < 0) {
                            break;
                        }

                        if (charsRead > 0) {
                            xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                        }
                    }
                    reader.close();
                    return xmlResult.toString();

                    //MalformedURLException must be invoked before IOException because it is a subclass
                    // of IOException.
                } catch (MalformedURLException e) {
                    Log.e(TAG, "downloadXML: Invalid Url " + e.getMessage());
                } catch (IOException e) {
                    Log.e(TAG, "downloadXML: Io Exception reading data:" + e.getMessage());
                } catch (SecurityException e) {
                    Log.e(TAG, "downloadXML: Security Exception. Needs  Permissions? " + e.getMessage());
                    e.printStackTrace();
                }

                return null;


            }


        }

}
