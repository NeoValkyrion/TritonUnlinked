package triton.unlinked;

import android.content.ContentValues;

/**
 * BrowseRoomsRow entry struct
 *
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class BrowseRoomsRow extends SQLiteRow {
    public String bld;
    public String room;
    public String room_id;

    public BrowseRoomsRow() {}

    public BrowseRoomsRow(ContentValues vals) {
        super(vals);
        bld = vals.getAsString(BrowseRoomsModel.COL_BLD);
        room = vals.getAsString(BrowseRoomsModel.COL_ROOM);
        room_id = vals.getAsString(BrowseRoomsModel.COL_RID);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues vals = super.toContentValues();
        vals.put(BrowseRoomsModel.COL_BLD, bld);
        vals.put(BrowseRoomsModel.COL_ROOM, room);
        vals.put(BrowseRoomsModel.COL_RID, room_id);
        return vals;
    }
}