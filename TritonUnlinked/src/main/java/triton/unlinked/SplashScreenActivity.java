package triton.unlinked;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    private static String url = "http://tritonunlinked.herokuapp.com/departments";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        setContentView(R.layout.activity_splash_screen);

        //new DownloadDatabase().execute();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

    }


    /**
     * Async task class to get json by making HTTP call
     */
    /*
    private class DownloadDatabase extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SplashScreenActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create service handler class instance
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null) {
                try {

                    String insert = "INSERT INTO 'browse_courses' ('subject', 'number', 'course', 'title', 'course_id')\n\n";
                    String subject, number, course, title, course_id = "";

                    JSONArray deptListJson = new JSONArray(jsonStr);

                    for(int i=0; i<deptListJson.length(); i++) {
                        JSONObject deptJson = deptListJson.getJSONObject(i);
                        JSONArray courseListJson = deptJson.getJSONArray("courses");

                        subject = deptListJson.getString(1);

                        //Iterate through list of courses for each department
                        for(int j=0; j<courseListJson.length(); j++) {
                            String courseRow = "";

                            //Add union to query if not first select
                            if(j != 0) {
                                courseRow = "UNION ";
                            }

                            //Populate row data from Json
                            JSONObject courseJson = courseListJson.getJSONObject(j);
                            number = courseJson.getString("code");
                            course = subject + " " + number;
                            title = courseJson.getString("title");
                            course_id = courseJson.getString("_id");

                            //Construct SQL inserts
                            courseRow += "SELECT " + subject + " AS 'subject',"
                                                   + number + " AS 'number',"
                                                   + course + " AS 'course',"
                                                   + title + " AS 'title',"
                                                   + course_id + " AS 'course_id'\n";

                            insert += courseRow;
                        }
                    }

                    Log.v("Constructed: ", insert);

                    //Save values from JSON Object
                    //subject = jsonObj.getString(TAG_SUBJECT);

                    //Check version number against database
                    //Get values from JSON Object
                    //Write values to formatted insert file

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
    */
}