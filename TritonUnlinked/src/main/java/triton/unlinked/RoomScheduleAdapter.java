package triton.unlinked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.TypedValue;

public class RoomScheduleAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public RoomScheduleAdapter(Context context, String[] values) {
        super(context, R.layout.room_schedule_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.room_schedule_row, parent, false);
        TextView time = (TextView) rowView.findViewById(R.id.time);
        time.setText(values[position]);

        //TODO: figure out how to set the text for the class and how to hide/show/resize the transparent rectangle based on class time/length
        View scheduleBlock1 = rowView.findViewById(R.id.schedule_block1);
        View scheduleBlock2 = rowView.findViewById(R.id.schedule_block2);
        //scheduleBlock1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));
        scheduleBlock2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0));

        //sets the box to half the height of the listview item and halfway down (for classes that start at X:30)
        /*int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, context.getResources().getDisplayMetrics());
        RelativeLayout.LayoutParams schedBlock2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        schedBlock2Param.setMargins(0, height, 0, 0);
        scheduleBlock2.setLayoutParams(schedBlock2Param);*/


        TextView courseName = (TextView) rowView.findViewById(R.id.course_name);
        TextView startEnd = (TextView) rowView.findViewById(R.id.start_end);
        courseName.setText("");
        startEnd.setText("");

        return rowView;
    }
}