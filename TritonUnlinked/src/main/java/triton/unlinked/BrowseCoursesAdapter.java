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

public class BrowseCoursesAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<String> subjList;
    private ArrayList<String> title;
    private ArrayList<String> course;
    private boolean displayingCourseAndTitle;

    public BrowseCoursesAdapter(Context context, ArrayList<String> course, ArrayList<String> title) {
        this.context = context;
        setDisplayingCourseAndTitle(false);
        this.subjList = course;
        addCourseRow(course, title);
    }
    public int getCount(){
        return this.course.size();
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
    public void addCourseRow(ArrayList<String> course, ArrayList<String> title){
        this.course = course;
        this.title = title;
        if (course.size() > 0 && title.size() > 0 ){
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

        if (this.course.size() > position && !this.course.get(position).equalsIgnoreCase(""))
            titleText.setText(this.course.get(position));

        if (this.title.size() > position && !this.title.get(position).equalsIgnoreCase(""))
            descText.setText(this.title.get(position));
        else
            descText.setVisibility(View.GONE);

        return rowView;
    }
}
