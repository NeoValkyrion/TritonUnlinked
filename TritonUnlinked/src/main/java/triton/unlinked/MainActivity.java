package triton.unlinked;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.graphics.Typeface;
import android.widget.TextView;

import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnItemSelectedListener {

    private String[] options = {"Courses", "Professors","Rooms"};
    private BrowseCoursesModel course_model_data;
    private BrowseCoursesRow[] course_row_data;
    ArrayList<String> subjectList;

    private String spinnerOption = "Courses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView myTextView=(TextView)findViewById(R.id.searchByHeader);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Regular.otf");
        myTextView.setTypeface(typeFace);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Spinner spinner = (Spinner) findViewById(R.id.searchOptionDropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ImageButton browseButton = (ImageButton) findViewById(R.id.browseButton);
        browseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), BrowseActivity.class);
                startActivity(i);
            }
        });

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.searchBox);

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    checkInputAndCreateNewActivity(autoCompleteTextView.getText().toString());
                    handled = true;
                }
                return handled;
            }
        });

        subjectList = new ArrayList<String>();
        course_model_data = new BrowseCoursesModel(this);

        // Access the database and retrieve list of subjects
        course_model_data.open();
        course_row_data = course_model_data.getAllCoursesRows();
        course_model_data.close();

        subjectList = new ArrayList<String>();

        for (int i = 0; i < course_row_data.length; i++) {
            subjectList.add(course_row_data[i].course);
        }

        for (String s : subjectList)
        {
            Log.d("Subject: ", s);
        }

        ArrayAdapter<String> acAdapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_item, subjectList);
        autoCompleteTextView.setAdapter(acAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    /**
     * Called when the user clicks the Course Page button
     */
    public void CoursePage(View view) {
        Intent intent = new Intent(this, CourseProfileActivity.class);
        startActivity(intent);
    }


    /**
     * Called when the user clicks the Professor Page button
     */
    public void ProfessorPage(View view) {
        Intent intent = new Intent(this, ProfessorProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Room Page button
     */
    public void RoomPage(View view) {
        Intent intent = new Intent(this, RoomActivity.class);
        startActivity(intent);
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

    //helper method to verify that data is sanitized else go into browse activity
    //TODO: Change this once the internal db is finished
    private void checkInputAndCreateNewActivity(String str){
        boolean found = false;
        for(String a: subjectList){
            if(a.equalsIgnoreCase(str)){
                found = true;
            }
        }
        if(found){
            createNewActivity(str);
            return;
        }
        else{
            Intent i = new Intent(getApplicationContext(), BrowseActivity.class);
            startActivity(i);
        }

    }

    //helper method to create activity and pass in string data to locations based on
    //spinner values.
    private void createNewActivity(String str){
        if(!str.isEmpty()){
            if(spinnerOption.equals("Courses")){
                Intent i = new Intent(getApplicationContext(), CourseProfileActivity.class);
                i.putExtra("SearchValue",str);
                startActivity(i);}
            else if(spinnerOption.equals("Professors")){
                Intent i = new Intent(getApplicationContext(), ProfessorProfileActivity.class);
                i.putExtra("SearchValue",str);
                startActivity(i);}
            else if(spinnerOption.equals("Rooms")){
                Intent i = new Intent(getApplicationContext(), RoomActivity.class);
                i.putExtra("SearchValue",str);
                startActivity(i);}
            }
    }

}

