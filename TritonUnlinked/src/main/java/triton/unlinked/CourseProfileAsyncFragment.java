package triton.unlinked;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment containing an async task for course profile
 */
public class CourseProfileAsyncFragment extends Fragment {
    private static final String TAG = CourseProfileAsyncFragment.class.getSimpleName();
    private String courseDept;
    private String courseNum;

    public CourseProfileAsyncFragment(String courseName){
        Log.i("CourseProfileAsync", "this is courseName: " + courseName);
        String[] courseArr = courseName.split(" ");
        this.courseDept = courseArr[0];
        this.courseNum = courseArr[courseArr.length-1];
    }
    /**
     * Callback interface through which the fragment can report the task's
     * results back to the Activity.
     */
    static interface TaskCallbacks {
        public void onPreExecute();
        public void onPostExecute(JSONObject course);
    }

    private TaskCallbacks mCallbacks;
    private GetCoursesTask mTask;
    private boolean mRunning;
    /**
     * Android passes us a reference to the newly created Activity by calling this
     * method after each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach(Activity)");
        super.onAttach(activity);

        if (!(activity instanceof TaskCallbacks)) {
            throw new IllegalStateException("Activity must implement the TaskCallbacks interface.");
        }

        // Hold a reference to the parent Activity so we can report back the task's
        // current progress and results.
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method is called only once when the Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        start();
    }

    /**
     * This method is not called when the Fragment is being retained
     * across Activity instances.
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
        cancel();
    }

    /**
     * Start the background task.
     */
    public void start() {
        if (!mRunning) {
            mTask = new GetCoursesTask(this.courseDept, this.courseNum);
            mTask.execute();
            mRunning = true;
        }
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {
        if (mRunning) {
            mTask.cancel(false);
            mTask = null;
            mRunning = false;
        }
    }

    /**
     * Returns the current state of the background task.
     */
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetCoursesTask extends AsyncTask<Void, Void, Void> {
        private String courseSubj;
        private String courseNum;

        //Temporary, must be replaced by constructed query string given course information
        private String url;

        private JSONObject courseJson;
        private JSONArray sectionsJson;

        public GetCoursesTask(String courseSubj, String courseNum){
            this.courseSubj = courseSubj;
            this.courseNum = courseNum;
            this.url =  "http://tritonunlinked.herokuapp.com/coursedb?subject="+ this.courseSubj +"&num="+ this.courseNum;
        }
        @Override
        protected void onPreExecute() {
            mCallbacks.onPreExecute();
            mRunning = true;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create service handler class instance
            ServiceHandler sh = new ServiceHandler();
            Log.i("CourseProfile", "this is the url hit: " + url);
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null) {
                try {
                    courseJson = new JSONObject(jsonStr);
                    sectionsJson = new JSONArray(courseJson.getString("sections"));

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
            mCallbacks.onPostExecute(courseJson);
            mRunning = false;
        }
    }

    /*
     * Handy logging methods for lifecycle debugging
     */
    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated(Bundle)");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }
    */
}
