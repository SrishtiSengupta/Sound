//package sound.example.com.sound;
//
//import android.app.Application;
//
//import com.parse.Parse;
//import com.parse.ParseInstallation;
//
///**
// * Created by Srishti on 12/11/2015.
// */
//public class ParseApplication extends Application {
//    public static final String YOUR_APPLICATION_ID = "IWk0D7wXJLT3D7Fciq5aM8PXH61Pbsw4ejorRl1l";
//    public static final String YOUR_CLIENT_KEY = "pf9KzvhfQpIdDamZlKjwJimx5IYCNBEKzs8WnV5R";
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        // Enable Local Datastore.
//        Parse.enableLocalDatastore(this);
//
//        try {
//            Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
//            ParseInstallation.getCurrentInstallation().saveInBackground();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
