package triton.unlinked;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BrowseCoursesAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<String> title;
    private ArrayList<String> description;

    public BrowseCoursesAdapter(Context context, ArrayList<String> title, ArrayList<String> description) {
        this.context = context;
        this.title = title;
        this.description = description;
        Log.i("BrowseCoursesAdapter", "BrowseCoursesAdapter constructor — get title 0th element " + this.title.get(0));
    }
    public int getCount(){
        return this.title.size();
    }
    public long getItemId(int position){
        return position;
    }
    public Object getItem(int position){
        return position;
    }
    public void addDescription(ArrayList<String> description){
        this.description = description;
    }
    public void addTitle(ArrayList<String> title){
        this.title = title;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.browse_list_item, parent, false);
        TextView titleText = (TextView) rowView.findViewById(R.id.courseTitleListItem);
        TextView descText = (TextView) rowView.findViewById(R.id.courseDescListItem);
        Log.i("BrowseCoursesAdapter", "BrowseCoursesAdapter.getView() — get title Text " + this.title.get(position));
        if (this.title.size() > position && !this.title.get(position).equalsIgnoreCase(""))
            titleText.setText(this.title.get(position));
        if (this.description.size() > position && !this.description.get(position).equalsIgnoreCase(""))
            descText.setText(this.description.get(position));
        else
            descText.setVisibility(View.GONE);
        return rowView;
    }
}
