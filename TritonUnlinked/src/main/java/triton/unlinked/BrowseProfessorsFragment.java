package triton.unlinked;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jeremykao on 2/27/14.
 */
public class BrowseProfessorsFragment extends ListFragment {

    Activity browseActivity;
    private ArrayList<String> professors = new ArrayList<String>();
    private BrowseProfessorsRow[] professor_model_row;
    private BrowseProfessorsModel professor_model_data;
    private ArrayAdapter browseAdapter;

    public BrowseProfessorsFragment(){

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        browseActivity = activity;

        professors.clear();
        professor_model_data = new BrowseProfessorsModel(this.browseActivity);
        // Access the database and retrieve list of subjects
        professor_model_data.open();
        professor_model_row = professor_model_data.getAllProfessorsRows();
        professor_model_data.close();

        for (int i = 0; i < professor_model_row.length; ++i){
            professors.add(professor_model_row[i].fname + " " + professor_model_row[i].lname);
        }

        browseAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, professors);
        this.setListAdapter(browseAdapter);
        /*for (int i = 0; i < course_row_data.length; i++) {
            subjectList.add(course_row_data[i].subject);
        }*/

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        Intent i = new Intent(getActivity(), ProfessorProfileActivity.class);
        i.putExtra("name", professors.get(position));
        startActivity(i);
    }
    public void onBackPressed(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
