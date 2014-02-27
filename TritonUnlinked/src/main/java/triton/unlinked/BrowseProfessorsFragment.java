package triton.unlinked;

import android.app.Fragment;
import android.app.ListFragment;
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
public class BrowseProfessorsFragment extends ListFragment {

    private String[] professors;
    private ArrayList<String> currMenuItems;
    private ArrayAdapter browseAdapter;

    public BrowseProfessorsFragment(){
        /*INSERT LOGIC TO GET COURSES OUT OF LOCAL DB */
        professors = new String[] {"Richard Ord", "Tupac Shakur", "The Great Gatsby", "Will.i.am"};

        currMenuItems = new ArrayList<String>();
        for (int i = 0; i < professors.length; ++i){
            currMenuItems.add(professors[i]);
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        browseAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, currMenuItems);
        setListAdapter(browseAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onListItemClick(ListView list, View v, int position, long id) {

    }
}
