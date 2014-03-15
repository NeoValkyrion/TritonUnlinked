package triton.unlinked;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.util.TypedValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RoomScheduleAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private JSONArray classesArr;
    private String selectedDay;

    public RoomScheduleAdapter(Context context, String[] values) {
        super(context, R.layout.room_schedule_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        /*
        View view = convertView; // re-use an existing view, if one is supplied
        if (view == null) { // otherwise create a new one
            view = inflater.inflate(R.layout.room_schedule_row, null);
        }
        // set view properties to reflect data for the given row
        view.FindViewById<TextView>(Resource.id.course_name).Text = items[position];
        // return the view, populated with data, for display
        return view;
        */
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;

        if(rowView == null) {
            rowView = inflater.inflate(R.layout.room_schedule_row, parent, false);
            TextView time = (TextView) rowView.findViewById(R.id.time);
            time.setText(values[position]);

            View scheduleBlock1 = rowView.findViewById(R.id.schedule_block1);
            View scheduleBlock2 = rowView.findViewById(R.id.schedule_block2);
            scheduleBlock1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
            scheduleBlock2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));

            TextView courseName = (TextView) rowView.findViewById(R.id.course_name);
            TextView startEnd = (TextView) rowView.findViewById(R.id.start_end);
            courseName.bringToFront();
            startEnd.bringToFront();
            courseName.setText("");
            startEnd.setText("");

            /*if(classesArr != null) {
                rowView = updateItem(rowView, time);
            }*/
        }
        else {
            TextView time = (TextView) rowView.findViewById(R.id.time);
            time.setText(values[position]);
            //other stuff...
            if(classesArr != null) {
                rowView = updateItem(rowView, time);
            }
        }

        return rowView;
    }

    public void setClassesArr(JSONArray arr) {
        this.classesArr = arr;
    }

    public void setSelectedDay(String day) {
        this.selectedDay = day;
    }

    private boolean selectedDayHasClasses() {
        JSONObject currClass;
        String day;
        try{
            for(int i=0; i<classesArr.length(); i++) {
                currClass = classesArr.getJSONObject(i);
                day = currClass.getString("day");
                if(day.contains(selectedDay)) {
                    return true;
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    private View updateItem (View itemToUpdate, TextView time) {
        //cases: 50 min class, 80 min class (either starts on the hour or on the half hour), 3 hour class/final (could start at :30)
        int startHr, startMin, endHr, endMin;
        boolean fill = false;
        JSONObject currClass;
        String courseName, day, start, end;
        String listItemTime = time.getText().toString();
        TextView courseNameView, courseTimeView;
        View scheduleBlock1, scheduleBlock2;

        courseNameView = (TextView) itemToUpdate.findViewById(R.id.course_name);
        courseTimeView = (TextView) itemToUpdate.findViewById(R.id.start_end);
        scheduleBlock1 = itemToUpdate.findViewById(R.id.schedule_block1);
        scheduleBlock2 = itemToUpdate.findViewById(R.id.schedule_block2);

        if(!selectedDayHasClasses()) {
            courseNameView.setText("");
            courseTimeView.setText("");
            scheduleBlock1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
            scheduleBlock2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
        }
        else {
            try {
                for(int i=0; i<classesArr.length(); i++) {
                    currClass = classesArr.getJSONObject(i);
                    courseName = currClass.getString("class");
                    day = currClass.getString("day");
                    String[] startEnd = currClass.getString("time").split(" - ");
                    start = startEnd[0];
                    end = startEnd[1];

                    startHr = Integer.parseInt( start.split(":")[0] );
                    startMin = Integer.parseInt( start.split(":")[1].substring(0,2) ); //use substring to get rid of the 'a'/'p'
                    endHr = Integer.parseInt( end.split(":")[0] );
                    endMin = Integer.parseInt( end.split(":")[1].substring(0,2) ); //use substring to get rid of the 'a'/'p'

                    // only display the classes that occur on the selected day
                    if( day.contains(selectedDay) ) {
                        //Log.d("RoomScheduleAdapter", "selected day is " + selectedDay + ". class: " + courseName + ", day: " + day + ", start: " + start + ", end: " + end);
                        //Log.d("RoomScheduleAdapter", "startHr: " + startHr + ", endHr: " + endHr);
                        // check that the starting hour matches and the 'a'/'p' matches
                        if( (startHr == Integer.parseInt(listItemTime.split(":")[0])) && (start.split(":")[1].charAt(2) == listItemTime.split(":")[1].charAt(2)) ) {
                            fill = true;
                            //case 1: 50 minute class (always starts at X:00 and ends at X:50)
                            if(startHr == endHr) {
                                int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, itemToUpdate.getContext().getResources().getDisplayMetrics());
                                RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                                scheduleBlock1.setLayoutParams(schedBlock1Param);

                                courseNameView.setText(courseName);
                                courseTimeView.setText(start + " - " + end);

                                fill = true;
                            }
                            else {
                                //case 2: 80 minute class
                                if( ((startHr + 1) % 12) == (endHr % 12) ) {
                                    Log.d("RoomScheduleAdapter", "80 minute class: " + courseName + ", day: " + day + ", start: " + start + ", end: " + end);
                                    //case 2a: starts on the hour, ends at x:20 (ex: 8:00-9:20) -- in this case, the text should be set in the FIRST item
                                    if( startMin == 0 ) {
                                        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, itemToUpdate.getContext().getResources().getDisplayMetrics());
                                        RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                                        scheduleBlock1.setLayoutParams(schedBlock1Param);

                                        courseNameView.setText(courseName);
                                        courseTimeView.setText(start + " - " + end);

                                        fill = true;
                                    }
                                    else {
                                        Log.d("RoomScheduleAdapter", "Are we getting here?? class: " + courseName + ", day: " + day + ", start: " + start + ", end: " + end);
                                        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, itemToUpdate.getContext().getResources().getDisplayMetrics());
                                        RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                                        schedBlock1Param.setMargins(0, height, 0, 0);
                                        scheduleBlock2.setLayoutParams(schedBlock1Param);

                                        fill = true;
                                    }
                                }

                                //case 3: 3 hour class
                                //courseNameView.setText(courseName);
                                //courseTimeView.setText(start + " - " + end);
                            }
                        }
                        //this case is for classes longer that 50 min, that require than 1 block to be set
                        else if( (endHr == Integer.parseInt(listItemTime.split(":")[0])) && end.split(":")[1].substring(2).equals(listItemTime.split(":")[1].substring(2)) ) {
                            fill = true;
                            //case 2: 80 minute class
                            if( ((startHr + 1) % 12) == (endHr % 12) ) {
                                //case 2a: starts on the hour, ends at x:20 (ex: 8:00-9:20) -- setting the SECOND box of the 2 required boxes (this one should be 20dp tall)
                                if( startMin == 0 ) {
                                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, itemToUpdate.getContext().getResources().getDisplayMetrics());
                                    RelativeLayout.LayoutParams schedBlock1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                                    scheduleBlock1.setLayoutParams(schedBlock1Param);

                                    fill = true;
                                }
                                //case 2b: starts on the half-hour, ends at x:50 (ex: 9:30-10:50) -- setting the SECOND box of the 2 required boxes (this one should be 20dp tall)
                                else if( startMin == 30 ) {
                                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, endMin, itemToUpdate.getContext().getResources().getDisplayMetrics());
                                    RelativeLayout.LayoutParams schedBlock2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                                    scheduleBlock1.setLayoutParams(schedBlock2Param);

                                    courseNameView.setText(courseName);
                                    courseTimeView.setText(start + " - " + end);

                                    fill = true;
                                }
                            }
                        }

                        if(!fill) {
                            Log.d("RoomScheduleAdapter", "fill was not set to true! class: " + courseName + ", day: " + day + ", start: " + start + ", end: " + end);
                            courseNameView.setText("");
                            courseTimeView.setText("");
                            scheduleBlock1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
                            scheduleBlock2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
                        }
                    }
                    /*else {
                        //Log.d("RoomScheduleAdapter", "Class not on selected day. What class is this? class: " + courseName + ", day: " + day + ", start: " + start + ", end: " + end);
                        courseNameView.setText("");
                        courseTimeView.setText("");
                        scheduleBlock1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
                        scheduleBlock2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
                    }*/
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return itemToUpdate;
    }
}