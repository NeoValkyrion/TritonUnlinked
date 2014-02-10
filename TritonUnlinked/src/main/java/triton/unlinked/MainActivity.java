package triton.unlinked;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity {
    private String[] options = {"Courses", "Professors","Rooms"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.searchOptionDropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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
}