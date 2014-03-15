package triton.unlinked;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.ArrayList;

public class BrowseActivity extends FragmentActivity implements ActionBar.TabListener{

    private static final String CURRENT_NAV_ITEM = "current_selected_nav_item";
    private short whichPage = 1;
    private BrowseCoursesFragment browseCoursesFrag;
    private BrowseProfessorsFragment browseProfFrag;
    private BrowseRoomsFragment browseRoomsFrag;

    @Override
    public void onStart(){
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        // The rest of your onStop() code.
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setCustomView(R.layout.action_bar);
        //actionBar.setIcon(android.R.color.transparent);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab coursesTab = actionBar.newTab().setText("Courses").setTabListener(this);
        ActionBar.Tab profsTab = actionBar.newTab().setText("Professors").setTabListener(this);
        ActionBar.Tab roomsTab = actionBar.newTab().setText("Rooms").setTabListener(this);
        actionBar.addTab(coursesTab);
        actionBar.addTab(profsTab);
        actionBar.addTab(roomsTab);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String whichTab = extras.getString("SelectedCategory");
            if (whichTab.equalsIgnoreCase("Courses"))
                actionBar.selectTab(coursesTab);
            else if (whichTab.equalsIgnoreCase("Professors"))
                actionBar.selectTab(profsTab);
            else if (whichTab.equalsIgnoreCase("Rooms"))
                actionBar.selectTab(roomsTab);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(CURRENT_NAV_ITEM)) {
            getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(CURRENT_NAV_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_NAV_ITEM, getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        if (tab.getPosition() == 0) {
            browseCoursesFrag = new BrowseCoursesFragment();
            getFragmentManager().beginTransaction().replace(R.id.activityBrowseContainer, browseCoursesFrag).commit();
            this.whichPage = 1;
        }
        else if (tab.getPosition() == 1) {
            browseProfFrag = new BrowseProfessorsFragment();
            getFragmentManager().beginTransaction().replace(R.id.activityBrowseContainer, browseProfFrag).commit();
            this.whichPage = 2;
        }
        else{
            browseRoomsFrag = new BrowseRoomsFragment();
            getFragmentManager().beginTransaction().replace(R.id.activityBrowseContainer, browseRoomsFrag).commit();
            this.whichPage = 3;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }
    @Override
    public void onBackPressed(){
        if(this.whichPage == 1){
            browseCoursesFrag.onBackPressed();
        }
        else if (this.whichPage == 2){
            browseProfFrag.onBackPressed();
        }
        else if(this.whichPage == 3){
            browseRoomsFrag.onBackPressed();
        }
    }
}
