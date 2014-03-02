package triton.unlinked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * DAO for "Course" table.
 *
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of CourseRow where each column
 * is a publicly accessible property.
 *
 */
public class CourseModel extends SQLiteDAO {

    //Table-specific Columns
    public static final String COL_NUM = "number";
    public static final String COL_SUB = "subject";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";

    /**
     * Init SQLiteDAO with table "course"
     */
    public CourseModel(Context ctx)
    {
        super("course", ctx);
    }

    /**
     * Utility method to grab all the rows from a cursor
     */
    private CourseRow[] fetchCourseRows(Cursor cr)
    {
        if (cr == null) {
            return null;
        }
        CourseRow[] result = new CourseRow[cr.getCount()];
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
        int ind_title = cr.getColumnIndexOrThrow(COL_TITLE);
        int ind_desc = cr.getColumnIndexOrThrow(COL_DESC);
        int ind_dm   = cr.getColumnIndexOrThrow(COL_MDATE);
        int ind_dc   = cr.getColumnIndexOrThrow(COL_CDATE);

        // Iterate over every row (move the cursor down the set)
        while (valid) {
            result[ii] = new CourseRow();
            fetchBaseData(cr, result[ii], ind_id, ind_dm, ind_dc);
            result[ii].subject     = cr.getString(ind_sub);
            result[ii].number      = cr.getString(ind_num);
            result[ii].title        = cr.getString(ind_title);
            result[ii].description = cr.getString(ind_desc);

            valid = cr.moveToNext();
            ii ++;
        }

        cr.close();
        return result;
    }

    /**
     * Inserts a new entry into the course table
     */
    public long insert(CourseRow row)
    {
        return super.insert(row.toContentValues());
    }

    /**
     * Inserts a new entry into the course table, defaults record to 0
     */
    public long insert(String number, String title, String description)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_NUM, number);
        cv.put(COL_TITLE, title);
        cv.put(COL_DESC, description);
        return super.insert(cv);
    }

    /**
     * Fetch an entry via the ID
     */
    public CourseRow getByID(long id)
    {
        Cursor cr = selectByID(id);

        if (cr.getCount() > 1) {
            return null;
        }

        CourseRow[] rows = fetchCourseRows(cr);
        return (rows.length == 0) ? null : rows[0];
    }

    public CourseRow[] getAllBySubject(String sub)
    {
        String[] col = { COL_SUB };
        String[] val = { String.valueOf(sub) };
        Cursor cr = select(col, val);
        return fetchCourseRows(cr);
    }
}
