package triton.unlinked;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowseCoursesAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<CourseObject> coursesList;
    private boolean displayingCourseAndTitle;

    public BrowseCoursesAdapter(Context context, ArrayList<CourseObject> coursesList ) {
        this.context = context;
        setDisplayingCourseAndTitle(false);
        this.coursesList = coursesList;
        addCourseRow(this.coursesList);
    }
    public int getCount(){
        return this.coursesList.size();
    }
    public long getItemId(int position){
        return position;
    }
    public Object getItem(int position){
        return position;
    }
    public void setDisplayingCourseAndTitle(boolean val){
        this.displayingCourseAndTitle = val;
    }
    public boolean getDisplayingCourseAndTitle(){
        return this.displayingCourseAndTitle;
    }

    public void addCourseRow(ArrayList<CourseObject> course){
        this.coursesList = course;
        if ( !course.get(0).getTitle().equalsIgnoreCase("") ){
            setDisplayingCourseAndTitle(true);
        }
        else{
            setDisplayingCourseAndTitle(false);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.browse_list_item, parent, false);

        TextView titleText = (TextView) rowView.findViewById(R.id.courseTitleListItem);
        TextView descText = (TextView) rowView.findViewById(R.id.courseDescListItem);

        Collections.sort(this.coursesList, new Comparator<CourseObject>() {
            @Override
            public int compare(CourseObject course1, CourseObject course2) {
                if (course1.getSubject().indexOf(" ") > -1){
                    int numberStart = course1.getSubject().indexOf(" ") + 1;

                    /* getting the subject numbers */
                    String subj1 = course1.getSubject().substring(numberStart, course1.getSubject().length());
                    String subj2 = course2.getSubject().substring(numberStart, course2.getSubject().length());

                    /* remove letters from subject number */
                    Pattern pattern = Pattern.compile("([A-Z])");
                    Matcher matcher = pattern.matcher(subj1);
                    if (matcher.find()){
                        subj1 = subj1.substring(0,matcher.start());
                    }
                    matcher = pattern.matcher(subj2);
                    if (matcher.find()){
                        subj2 = subj2.substring(0,matcher.start());
                    }

                    if ( subj1.length() - subj2.length() == 0 ){
                        return subj1.compareTo(subj2);
                    }
                    else if (subj1.length() - subj2.length() > 0){
                        String padding = "";
                        for (int i = 0; i < subj1.length() - subj2.length(); ++i){
                            padding += "0";
                        }
                        return subj1.compareTo(padding + subj2);
                    }
                    else{
                        String padding = "";
                        for (int i = 0; i < subj2.length() - subj1.length(); ++i){
                            padding += "0";
                        }
                        return (padding + subj1).compareTo(subj2);
                    }
                }
                int swag = course1.getSubject().compareTo(course2.getSubject());
                Log.i("BrowseCoursesAdapter", "Comparison: " + course1.getSubject() + " - " + course2.getSubject() + " = " + swag);
                return swag;
            }
        });

        titleText.setText(this.coursesList.get(position).getSubject());
        if ( !this.coursesList.get(position).getTitle().equalsIgnoreCase("") )
            descText.setText(this.coursesList.get(position).getTitle());
        else
            descText.setVisibility(View.GONE);

        return rowView;
    }
}
