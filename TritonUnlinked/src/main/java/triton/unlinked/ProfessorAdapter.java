package triton.unlinked;

        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ArrayAdapter;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

/**
 * Created by Grim on 3/15/14.
 */
public class ProfessorAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<ProfessorObject> courseList;
    private TextView codeView;
    private TextView titleView;
    private TextView sectionView;
    private TextView professorNameView;


    public ProfessorAdapter(Context context, ArrayList<ProfessorObject> courseList ) {
        this.context = context;
        this.courseList = courseList;
    }
    @Override
    public int getCount() {
      return this.courseList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.fragment_professor_profile, parent, false);

        titleView = (TextView) rowView.findViewById(R.id.prof_title_section);
        codeView = (TextView) rowView.findViewById(R.id.prof_title_section);
        sectionView = (TextView) rowView.findViewById(R.id.prof_title_section);

        titleView.setText(this.courseList.get(position).getTitle() + "\n");
        codeView.setText(this.courseList.get(position).getCode()+ "\n");
        sectionView.setText(this.courseList.get(position).getCode()+ "\n");

        return rowView;
    }


}
