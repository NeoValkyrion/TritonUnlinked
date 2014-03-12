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
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jeremykao on 2/27/14.
 */
public class BrowseCoursesFragment extends ListFragment{

    Activity browseActivity;
    private BrowseCoursesModel course_model_data;
    private BrowseCoursesRow[] course_row_data;
    ArrayList<String> subjectList;
    ArrayList<CourseObject> courses = new ArrayList<CourseObject>();
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

        for (int i = 0; i < subjectList.size(); ++i){
            this.courses.add(new CourseObject(this.subjectList.get(i), ""));
        }

        browseAdapter = new BrowseCoursesAdapter(this.getActivity(), this.courses);
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

            course_model_data.open();
            course_row_data = course_model_data.getAllBySubject(this.courses.get(position).getSubject());
            course_model_data.close();

            this.courses.clear();
            for (int i = 0; i < course_row_data.length; ++i){
                this.courses.add(new CourseObject(course_row_data[i].course, course_row_data[i].title));
            }

            int wantedPosition = position;
            int firstPosition = list.getFirstVisiblePosition() - list.getHeaderViewsCount();
            wantedPosition = wantedPosition - firstPosition;

            Animation anim = AnimationUtils.loadAnimation(browseActivity, R.anim.push_left_out);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    browseAdapter.addCourseRow(courses);
                    browseAdapter.notifyDataSetChanged();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            list.getChildAt(wantedPosition).startAnimation(anim);
        }
        else{
            //If clicked subject code, then now on course number page. then link to course profiles
            String[] courseTitle = this.courses.get(position).getTitle().split(" ");
            Intent intent = new Intent(getActivity(), CourseProfileActivity.class);
            intent.putExtra("subject", courseTitle[0]);
            intent.putExtra("num", courseTitle[courseTitle.length-1]);
            startActivity(intent);
        }

    }
    public void onBackPressed(){
        if (browseAdapter.getDisplayingCourseAndTitle()){
            this.courses.clear();
            for (int i = 0; i < subjectList.size(); ++i){
                this.courses.add(new CourseObject(this.subjectList.get(i), ""));
            }

            browseAdapter.addCourseRow(this.courses);
            browseAdapter.notifyDataSetChanged();
        }
        else{
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }

    }
}
