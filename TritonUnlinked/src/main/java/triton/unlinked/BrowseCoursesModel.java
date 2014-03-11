package triton.unlinked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import android.util.Log;

/**
 * DAO for "BrowseCourses" table.
 *
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of BrowseCoursesRow where each column
 * is a publicly accessible property.
 *
 */
public class BrowseCoursesModel extends SQLiteDAO {

    public static final String COL_SUB      = "subject";
    public static final String COL_NUM      = "number";
    public static final String COL_COURSE   = "course";
    public static final String COL_TITLE    = "title";
    public static final String COL_CID      = "course_id";

    /**
     * Init SQLiteDAO with table "browse_course"
     */
    public BrowseCoursesModel(Context ctx)
    {
        super("browse_courses", ctx);
    }

    /**
     * Utility method to grab all the rows from a cursor
     */
    private BrowseCoursesRow[] fetchBrowseCoursesRows(Cursor cr)
    {
        if (cr == null) {
            return null;
        }
        BrowseCoursesRow[] result = new BrowseCoursesRow[cr.getCount()];
        if (result.length == 0) {
            cr.close();
            return result;
        }

        boolean valid = cr.moveToFirst();
        int ii = 0;

        // Grab the cursor's column indices
        // An error here indicates the COL constants aren't synced with the DB
        int ind_id   = cr.getColumnIndexOrThrow(COL_ID);
        int ind_sub  = cr.getColumnIndexOrThrow(COL_SUB);
        int ind_num  = cr.getColumnIndexOrThrow(COL_NUM);
        int ind_course  = cr.getColumnIndexOrThrow(COL_COURSE);
        int ind_title = cr.getColumnIndexOrThrow(COL_TITLE);
        int ind_cid   = cr.getColumnIndexOrThrow(COL_CID);
        int ind_dm   = cr.getColumnIndexOrThrow(COL_MDATE);
        int ind_dc   = cr.getColumnIndexOrThrow(COL_CDATE);

        // Iterate over every row (move the cursor down the set)
        while (valid) {
            result[ii] = new BrowseCoursesRow();
            fetchBaseData(cr, result[ii], ind_id, ind_dm, ind_dc);
            result[ii].subject     = cr.getString(ind_sub);
            result[ii].number      = cr.getString(ind_num);
            result[ii].course      = cr.getString(ind_course);
            result[ii].title       = cr.getString(ind_title);
            result[ii].course_id   = cr.getString(ind_cid);

            valid = cr.moveToNext();
            ii ++;
        }

        cr.close();
        return result;
    }
    private ArrayList<String> fetchBrowseCoursesSubjectRows(Cursor cr){
        if (cr == null)
            return null;
        ArrayList<String> result = new ArrayList<String>();

        boolean valid = cr.moveToFirst();

        // Grab the cursor's column indices
        // An error here indicates the COL constants aren't synced with the DB
        int ind_sub  = cr.getColumnIndexOrThrow(COL_SUB);

        // Iterate over every row (move the cursor down the set)
        while (valid) {
            result.add(cr.getString(ind_sub));

            valid = cr.moveToNext();
        }

        cr.close();
        return result;
    }
    /**
     * Inserts a new entry into the course table
     */
    public long insert(BrowseCoursesRow row)
    {
        return super.insert(row.toContentValues());
    }

    /**
     * Inserts a new entry into the browse_courses table, defaults record to 0
     */
    public long insert(String subject, String number, String title, String course_id)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_SUB, subject);
        cv.put(COL_NUM, number);
        cv.put(COL_COURSE, subject + " " + number);
        cv.put(COL_TITLE, title);
        cv.put(COL_CID, course_id);
        return super.insert(cv);
    }

    /**
     * Retrieve an ArrayList of subject strings
     * CURRENTLY DOES NOT WORK
     */
    /*
    public ArrayList<String> getAllSubjects()
    {
        BrowseCoursesRow[] course_model_data;
        ArrayList<String> subjects;

        String[] col = { COL_SUB };
        Cursor cr = select(col, new String[] {});
        course_model_data = fetchBrowseCoursesRows(cr);

        //Fill ArrayList with subject strings
        subjects = new ArrayList<String>();

        for (int i = 0; i < course_model_data.length; i++) {
            subjects.add(course_model_data[i].subject);
        }

        return subjects;
    }
    */

    /**
     * Fetch all BrowseCourseRows
     */
    public BrowseCoursesRow[] getAllCoursesRows()
    {
        Cursor cr = select(new String[] {}, new String[] {});

        return fetchBrowseCoursesRows(cr);
    }

    /**
     * Fetch an entry via the ID
     */
    public BrowseCoursesRow getByID(long id)
    {
        Cursor cr = selectByID(id);

        if (cr.getCount() > 1) {
            return null;
        }

        BrowseCoursesRow[] rows = fetchBrowseCoursesRows(cr);
        return (rows.length == 0) ? null : rows[0];
    }

    /**
     * Fetch all BrowseCourseRows of a specific subject
     */
    public BrowseCoursesRow[] getAllBySubject(String sub)
    {
        String[] col = { COL_SUB };
        String[] val = { String.valueOf(sub) };
        Cursor cr = select(col, val);
        return fetchBrowseCoursesRows(cr);
    }

    /**
     * Fetch all BrowseCourseSubjects
     */

    public ArrayList<String> getAllSubjects(){
        Cursor c = db.rawQuery("SELECT DISTINCT subject FROM browse_courses", null);
        return fetchBrowseCoursesSubjectRows(c);
    }
}
