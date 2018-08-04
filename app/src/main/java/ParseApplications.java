import java.util.ArrayList;

public class ParseApplications {
    private static final String TAG = "ParseApplications";
    private ArrayList<FeedEntry> applications;

    //    Getter
    public ArrayList<FeedEntry> getApplications() {
        return applications;
    }

    public ParseApplications(ArrayList<FeedEntry> applications) {
        //This initializes the parameter as an FeedEntry object
        this.applications = new ArrayList<>();


    }
}
