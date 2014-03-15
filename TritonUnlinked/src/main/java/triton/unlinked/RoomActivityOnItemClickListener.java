package triton.unlinked;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class RoomActivityOnItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();
        //String selectedValue = (String) parent.getAdapter().getItem(position);
        TextView textViewItem = ((TextView) view.findViewById(R.id.course_name));
        String listItemText = textViewItem.getText().toString();

        if( !(listItemText.equals("")) ) {
            Log.d("RoomActivityOnItemClickListener", "list item text: " + listItemText);
            Intent i = new Intent(context, CourseProfileActivity.class);
            i.putExtra("SearchValue",listItemText);
            context.startActivity(i);
        }
    }
}