package triton.unlinked;

import android.content.ContentValues;

/**
 * BrowseCoursesRow entry struct
 *
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class BrowseProfessorsRow extends SQLiteRow {
    public String lname;
    public String fname;

    public BrowseProfessorsRow() {}

    public BrowseProfessorsRow(ContentValues vals) {
        super(vals);
        lname = vals.getAsString(BrowseProfessorsModel.COL_LNAME);
        fname = vals.getAsString(BrowseProfessorsModel.COL_FNAME);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues vals = super.toContentValues();
        vals.put(BrowseProfessorsModel.COL_LNAME, lname);
        vals.put(BrowseProfessorsModel.COL_FNAME, fname);
        return vals;
    }
}