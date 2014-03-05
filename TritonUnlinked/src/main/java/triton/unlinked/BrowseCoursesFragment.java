package triton.unlinked;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jeremykao on 2/27/14.
 */
public class BrowseCoursesFragment extends ListFragment {

    private ArrayList<String> subjCode, bicdCoursesNum, bicdCoursesTitle, cseCoursesNum, cseCoursesTitle;
    private ArrayList<String> currMenuItems;
    private Boolean subjCodeIsDisplayed, courseNumIsDisplayed;

    Activity browseActivity;
    private BrowseCoursesModel course_model_data;
    private BrowseCoursesRow[] course_row_data;
    ArrayList<String> subjectList;
    ArrayList<String> courseTitleList;
    ArrayList<String> courseDescList;
    private BrowseCoursesAdapter browseAdapter;

    public BrowseCoursesFragment(){
        /*
        subjCode = new ArrayList<String>(Arrays.asList("BICD", "BIMM", "CHEM", "CSE"));
        bicdCoursesNum = new ArrayList<String>(Arrays.asList("100", "110", "130", "134", "194", "210", "214", "215", "216", "218", "223", "273"));
        bicdCoursesTitle = new ArrayList<String>(Arrays.asList("Genetics", "Cell Biology", "Embryos, Genes, & Development",
                "Human Reproduction & Development", "Adv Topics-Cellular Dev"));
        cseCoursesNum = new ArrayList<String>(Arrays.asList("3", "7", "8A", "8B", "100", "110"));
        cseCoursesTitle = new ArrayList<String>(Arrays.asList("Fluency in Information Technology", "Introduction to Matlab",
                "Intro to Java", "Intro to OOP", "Advanced Data Structures", "Software Engineering"));
        */
       // currMenuItems = new ArrayList<String>();
       // for (int i = 0; i < subjCode.length; ++i){
       //     currMenuItems.add(subjCode[i]);
       // }
       // subjCodeIsDisplayed = true;
       // courseNumIsDisplayed = false;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        browseActivity = activity;

        subjectList = new ArrayList<String>();
        course_model_data = new BrowseCoursesModel(this.browseActivity);
        // Access the database and retrieve list of subjects
        course_model_data.open();
        course_row_data = course_model_data.getAllCoursesRows();
        course_model_data.close();

        for (int i = 0; i < course_row_data.length; i++) {
            subjectList.add(course_row_data[i].subject);
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //browseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, currMenuItems);
        browseAdapter = new BrowseCoursesAdapter(getActivity(), this.subjectList, new ArrayList<String>());
        setListAdapter(browseAdapter);
        subjCodeIsDisplayed = true;
        courseNumIsDisplayed = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        if (subjCodeIsDisplayed){
                    browseAdapter.addTitle(bicdCoursesNum);
                    browseAdapter.addDescription(bicdCoursesTitle);
                    browseAdapter.notifyDataSetChanged();

            subjCodeIsDisplayed = false;
            courseNumIsDisplayed = true;
        }
        else{
            //If clicked subject code, then now on course number page. then link to course profiles
        }

    }

}
