package triton.unlinked;

import android.content.ContentValues;

/**
 * Base class for the child DAOs' Row to extend.
 *
 * This contains all of the global columns and implements them for each
 * method
 */
public class SQLiteRow {
    public long _id;
    public String name;
    public long date_modified;
    public long date_created;

    public SQLiteRow() {}
    public SQLiteRow(ContentValues vals)
    {
        _id = vals.getAsLong(SQLiteDAO.COL_ID);
        name = vals.getAsString(SQLiteDAO.COL_NAME);
        date_modified = vals.getAsLong(SQLiteDAO.COL_MDATE);
        date_created = vals.getAsLong(SQLiteDAO.COL_CDATE);
    }

    /**
     * Utility method for converting from this to android's expected format
     * when working with SQLiteDatabase objects
     */
    public ContentValues toContentValues()
    {
        ContentValues vals = new ContentValues();
        vals.put(SQLiteDAO.COL_ID, _id);
        vals.put(SQLiteDAO.COL_NAME, name);
        vals.put(SQLiteDAO.COL_MDATE, date_modified);
        vals.put(SQLiteDAO.COL_CDATE, date_created);
        return vals;
    }
}
