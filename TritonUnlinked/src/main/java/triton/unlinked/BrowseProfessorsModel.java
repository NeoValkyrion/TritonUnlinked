package triton.unlinked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import android.util.Log;

/**
 * DAO for "BrowseProfessors" table.
 *
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of BrowseCoursesRow where each column
 * is a publicly accessible property.
 *
 */
public class BrowseProfessorsModel extends SQLiteDAO {

    public static final String COL_FNAME     = "fname";
    public static final String COL_LNAME     = "lname";
    /**
     * Init SQLiteDAO with table "browse_professors"
     */
    public BrowseProfessorsModel(Context ctx)
    {
        super("browse_professors", ctx);
    }

    /**
     * Utility method to grab all the rows from a cursor
     */
    private BrowseProfessorsRow[] fetchBrowseProfessorsRows(Cursor cr)
    {
        if (cr == null) {
            return null;
        }
        BrowseProfessorsRow[] result = new BrowseProfessorsRow[cr.getCount()];
        if (result.length == 0) {
            cr.close();
            return result;
        }

        boolean valid = cr.moveToFirst();
        int ii = 0;

        // Grab the cursor's column indices
        // An error here indicates the COL constants aren't synced with the DB
        int ind_id   = cr.getColumnIndexOrThrow(COL_ID);
        int ind_fname  = cr.getColumnIndexOrThrow(COL_FNAME);
        int ind_lname = cr.getColumnIndexOrThrow(COL_LNAME);
        int ind_dm   = cr.getColumnIndexOrThrow(COL_MDATE);
        int ind_dc   = cr.getColumnIndexOrThrow(COL_CDATE);

        // Iterate over every row (move the cursor down the set)
        while (valid) {
            result[ii] = new BrowseProfessorsRow();
            fetchBaseData(cr, result[ii], ind_id, ind_dm, ind_dc);
            result[ii].fname    = cr.getString(ind_fname);
            result[ii].lname      = cr.getString(ind_lname);

            valid = cr.moveToNext();
            ii ++;
        }

        cr.close();
        return result;
    }

    /**
     * Inserts a new entry into the course table
     */
    public long insert(BrowseProfessorsRow row)
    {
        return super.insert(row.toContentValues());
    }

    /**
     * Inserts a new entry into the browse_courses table, defaults record to 0
     */
    public long insert(String fname, String lname)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_FNAME, fname);
        cv.put(COL_LNAME, lname);
        return super.insert(cv);
    }

    /**
     * Fetch all BrowseCourseRows
     */
    public BrowseProfessorsRow[] getAllProfessorsRows()
    {
        Cursor cr = select(new String[] {}, new String[] {});

        return fetchBrowseProfessorsRows(cr);
    }

    /**
     * Fetch an entry via the ID
     */
    public BrowseProfessorsRow getByID(long id)
    {
        Cursor cr = selectByID(id);

        if (cr.getCount() > 1) {
            return null;
        }

        BrowseProfessorsRow[] rows = fetchBrowseProfessorsRows(cr);
        return (rows.length == 0) ? null : rows[0];
    }

}
