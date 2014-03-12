package triton.unlinked;

import java.util.Locale;
import java.util.Vector;

//imports for pulling in data from db
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
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

public class RoomActivity extends Activity implements AdapterView.OnItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    //SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    //ViewPager mViewPager;
    RoomScheduleAdapter timeListAdapter;
    ListView timeListView;
    private String[] times = {"8:00a", "9:00a", "10:00a", "11:00a", "12:00p", "1:00p", "2:00p", "3:00p", "4:00p", "5:00p", "6:00p", "7:00p", "8:00p", "9:00p"};
    private String[] days = {"Monday", "Tuesday","Wednesday", "Thursday", "Friday"}; //options for spinner
    private String spinnerOption = "Monday";

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
    private static String url = "http://tritonunlinked.herokuapp.com/room?bld=CENTR&room=109";

    private TextView bldView;
    private TextView roomView;
    private TextView courseNameView;
    private TextView startEndView;

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

    private String building;
    private String room;
    private JSONArray classesArr;
    private String[] course;
    private String[] day;
    private String[] start;
    private String[] end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Spinner spinner = (Spinner) findViewById(R.id.dayPicker);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,days);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);

        new GetRoom().execute();

        //TODO: grab the data passed in from the search bar and update the URL
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("SearchValue");
            Log.d("RoomActivity", "SearchValue: " + value);
        }

        //RoomScheduleAdapter adapter = new RoomScheduleAdapter(this, times);
        timeListAdapter = new RoomScheduleAdapter(this, times);
        //timeListAdapter.setListView(timeListView);

        timeListView = (ListView) findViewById(R.id.schedule);
        timeListView.setAdapter(timeListAdapter);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        //mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
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

            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Save values from JSON Object
                    building = jsonObj.getString(TAG_BLD);
                    room = jsonObj.getString(TAG_ROOM);
                    classesArr = jsonObj.getJSONArray(TAG_CLASSES);

                    //FOR TESTING PURPOSES: adding fake data to the classesArr
                    //classesArr.put( new JSONObject("{\"class\":\"80 min class case 1\",\"day\":\"TuTh\",\"time\":\"11:00a - 12:20p\"}") );
                    //classesArr.put( new JSONObject("{\"class\":\"80 min class case 2\",\"day\":\"TuTh\",\"time\":\"12:30p - 1:50p\"}") );
                    //classesArr.put( new JSONObject("{\"class\":\"3 hr class\",\"day\":\"TuTh\",\"time\":\"5:00p - 7:50p\"}") );

                    timeListAdapter.setClassesArr(classesArr);
                    course = new String[classesArr.length()];
                    day = new String[classesArr.length()];
                    start = new String[classesArr.length()];
                    end = new String[classesArr.length()];


                    for(int i = 0; i < classesArr.length(); i++) {

                        JSONObject currClass = classesArr.getJSONObject(i);
                        Log.d("RoomActivity: ", "currClass: " + currClass);

                        String[] startEnd = currClass.getString(TAG_TIME).split(" - ");

                        course[i] = currClass.getString(TAG_CLASS);
                        day[i] = currClass.getString(TAG_DAY);
                        start[i] = startEnd[0];
                        end[i] = startEnd[1];
                        //start[i] = currClass.getString(TAG_START);
                        //end[i] = currClass.getString(TAG_END);
                        Log.d("RoomActivity", "class: " + course[i] + ", day: " + day[i] + ", start: " + start[i] + ", end: " + end[i]);
                    }

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
            bldView = (TextView) findViewById(R.id.lecture_hall);
            bldView.setText(building);
            roomView = (TextView) findViewById(R.id.room_number);
            roomView.setText(room);

            /*for(int i = 0; i<course.length; i++) {
                Log.d("RoomActivity", "at the for loop iteration " + i + ". which class is this: " + course[i] + ", start: " + start[i] + ", end: " + end[i]);

                for(int j = 0; j<times.length; j++) {
                    View v = timeListView.getChildAt(j - timeListView.getFirstVisiblePosition());

                    int startHr = Integer.parseInt( start[i].split(":")[0] );
                    int startMin = Integer.parseInt( start[i].split(":")[1].substring(0,2) ); //use substring to get rid of the 'a'/'p'
                    int endHr = Integer.parseInt( end[i].split(":")[0] );
                    int endMin = Integer.parseInt( end[i].split(":")[1].substring(0,2) ); //use substring to get rid of the 'a'/'p'

                    if( start[i].equals(times[j]) ) {
                    //if( ( startHr == Integer.parseInt( times[j].split(":")[0] ) ) && ( start[i].charAt(start[i].length()-1) == times[j].charAt(times[j].length() - 1) ) ) {
                        courseNameView = (TextView) v.findViewById(R.id.course_name);
                        //courseNameView.setText(course[i]);
                        startEndView = (TextView) v.findViewById(R.id.start_end);
                        //startEndView.setText(start[i] + " - " + end[i]);

                        //logic to set the schedule blocks (SUPER MESSY)
                        View scheduleBlock1 = v.findViewById(R.id.schedule_block1);
                        View scheduleBlock2 = v.findViewById(R.id.schedule_block2);
                        //int classLength = 0; //length of the class in minutes

                        if( startHr == endHr ) { //50 minute class (ex: starts at 8:00, ends at 8:50)
                            //classLength = 50;
                            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, v.getContext().getResources().getDisplayMetrics());
                            RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                            scheduleBlock1.setLayoutParams(schedBlock1Param);

                            courseNameView.setText(course[i]);
                            startEndView.setText(start[i] + " - " + end[i]);
                        }
                        else { //all the other cases
                             // cases to handle:
                             // 80 minute classes: (will take up 2 list view items)
                             //  - case 1: starts on the hour, ends at x:20 (ex: 8:00-9:20) -- in this case, the text should be set in the FIRST item
                             //  - case 2: starts on the half hour, ends at x:50 (ex: 9:30-10:50) -- in this case, the text should be set in the SECOND item
                             // 3 hour classes e.g., 5:00-7:40 (will take up 3 list view items)

                            Log.d("RoomActivity", "not a 50 min class. class: " + course[i] + ", start: " + start[i] + ", end: " + end[i]);

                            if( ((startHr + 1) % 12) == (endHr % 12) ) { // 80 minute class
                                //classLength = 80;
                                Log.d("RoomActivity", "setting blocks for 80 min class: " + course[i] + ", start: " + start[i] + ", end: " + end[i]);

                                View nextListItem = timeListView.getChildAt(j+1 - timeListView.getFirstVisiblePosition());

                                //case 1: starts on the hour, ends at x:20 (ex: 8:00-9:20) -- in this case, the text should be set in the FIRST item
                                if( startMin == 0 ) {
                                    int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, v.getContext().getResources().getDisplayMetrics());
                                    RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height1);
                                    scheduleBlock1.setLayoutParams(schedBlock1Param);

                                    View nextSchedBlock = nextListItem.findViewById(R.id.schedule_block1);
                                    int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, nextListItem.getContext().getResources().getDisplayMetrics());
                                    RelativeLayout.LayoutParams schedBlock2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height2);
                                    nextSchedBlock.setLayoutParams(schedBlock2Param);

                                    courseNameView.setText(course[i]);
                                    startEndView.setText(start[i] + " - " + end[i]);
                                }

                                //case 2: starts on the half hour, ends at x:50 (ex: 9:30-10:50) -- in this case, the text should be set in the SECOND item
                                else if( startMin == 30 ) {
                                    int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, v.getContext().getResources().getDisplayMetrics());
                                    RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height1);
                                    schedBlock1Param.setMargins(0, height1, 0, 0);
                                    scheduleBlock2.setLayoutParams(schedBlock1Param);

                                    View nextSchedBlock = nextListItem.findViewById(R.id.schedule_block1);
                                    int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, nextListItem.getContext().getResources().getDisplayMetrics());
                                    RelativeLayout.LayoutParams schedBlock2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height2);
                                    nextSchedBlock.setLayoutParams(schedBlock2Param);

                                    //set the text in the SECOND item
                                    courseNameView = (TextView) nextListItem.findViewById(R.id.course_name);
                                    startEndView = (TextView) nextListItem.findViewById(R.id.start_end);

                                    courseNameView.setText(course[i]);
                                    startEndView.setText(start[i] + " - " + end[i]);
                                }
                            }
                            else if( ((startHr + 2) % 12) == (endHr % 12) ) { // 3 hour class
                                View hour1ListItem = v;
                                View hour2ListItem = timeListView.getChildAt(j+1 - timeListView.getFirstVisiblePosition());
                                View hour3ListItem = timeListView.getChildAt(j+2 - timeListView.getFirstVisiblePosition());

                                View hour1SchedBlock = scheduleBlock1;
                                View hour2SchedBlock = hour2ListItem.findViewById(R.id.schedule_block1);
                                View hour3SchedBlock = hour3ListItem.findViewById(R.id.schedule_block1);

                                int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, hour1ListItem.getContext().getResources().getDisplayMetrics());
                                RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height1);
                                hour1SchedBlock.setLayoutParams(schedBlock1Param);

                                int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, hour2ListItem.getContext().getResources().getDisplayMetrics());
                                RelativeLayout.LayoutParams schedBlock2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height2);
                                hour2SchedBlock.setLayoutParams(schedBlock2Param);

                                int height3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, hour3ListItem.getContext().getResources().getDisplayMetrics());
                                RelativeLayout.LayoutParams schedBlock3Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height3);
                                hour3SchedBlock.setLayoutParams(schedBlock3Param);

                                //set the text in the SECOND item
                                courseNameView = (TextView) hour2ListItem.findViewById(R.id.course_name);
                                startEndView = (TextView) hour2ListItem.findViewById(R.id.start_end);

                                courseNameView.setText(course[i]);
                                startEndView.setText(start[i] + " - " + end[i]);
                            }
                        }
                    }
                }
            }*/

           /* courseNameView = (TextView) findViewById(R.id.course_name);
            courseNameView.setText(course[0]);
            startEndView = (TextView) findViewById(R.id.start_end);
            startEndView.setText(start[0] + "-" + end[0]);*/

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    //Get value from spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        spinnerOption = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    /*public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 7 total pages (one for each day of the week)
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
                case 5:
                    return getString(R.string.title_section6).toUpperCase(l);
                case 6:
                    return getString(R.string.title_section7).toUpperCase(l);
            }
            return null;
        }
    }*/

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
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

}
