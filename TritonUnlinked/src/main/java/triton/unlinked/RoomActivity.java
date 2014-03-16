package triton.unlinked;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Vector;

//imports for pulling in data from db
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.util.TypedValue;
import android.widget.AdapterView.OnItemSelectedListener;

public class RoomActivity extends Activity implements OnItemSelectedListener {
    RoomScheduleAdapter timeListAdapter;
    ListView timeListView;
    private String[] times = {"8:00a", "9:00a", "10:00a", "11:00a", "12:00p", "1:00p", "2:00p", "3:00p", "4:00p", "5:00p", "6:00p", "7:00p", "8:00p", "9:00p"};
    private String[] days = {"Monday", "Tuesday","Wednesday", "Thursday", "Friday"}; //options for spinner
    private int spinnerOption = 0;

    private ProgressDialog pDialog;

    /* sample JSON object for a room:
     * {"bld":"CENTR",
     * "room":"115",
     * "map":"http://m.ucsd.edu/maps/isisCode/CENTR",
     * "_id":"530c49cc7d98ddee06000009",
     * "__v":0,
     * "classes":[{"class":"CSE 11","day":"TuTh","time":"8:00a - 9:20a","_id":"530c49cc7d98ddee0600000a"}]}
     */

    //Temporary, must be replaced by constructed query string given course information
    //private static String url = "http://tritonunlinked.herokuapp.com/room?bld=CENTR&room=109";

    //private TextView bldView;
    //private TextView roomView;
    private TextView bldRoomView;

    //JSON field tags for Room
    private static final String TAG_BLD = "bld";
    private static final String TAG_ROOM = "room";
    private static final String TAG_CLASSES = "classes";
    //JSON field tags for the classes array
    private static final String TAG_CLASS = "class";
    private static final String TAG_DAY = "day";
    private static final String TAG_START = "startTime";
    private static final String TAG_END = "endTime";
    private static final String TAG_TIME = "time";

    private String buildingEncoded = ""; //encoded building for the url
    private String building = "";
    private String room = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Spinner spinner = (Spinner) findViewById(R.id.dayPicker);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("SearchValue");
            String[] dataSplit = value.split(" ");
            for(int i=0; i<dataSplit.length-1; i++) {
                building = building + dataSplit[i];
                buildingEncoded = buildingEncoded + dataSplit[i];
                if(i<dataSplit.length-2) {
                    building = building + " ";
                    buildingEncoded = buildingEncoded + "%20";
                }
            }
            room = dataSplit[dataSplit.length - 1]; //should be the last item in the array
        }

        //RoomScheduleAdapter adapter = new RoomScheduleAdapter(this, times);
        timeListAdapter = new RoomScheduleAdapter(this, times);

        timeListView = (ListView) findViewById(R.id.schedule);
        timeListView.setAdapter(timeListAdapter);
        timeListView.setOnItemClickListener(new RoomActivityOnItemClickListener());

        new GetRoom().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRoom extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RoomActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create service handler class instance
            ServiceHandler sh = new ServiceHandler();

            String jsonStr = sh.makeServiceCall(generateUrl(buildingEncoded, room), ServiceHandler.GET);

            Log.d("RoomActivity", "Response: > " + jsonStr);

            if(jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Save values from JSON Object
                    //building = jsonObj.getString(TAG_BLD);
                    //room = jsonObj.getString(TAG_ROOM);
                    JSONArray classesArr = jsonObj.getJSONArray(TAG_CLASSES);

                    //FOR TESTING PURPOSES: adding fake data to the classesArr
                    //classesArr.put( new JSONObject("{\"class\":\"80 min class case 1\",\"day\":\"TuTh\",\"time\":\"11:00a - 12:20p\"}") );
                    //classesArr.put( new JSONObject("{\"class\":\"80 min class case 2\",\"day\":\"TuTh\",\"time\":\"12:30p - 1:50p\"}") );
                    //classesArr.put( new JSONObject("{\"class\":\"3 hr class\",\"day\":\"TuTh\",\"time\":\"5:00p - 7:50p\"}") );

                    timeListAdapter.setClassesArr(classesArr);
                    timeListAdapter.setSelectedDay("M");

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

            //Update local fields with values from JSON
            bldRoomView = (TextView) findViewById(R.id.bld_room);
            bldRoomView.setText(building + " " + room);
            /*bldView = (TextView) findViewById(R.id.lecture_hall);
            bldView.setText(building);
            roomView = (TextView) findViewById(R.id.room_number);
            roomView.setText(room);*/

            timeListView.invalidateViews();

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    //Get value from spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch(pos) {
            case 0:
                timeListAdapter.setSelectedDay("M");
                break;
            case 1:
                timeListAdapter.setSelectedDay("Tu");
                break;
            case 2:
                timeListAdapter.setSelectedDay("W");
                break;
            case 3:
                timeListAdapter.setSelectedDay("Th");
                break;
            case 4:
                timeListAdapter.setSelectedDay("F");
                break;
        }

        timeListView.invalidateViews();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_room, container, false);
            return rootView;
        }
    }

    private String generateUrl(String bld, String room){
        Log.d("RoomActivity", "Generated URL: " + "http://tritonunlinked.herokuapp.com/room?bld=" + bld + "&room=" + room);
        return "http://tritonunlinked.herokuapp.com/room?bld=" + bld + "&room=" + room;
    }

}
