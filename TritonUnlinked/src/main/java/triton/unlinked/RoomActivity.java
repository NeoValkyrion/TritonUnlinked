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
import android.widget.ListView;
import android.widget.TextView;

public class RoomActivity extends Activity {

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
    ListView timeListView;
    String[] times = {"8:00a", "9:00a", "10:00a", "11:00a", "12:00p", "1:00p", "2:00p", "3:00p", "4:00p", "5:00p", "6:00p", "7:00p", "8:00p", "9:00p"};


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
    private static String url = "http://tritonunlinked.herokuapp.com/room?bld=CENTR&room=115";

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

    private String building;
    private String room;
    private JSONArray classesArr;
    private JSONObject[] classes;
    private String[] course;
    private String[] start;
    private String[] end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        new GetRoom().execute();

        //grab the data passed in from the search bar -- NEED TO CHANGE TO PROPER NAMES
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("new_variable_name");
        }*/

        RoomScheduleAdapter adapter = new RoomScheduleAdapter(this, times);

        timeListView = (ListView) findViewById(R.id.schedule);
        timeListView.setAdapter(adapter);

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
                    classes = new JSONObject[classesArr.length()];
                    /*course = new String[classesArr.length()];
                    start = new String[classesArr.length()];
                    end = new String[classesArr.length()];*/


                    for(int i=0; i<classesArr.length(); i++) {
                        Log.d("RoomActivity: ", "" + classesArr.getJSONObject(i));
                        classes[i] = classesArr.getJSONObject(i);

                        /*course[i] = classes[i].getString(TAG_CLASS);
                        start[i] = classes[i].getString(TAG_START);
                        end[i] = classes[i].getString(TAG_END);*/
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

            //TODO: need to access the data for each listview item (via the adapter?)
            courseNameView = (TextView) findViewById(R.id.course_name);
            //courseNameView.setText(course[0]);
            startEndView = (TextView) findViewById(R.id.start_end);
            //startEndView.setText(start[0] + "-" + end[0]);

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
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
