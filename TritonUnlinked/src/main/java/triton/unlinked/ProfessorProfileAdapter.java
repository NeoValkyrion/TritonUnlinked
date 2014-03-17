package triton.unlinked;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jeremykao on 3/16/14.
 */
public class ProfessorProfileAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<CourseObject> profClassesList;

    public ProfessorProfileAdapter(Context context, ArrayList<CourseObject> classesList ) {
        this.context = context;
        this.profClassesList = classesList;
    }
    public int getCount(){
        return this.profClassesList.size();
    }
    public long getItemId(int position){
        return position;
    }
    public Object getItem(int position){
        return this.profClassesList.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.professor_class_list_item, parent, false);

        TextView classCode = (TextView) rowView.findViewById(R.id.professorClassCode);
        TextView className = (TextView) rowView.findViewById(R.id.professorClassName);

        Collections.sort(this.profClassesList, new Comparator<CourseObject>() {
            @Override
            public int compare(CourseObject course1, CourseObject course2) {
                if (course1.getSubject().indexOf(" ") > -1) {
                    int numberStart = course1.getSubject().indexOf(" ") + 1;

                    /* getting the subject numbers */
                    String subj1 = course1.getSubject().substring(numberStart, course1.getSubject().length());
                    String subj2 = course2.getSubject().substring(numberStart, course2.getSubject().length());

                    /* remove letters from subject number */
                    Pattern pattern = Pattern.compile("([A-Z])");
                    Matcher matcher = pattern.matcher(subj1);
                    if (matcher.find()) {
                        subj1 = subj1.substring(0, matcher.start());
                    }
                    matcher = pattern.matcher(subj2);
                    if (matcher.find()) {
                        subj2 = subj2.substring(0, matcher.start());
                    }

                    if (subj1.length() - subj2.length() == 0) {
                        return subj1.compareTo(subj2);
                    } else if (subj1.length() - subj2.length() > 0) {
                        String padding = "";
                        for (int i = 0; i < subj1.length() - subj2.length(); ++i) {
                            padding += "0";
                        }
                        return subj1.compareTo(padding + subj2);
                    } else {
                        String padding = "";
                        for (int i = 0; i < subj2.length() - subj1.length(); ++i) {
                            padding += "0";
                        }
                        return (padding + subj1).compareTo(subj2);
                    }
                }
                int swag = course1.getSubject().compareTo(course2.getSubject());
                return swag;
            }
        });

        classCode.setText(this.profClassesList.get(position).getSubject());
        className.setText(this.profClassesList.get(position).getTitle());

        return rowView;
    }
}
