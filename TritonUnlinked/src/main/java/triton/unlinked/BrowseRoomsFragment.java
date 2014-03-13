package triton.unlinked;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jeremykao on 2/27/14.
 */
public class BrowseRoomsFragment extends ListFragment {

    Activity browseActivity;
    private ArrayList<String> rooms = new ArrayList<String>();
    private BrowseRoomsRow[] rooms_model_row;
    private BrowseRoomsModel rooms_model_data;
    private ArrayAdapter browseAdapter;

    public BrowseRoomsFragment(){

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        browseActivity = activity;

        rooms.clear();
        rooms_model_data = new BrowseRoomsModel(this.browseActivity);

        rooms_model_data.open();
        rooms_model_row = rooms_model_data.getAllRoomsRows();
        rooms_model_data.close();

        for (int i = 0; i < rooms_model_row.length; ++i){
            rooms.add(rooms_model_row[i].bld + " " + rooms_model_row[i].room);
        }

        browseAdapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_list_item_1, rooms);
        this.setListAdapter(browseAdapter);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {
        Intent i = new Intent(getActivity(), RoomActivity.class);
        i.putExtra("SearchValue", rooms.get(position));
        startActivity(i);
    }
    public void onBackPressed(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
}
