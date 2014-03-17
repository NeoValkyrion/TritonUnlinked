package triton.unlinked;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.app.Activity;
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

public  class ProfessorProfileActivity extends ListActivity{

    //JSON field tags for Courses
    private static final String TAG_NAME = "name";
    private static final String TAG_FIRSTNAME = "firstName";
    private static final String TAG_LASTNAME = "lastName";
    private static final String TAG_CODE = "code";
    private static final String TAG_TITLE = "title";
    private static final String TAG_SECTION = "section";
    private static final String TAG_CLASSES = "classes";


    //Variables used for
    private String firstName;
    private String lastName;
    protected Context ctx;
    private GetProfessor getProfAsync;

    private TextView profName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = getApplicationContext();
        setContentView(R.layout.activity_professor_profile_2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            String value = extras.getString("SearchValue");
            String[] dataSplit = value.split(" ");
            firstName = dataSplit[0];
            lastName = dataSplit[1];
            Log.v("TAG", firstName);
            Log.v("TAG", lastName);

        }
        profName = (TextView) findViewById(R.id.professorName);
        this.profName.setText(firstName + " " + lastName);
        this.getProfAsync = new GetProfessor();
        getProfAsync.execute();
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        Intent i = new Intent(this.ctx, CourseProfileActivity.class);
        Object classClicked = getListAdapter().getItem(position);
        i.putExtra("SearchValue", ((CourseObject) classClicked).getSubject());
        startActivity(i);
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetProfessor extends AsyncTask<Void, Void, Void> {

        protected ArrayList<CourseObject> profClasses = new ArrayList<CourseObject>();

        @Override
        protected Void doInBackground(Void... arg0) {
            //Create service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = null;
            jsonStr = sh.makeServiceCall(generateUrl(firstName, lastName), ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if(jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    //Save values from JSON Object
                    firstName = jsonObj.getString(TAG_FIRSTNAME);
                    lastName = jsonObj.getString(TAG_LASTNAME);

                    JSONArray classesJson = jsonObj.getJSONArray(TAG_CLASSES);

                    for(int i=0; i < classesJson.length(); i++){
                        JSONObject classes = classesJson.getJSONObject(i);

                        CourseObject courseObj = new CourseObject(classes.getString(TAG_CODE),
                                classes.getString(TAG_TITLE));
                        this.profClasses.add(courseObj);
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
            ProfessorProfileAdapter profClassesAdapter = new ProfessorProfileAdapter(ProfessorProfileActivity.this.ctx, this.profClasses);
            setListAdapter(profClassesAdapter);
        }

       private String generateUrl(String fname, String lname){
           return "http://tritonunlinked.herokuapp.com/professor?fname="+ fname +"&lname=" + lname;
        }
    }


}
