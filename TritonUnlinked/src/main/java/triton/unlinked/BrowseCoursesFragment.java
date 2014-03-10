package triton.unlinked;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    Activity browseActivity;
    private BrowseCoursesModel course_model_data;
    private BrowseCoursesRow[] course_row_data;
    ArrayList<String> subjectList;
    ArrayList<String> courseCourseList = new ArrayList<String>();
    ArrayList<String> courseTitleList = new ArrayList<String>();
    private BrowseCoursesAdapter browseAdapter;

    public BrowseCoursesFragment(){

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        browseActivity = activity;

        course_model_data = new BrowseCoursesModel(this.browseActivity);
        // Access the database and retrieve list of subjects
        course_model_data.open();
        subjectList = course_model_data.getAllSubjects();
        course_model_data.close();


        browseAdapter = new BrowseCoursesAdapter(this.getActivity(), this.subjectList, new ArrayList<String>());
        this.setListAdapter(browseAdapter);
        /*for (int i = 0; i < course_row_data.length; i++) {
            subjectList.add(course_row_data[i].subject);
        }*/

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //browseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, currMenuItems);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        if (!browseAdapter.getDisplayingCourseAndTitle()){
            this.courseTitleList.clear();
            this.courseCourseList.clear();

            course_model_data.open();
            course_row_data = course_model_data.getAllBySubject(subjectList.get(position));
            course_model_data.close();

            for (int i = 0; i < course_row_data.length; ++i){
                this.courseCourseList.add(course_row_data[i].course);
                this.courseTitleList.add(course_row_data[i].title);
            }
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.push_left_out);
            v.startAnimation(anim);

            browseAdapter.addCourseRow(this.courseCourseList, this.courseTitleList);
            browseAdapter.notifyDataSetChanged();
        }
        else{
            //If clicked subject code, then now on course number page. then link to course profiles
            String[] courseTitle = this.courseTitleList.get(position).split(" ");
            Intent intent = new Intent(getActivity(), CourseProfileActivity.class);
            intent.putExtra("subject", courseTitle[0]);
            intent.putExtra("num", courseTitle[courseTitle.length-1]);
            startActivity(intent);
        }

    }
    public void onBackPressed(){
        if (browseAdapter.getDisplayingCourseAndTitle()){
            browseAdapter.addCourseRow(this.subjectList, new ArrayList<String>());
            browseAdapter.notifyDataSetChanged();
        }
        else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

    }

}
