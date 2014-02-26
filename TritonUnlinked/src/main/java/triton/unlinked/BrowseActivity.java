package triton.unlinked;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

public class BrowseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        final String[] subjCode = {"BICD", "BIMM", "CHEM", "CSE"};
        final String[] bicdCoursesNum = {"100", "110", "130", "134", "194"};
        final String[] bicdCoursesTitle = {"Genetics", "Cell Biology", "Embryos, Genes, & Development",
                "Human Reproduction & Development", "Adv Topics-Cellular Dev"};
        final String[] cseCoursesNum = {"3", "7", "8A", "8B", "100", "110"};
        final String[] cseCoursesTitle = {"Fluency in Information Technology", "Introduction to Matlab",
                "Intro to Java", "Intro to OOP", "Advanced Data Structures", "Software Engineering"};

        final ArrayList<String> listArr = new ArrayList<String>();
        for (int i = 0; i < subjCode.length; ++i){
            listArr.add(subjCode[i]);
        }
        final ArrayAdapter<String> browseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArr);

        ListView lv = (ListView)findViewById(R.id.browseList);
        lv.setAdapter(browseAdapter);

        lv.setOnItemClickListener(new OnItemClickListener()
        {
            boolean subjCodeIsDisplayed = true;
            boolean courseNumIsDisplayed = false;
            @Override
            public void onItemClick(AdapterView<?> a, View v,int position, long id)
            {
                if (subjCodeIsDisplayed == true){
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
                            listArr.clear();
                            for (int i = 0; i < cseCoursesNum.length; ++i){
                                browseAdapter.add(cseCoursesNum[i]);
                            }
                            browseAdapter.notifyDataSetChanged();
                            break;
                    }
                    subjCodeIsDisplayed = false;
                    courseNumIsDisplayed = true;
                }
            }
        });

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }

  /*  @Override
    public boolean onBackPressed()  {

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_browse, container, false);
            return rootView;
        }
    }*/

}
