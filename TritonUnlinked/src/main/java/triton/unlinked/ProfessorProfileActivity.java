package triton.unlinked;

import android.app.Activity;
import android.app.ListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Locale;

//public class ProfessorProfileActivity extends FragmentActivity implements ProfessorProfileActivity.TaskCallBacks {
public  class ProfessorProfileActivity extends ListActivity{
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    //SectionsPagerAdapter profAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
   // ListView profView;
    //

    private ProgressDialog pDialog;

    //Temporary, must be replaced by constructed query string given course information
    //private static String url = "http://tritonunlinked.herokuapp.com/professor?fname=Paul&lname=Kube";


    private TextView professorNameView;
    private TextView codeView;
    private TextView titleView;
    private TextView classNameView;
    private TextView sectionView;
    private TextView professorDescView;
    private ProfessorAdapter browseAdapter;

    //JSON field tags for Courses
    private static final String TAG_NAME = "name";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_CODE = "code";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SECTION = "section";
    private static final String TAG_CLASSES = "classes";


    //Variables used for
    private String name;
    private String firstName;
    private String lastName;
    private String[] code;
    private String[] title;
    private String[] section;
    static ArrayList<ProfessorObject> courses = new ArrayList<ProfessorObject>();

    //Professor names
   // private JSONObject[] classes;
    private JSONArray classesJson;
    //private JSONObject[] sectionsJson;


    //Local Database stuff
    /*
    private CourseModel model_data;
    private CourseRow pulled_data;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor_profile);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String value = extras.getString("SearchValue");
            String[] dataSplit = value.split(" ");
            firstName = dataSplit[0];
            lastName = dataSplit[1];
         }

        //TODO: update with a GetProfessor().execute once the internal database has been flushed out
        new GetProfessor().execute();

        Log.d("courses of prof ", courses.size() + "");


        //ListView listView = (ListView)findViewById(android.R.id.list);
        //browseAdapter = new ProfessorAdapter(this, courses);
        //this.setListAdapter(browseAdapter);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
       // profAdapter = new SectionsPagerAdapter(this));
        // Set up the ViewPager with the sections adapter.
       // mViewPager = (ViewPager) findViewById(R.id.prof_pager);
       // profView = (ListView) findViewById(R.id.prof_pager);
       // profView.setAdapter(profAdapter);
        //mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    //TODO: update with GetProfessor Async method
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetProfessor extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ProfessorProfileActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;
            Log.d("FIESTNAME", firstName);
            Log.d("LASTNAME", lastName);

            jsonStr = sh.makeServiceCall(generateUrl(firstName, lastName), ServiceHandler.POST);

            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Save values from JSON Object
                    name = jsonObj.getString(TAG_NAME);
                    firstName = jsonObj.getString(TAG_FIRSTNAME);
                    lastName = jsonObj.getString(TAG_LASTNAME);

                    Log.d("Response:", name);
                    Log.d("Response:", firstName);
                    classesJson = jsonObj.getJSONArray(TAG_CLASSES);
                   // sectionsJson = new JSONObject[classesJson.length()];

                    title = new String[classesJson.length()];
                    code = new String[classesJson.length()];
                    section = new String[classesJson.length()];

                    for(int i=0; i < classesJson.length(); i++){
                        JSONObject classes = classesJson.getJSONObject(i);
                        //sectionsJson[i] = classes;

                        title[i] = classes.getString(TAG_TITLE);
                        code[i] = classes.getString(TAG_CODE);
                        section[i] = classes.getString(TAG_SECTION);
                        courses.add(new ProfessorObject(title[i], code[i], section[i]));
                        Log.d("Section:",classesJson.getString(i));
                        Log.d("courses TITLE:",courses.get(i).getTitle());
                        Log.d("courses CODE:",courses.get(i).getCode());
                        Log.d("courses SECTIOn:",courses.get(i).getSection());
                    }

                } catch (JSONException e) {
                    Log.d("Error:", "jsonObj returned exception");
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
            setContentView(R.layout.activity_professor_profile);
            professorNameView = (TextView) findViewById(R.id.professor_header);
            professorNameView.setText(firstName + " " + lastName);

            /*for(int i = 0; i < title.length; i++){

                titleView = (TextView) findViewById(R.id.prof_title_section);
                titleView.append("Title: "+ title[i] + "\n");
                codeView = (TextView) findViewById(R.id.prof_title_section);
                codeView.append("Code: " + code[i] + "\n");
                sectionView = (TextView) findViewById(R.id.prof_title_section);
                sectionView.append("Section: "+section[i] + "\n");
                sectionView.append("\n" );
           }*/

            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
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
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 0 total pages.
            return 1;
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
            View rootView = inflater.inflate(R.layout.fragment_professor_profile, container, false);
            return rootView;
        }
    }

   private String generateUrl(String fname, String lname){
       return "http://tritonunlinked.herokuapp.com/professor?fname="+ fname +"&lname=" + lname;
    }

}
