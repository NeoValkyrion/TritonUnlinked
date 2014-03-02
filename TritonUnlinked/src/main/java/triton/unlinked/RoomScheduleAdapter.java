package triton.unlinked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.line);
        textView.setText(values[position]);

        return rowView;
    }
}