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

    //Database data objects
    private BrowseCoursesModel course_model_data;
    private BrowseCoursesRow[] course_row_data;
    private BrowseProfessorsModel prof_model_data;
    private BrowseProfessorsRow[] prof_row_data;
    private BrowseRoomsModel room_model_data;
    private BrowseRoomsRow[] room_row_data;

    ArrayList<String> autoCorrectItemList;
    ArrayList<String> courseList;
    ArrayList<String> profList;
    ArrayList<String> roomList;

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

        //initialize the autocomplete section of the code
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

        //create and populate the lists of autocomplete items
        initializeAutoCompleteArrays();
        autoCorrectItemList = courseList;

        ArrayAdapter<String> acAdapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_item, autoCorrectItemList);
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

    //Get value from spinner as well as setting autocomplete adapter
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        spinnerOption = parent.getItemAtPosition(pos).toString();
        setAutoCompleteAdapter(spinnerOption);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    //helper method to verify that data is sanitized else go into browse activity
    private void checkInputAndCreateNewActivity(String str){
        boolean found = false;
        for(String a: autoCorrectItemList){
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
            i.putExtra("SelectedCategory", this.spinnerOption);
            startActivity(i);
        }
    }

    //set the adapter to different autocorrects based off of spinner option
    private void setAutoCompleteAdapter(String str){
        if(str.equals("Courses")){ autoCorrectItemList = courseList; }
        if(str.equals("Professors")){ autoCorrectItemList = profList; }
        if(str.equals("Rooms")){ autoCorrectItemList = roomList; }
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.searchBox);
        ArrayAdapter<String> acAdapter = new ArrayAdapter<String>(this, R.layout.autocomplete_list_item, autoCorrectItemList);
        autoCompleteTextView.setAdapter(acAdapter);
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

    //helper method to fill up autocomplete array lists
    private void initializeAutoCompleteArrays(){
        course_model_data = new BrowseCoursesModel(this);
        prof_model_data = new BrowseProfessorsModel(this);
        room_model_data = new BrowseRoomsModel(this);

        // Access the database and retrieve list of courses
        course_model_data.open();
        course_row_data = course_model_data.getAllCoursesRows();
        course_model_data.close();
        courseList = new ArrayList<String>();

        for (int i = 0; i < course_row_data.length; i++) {
            courseList.add(course_row_data[i].subject + " " + course_row_data[i].number);
        }

        for (String s : courseList)
        {
            Log.d("Course: ", s);
        }

        // Access the database and retrieve list of professors
        prof_model_data.open();
        prof_row_data = prof_model_data.getAllProfessorsRows();
        prof_model_data.close();

        profList = new ArrayList<String>();

        for (int i = 0; i < prof_row_data.length; i++) {
            profList.add(prof_row_data[i].fname + " " + prof_row_data[i].lname);
        }

        for (String p : profList)
        {
            Log.d("Professor: ", p);
        }

        // Access the database and retrieve list of rooms
        room_model_data.open();
        room_row_data = room_model_data.getAllRoomsRows();
        room_model_data.close();

        roomList = new ArrayList<String>();

        for (int i = 0; i < room_row_data.length; i++) {
            roomList.add(room_row_data[i].bld + " " + room_row_data[i].room);
        }

        for (String r : roomList)
        {
            Log.d("Room: ", r);
        }
    }
}

