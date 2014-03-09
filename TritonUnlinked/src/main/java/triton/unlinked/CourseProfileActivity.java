package triton.unlinked;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseProfileActivity extends FragmentActivity implements CourseProfileAsyncFragment.TaskCallbacks {
    private static final String TAG = CourseProfileActivity.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private TextView courseNameView;
    private TextView courseNumView;
    private TextView courseSubView;
    private TextView courseDescView;

    private JSONObject courseJson;

    //Variables pertaining to a particular course
    private String subject;
    private String number;
    private String title;
    private String desc;
    private JSONArray sectionsJson;

    //Local Database stuff
    /*
    private CourseModel model_data;
    private CourseRow pulled_data;
    */

    private CourseProfileAsyncFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_profile);

        //Initialize views
        courseSubView = (TextView) findViewById(R.id.course_sub);
        courseNumView = (TextView) findViewById(R.id.course_num);

        FragmentManager fm = getFragmentManager();
        mTaskFragment = (CourseProfileAsyncFragment) fm.findFragmentByTag("task");

        // If the Async Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) {
            mTaskFragment = new CourseProfileAsyncFragment();
            fm.beginTransaction().add(mTaskFragment, "task").commit();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    /*
     * Callback methods from CourseProfileAsyncFragment
     */
    @Override
    public void onPreExecute() {
        Log.i(TAG, "onPreExecute()");
    }

    @Override
    public void onPostExecute(JSONObject course) {
        Log.i(TAG, "onPostExecute()");
        try {
            courseSubView.setText(course.getString("subject"));
            courseNumView.setText(course.getString("num"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a CourseFragment (defined as a static inner class below).
            return CourseFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
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
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CourseFragment extends Fragment {

        //Variables pertaining to a single section
        private String number;
        private String professor;
        private JSONArray classesJson;

        //Variables pertaining to a single class
        private String type;

        private TextView sectionNumView;
        private TextView sectionProfView;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static CourseFragment newInstance(int sectionNumber) {
            CourseFragment fragment = new CourseFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public CourseFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_profile, container, false);
            sectionNumView = (TextView) rootView.findViewById(R.id.course_section);
            sectionProfView = (TextView) rootView.findViewById(R.id.course_section_prof);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
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
