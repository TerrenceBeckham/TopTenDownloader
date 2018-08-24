package com.afrodroid.android.toptendownloader;

import android.util.Log;

import com.afrodroid.android.toptendownloader.FeedEntry;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;


    public ParseApplications() {
        //This initializes the parameter as an com.afrodroid.android.toptendownloader.FeedEntry object
        this.applications = new ArrayList<>();
    }

    //    Getter
    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    //The Boolean false is returned if there are any errors in parsing the XML
    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        //Hold value of current tag
        String textValue = "";


        try {
            //This creates a new instance of a PullParserFactory that can be used to create XML pull parsers
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            //This specifies that the parser produced by this factory will be validating(it simply set feature
            //XmlPullParser.FEATURE_VALIDATION to true or false.
            factory.setNamespaceAware(true);
            //Creates a new instance of an XML Pull Parser using the currently configured factory features.
            XmlPullParser xxp = factory.newPullParser();
            //XmlPullParser object needs a StringReader to read from and, a StringReader needs a String to process
            //Here the string is the XML that we pulled in from the Rss.
            xxp.setInput(new StringReader(xmlData));
            //Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)(Docs)
            int eventType = xxp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xxp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for " + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xxp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse:Ending tag for" + tagName);
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                applications.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(textValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(textValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {

                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(textValue);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURL(textValue);
                            }
                        }
                        break;
                    default:
                        //nothing else to do.

                }
                eventType = xxp.next();
            }
//            for (FeedEntry app : applications) {
//                Log.d(TAG, "***************");
//                Log.d(TAG, app.toString());
//            }


            //This will catch all exceptions
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
