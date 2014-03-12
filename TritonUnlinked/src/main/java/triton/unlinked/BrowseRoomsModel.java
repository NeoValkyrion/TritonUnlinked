package triton.unlinked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import android.util.Log;

/**
 * DAO for "BrowseRooms" table.
 *
 * Create a new instance and use the methods to interact with the database.
 * Data is returned as instances of BrowseRooms where each column
 * is a publicly accessible property.
 *
 */
public class BrowseRoomsModel extends SQLiteDAO {

    public static final String COL_BLD      = "bld";
    public static final String COL_ROOM     = "room";
    /**
     * Init SQLiteDAO with table "browse_professors"
     */
    public BrowseRoomsModel(Context ctx)
    {
        super("browse_rooms", ctx);
    }

    /**
     * Utility method to grab all the rows from a cursor
     */
    private BrowseRoomsRow[] fetchBrowseRoomsRows(Cursor cr)
    {
        if (cr == null) {
            return null;
        }
        BrowseRoomsRow[] result = new BrowseRoomsRow[cr.getCount()];
        if (result.length == 0) {
            cr.close();
            return result;
        }

        boolean valid = cr.moveToFirst();
        int ii = 0;

        // Grab the cursor's column indices
        // An error here indicates the COL constants aren't synced with the DB
        int ind_id   = cr.getColumnIndexOrThrow(COL_ID);
        int ind_bld  = cr.getColumnIndexOrThrow(COL_BLD);
        int ind_room = cr.getColumnIndexOrThrow(COL_ROOM);
        int ind_dm   = cr.getColumnIndexOrThrow(COL_MDATE);
        int ind_dc   = cr.getColumnIndexOrThrow(COL_CDATE);

        // Iterate over every row (move the cursor down the set)
        while (valid) {
            result[ii] = new BrowseRoomsRow();
            fetchBaseData(cr, result[ii], ind_id, ind_dm, ind_dc);
            result[ii].bld    = cr.getString(ind_bld);
            result[ii].room      = cr.getString(ind_room);

            valid = cr.moveToNext();
            ii ++;
        }

        cr.close();
        return result;
    }

    /**
     * Inserts a new entry into the room table
     */
    public long insert(BrowseRoomsRow row)
    {
        return super.insert(row.toContentValues());
    }

    /**
     * Inserts a new entry into the browse_rooms table, defaults record to 0
     */
    public long insert(String bld, String room)
    {
        ContentValues cv = new ContentValues();
        cv.put(COL_BLD, bld);
        cv.put(COL_ROOM, room);
        return super.insert(cv);
    }

    /**
     * Fetch all BrowseRoomRows
     */
    public BrowseRoomsRow[] getAllRoomsRows()
    {
        Cursor cr = select(new String[] {}, new String[] {});

        return fetchBrowseRoomsRows(cr);
    }

    /**
     * Fetch an entry via the ID
     */
    public BrowseRoomsRow getByID(long id)
    {
        Cursor cr = selectByID(id);

        if (cr.getCount() > 1) {
            return null;
        }

        BrowseRoomsRow[] rows = fetchBrowseRoomsRows(cr);
        return (rows.length == 0) ? null : rows[0];
    }

}
