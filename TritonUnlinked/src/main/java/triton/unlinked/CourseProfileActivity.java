package triton.unlinked;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private TextView courseSectionView;
    private TextView courseDescView;
    private ScrollView courseScrollViewDesc;

    protected boolean showingDesc = false;
    private String courseSubj, courseNum;

    private JSONObject courseJson;

    //Variables pertaining to a particular course
    private String classes;
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
        courseSectionView = (TextView) findViewById(R.id.course_section);
        courseDescView = (TextView) findViewById(R.id.course_desc);
        courseScrollViewDesc = (ScrollView) findViewById(R.id.course_scrollview_desc);

        FragmentManager fm = getFragmentManager();
        mTaskFragment = (CourseProfileAsyncFragment) fm.findFragmentByTag("task");


        Bundle searchTerm = getIntent().getExtras();
        // If the Async Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null && searchTerm != null) {
            String value = searchTerm.getString("SearchValue");
            Log.i("CourseProfileActivity", "This is value passed into AsyncFrag" + value);
            String[] courseArr = value.split(" ");
            this.courseSubj = courseArr[0];
            this.courseNum = courseArr[courseArr.length-1];
            mTaskFragment = new CourseProfileAsyncFragment(this.courseSubj, this.courseNum);
            fm.beginTransaction().add(mTaskFragment, "task").commit();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                courseSectionView.setText("Section " + (position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        courseSubView.setText(this.courseSubj);
        courseNumView.setText(this.courseNum);
        if (course != null){
            try {
                mSectionsPagerAdapter.clearFragments();
                String description = course.getString("desc");
                if (!description.equalsIgnoreCase("null")){
                    description = "Description: " + description;
                    if (description.indexOf("Prerequisites") >= 0){
                        description = description.replace("Prerequisites:", "\n\nPrerequisites: ");
                    }
                    else
                        description = description.replace("Prerequisite:", "\n\nPrerequisites: ");
                }
                courseDescView.setText(description);

                LinearLayout titleBar = (LinearLayout) findViewById(R.id.courseTitleBar);
                titleBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CourseProfileActivity.this.showingDesc){
                            courseScrollViewDesc.setVisibility(View.GONE);
                        }
                        else{
                            courseScrollViewDesc.setVisibility(View.VISIBLE);
                        }
                        CourseProfileActivity.this.showingDesc = !CourseProfileActivity.this.showingDesc;
                    }
                });

                JSONArray sections = course.getJSONArray("sections");
                for (int i = 0; i < sections.length(); ++i){
                    JSONObject classesInSection = sections.getJSONObject(i);

                    String section = classesInSection.getString("num");
                    String prof_fname = classesInSection.getString("firstName");
                    String prof_lname = classesInSection.getString("lastName");

                    JSONArray classes = classesInSection.getJSONArray("classes");


                    CourseFragment courseFrag = new CourseFragment(section, prof_fname, prof_lname, classes);
                    mSectionsPagerAdapter.addFragment(courseFrag);
                }
                mSectionsPagerAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            courseSectionView.setText("Sorry, this course is not being offered.");
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<CourseFragment> createdFragments = new ArrayList<CourseFragment>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a CourseFragment (defined as a static inner class below).
            return createdFragments.get(position);
        }
        public void clearFragments(){
            createdFragments.clear();
        }
        public void addFragment(CourseFragment frag){
            createdFragments.add(frag);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return createdFragments.size();
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
        private String section;
        private String prof_name;
        private String discussion = new String("");
        private String lecture = new String("");
        private String finals  = new String("");
        private String location  = new String("");

        private String mapLink;

        private TextView sectionNumView;
        private TextView sectionProfView;
        private TextView sectionDescView;
        private TextView sectionTypeView;
        private TextView sectionLecView;
        private TextView sectionDiView;
        private TextView sectionFiView;
        private TextView sectionLocationView;
        private TextView sectionTimeView;

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
        public CourseFragment( String section, String prof_fname, String prof_lname,  JSONArray diffClasses) throws JSONException {
            //ArrayList<String> lectures = new ArrayList<String>();
            //ArrayList<String> discussions = new ArrayList<String>();
            //ArrayList<String> finals = new ArrayList<String>();

            for(int i = 0; i < diffClasses.length(); ++i){
                JSONObject aClass = diffClasses.getJSONObject(i);
                if (aClass.getString("type").equalsIgnoreCase("LE")){
                    String daysOfLecture = aClass.getString("day");
                    String timesOfLecture = aClass.getString("time");
                    //lectures.add(daysOfLecture + " " + timesOfLecture + "\n");
                    if (!daysOfLecture.equalsIgnoreCase("null") && !timesOfLecture.equalsIgnoreCase("null"))
                      this.lecture = this.lecture + daysOfLecture + " " + timesOfLecture + "\n";
                    String location = aClass.getString("bld") + " " + aClass.getString("room");
                    this.location = location;
                }
                else if (aClass.getString("type").equalsIgnoreCase("DI")){
                    String daysOfDiscussion = aClass.getString("day");
                    String timesOfDiscussion = aClass.getString("time");
                    //discussions.add(daysOfDiscussion + " " + timesOfDiscussion + "\n");
                    if (!daysOfDiscussion.equalsIgnoreCase("null") && !timesOfDiscussion.equalsIgnoreCase("null"))
                        this.discussion = this.discussion + daysOfDiscussion + " " + timesOfDiscussion + "\n";
                }
                else if (aClass.getString("type").equalsIgnoreCase("FI")){
                    String daysOfFinal = aClass.getString("day");
                    String timesOfFinal = aClass.getString("time");
                    //finals.add(daysOfFinal + " " + timesOfFinal + "\n");
                    if (!daysOfFinal.equalsIgnoreCase("null") && !timesOfFinal.equalsIgnoreCase("null"))
                         this.finals = this.finals + daysOfFinal + " " + timesOfFinal + "\n";
                }
            }
            this.section = section;
            this.prof_name = prof_fname + " " + prof_lname;

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_course_profile, container, false);

            //sectionNumView = (TextView) rootView.findViewById(R.id.course_section);
            sectionProfView = (TextView) rootView.findViewById(R.id.course_section_prof);
            sectionLecView = (TextView) rootView.findViewById(R.id.course_section_lecture);
            sectionDiView = (TextView) rootView.findViewById(R.id.course_section_discussions);
            sectionFiView = (TextView) rootView.findViewById(R.id.course_section_final);
            sectionLocationView = (TextView) rootView.findViewById(R.id.course_section_loc);
            sectionDescView = (TextView) rootView.findViewById(R.id.course_desc);


            //sectionNumView.setText(this.section);
            //sectionProfView.setText(this.prof_name);
            sectionProfView.setText(Html.fromHtml("<u>" + this.prof_name + "</u>"));
            sectionProfView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), ProfessorProfileActivity.class);
                    i.putExtra("SearchValue", CourseFragment.this.prof_name);
                    startActivity(i);
                }
            });
            sectionLecView.setText(this.lecture);
            sectionDiView.setText(this.discussion);
            sectionFiView.setText(this.finals);
            sectionLocationView.setText(this.location);
            sectionLocationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), RoomActivity.class);
                    i.putExtra("SearchValue", CourseFragment.this.location);
                    startActivity(i);
                }
            });
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
