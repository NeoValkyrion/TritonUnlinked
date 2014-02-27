package triton.unlinked;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jeremykao on 2/27/14.
 */
public class BrowseCoursesFragment extends ListFragment {

    private String[] subjCode, bicdCoursesNum, bicdCoursesTitle, cseCoursesNum, cseCoursesTitle;
    private ArrayList<String> currMenuItems;
    private Boolean subjCodeIsDisplayed, courseNumIsDisplayed;
    private ArrayAdapter browseAdapter;

    public BrowseCoursesFragment(){
        /*INSERT LOGIC TO GET COURSES OUT OF LOCAL DB */
        subjCode = new String[] {"BICD", "BIMM", "CHEM", "CSE"};
        bicdCoursesNum = new String[]{"100", "110", "130", "134", "194", "210", "214", "215", "216", "218", "223", "273"};
        bicdCoursesTitle = new String[] {"Genetics", "Cell Biology", "Embryos, Genes, & Development",
                "Human Reproduction & Development", "Adv Topics-Cellular Dev"};
        cseCoursesNum = new String[] {"3", "7", "8A", "8B", "100", "110"};
        cseCoursesTitle = new String[] {"Fluency in Information Technology", "Introduction to Matlab",
                "Intro to Java", "Intro to OOP", "Advanced Data Structures", "Software Engineering"};

        currMenuItems = new ArrayList<String>();
        for (int i = 0; i < subjCode.length; ++i){
            currMenuItems.add(subjCode[i]);
        }
        subjCodeIsDisplayed = true;
        courseNumIsDisplayed = false;

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
        if (subjCodeIsDisplayed){
            switch (position){
                case 0:
                    browseAdapter.clear();
                    for (int i = 0; i < bicdCoursesNum.length; ++i){
                        browseAdapter.add(bicdCoursesNum[i]);
                    }
                    browseAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    browseAdapter.clear();
                    for (int i = 0; i < cseCoursesNum.length; ++i){
                        browseAdapter.add(cseCoursesNum[i]);
                    }
                    browseAdapter.notifyDataSetChanged();
                    break;
            }
            subjCodeIsDisplayed = false;
            courseNumIsDisplayed = true;
        }
        else{
            //If clicked subject code, then now on course number page. then link to course profiles
        }

    }

}
