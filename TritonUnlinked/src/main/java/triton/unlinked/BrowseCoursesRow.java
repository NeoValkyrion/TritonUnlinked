package triton.unlinked;

import android.content.ContentValues;

/**
 * BrowseCoursesRow entry struct
 *
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class BrowseCoursesRow extends SQLiteRow {
    public String subject;
    public String number;
    public String course;
    public String title;
    public String course_id;

    public BrowseCoursesRow() {}

    public BrowseCoursesRow(ContentValues vals) {
        super(vals);
        subject = vals.getAsString(BrowseCoursesModel.COL_SUB);
        number = vals.getAsString(BrowseCoursesModel.COL_NUM);
        course = vals.getAsString(BrowseCoursesModel.COL_COURSE);
        title = vals.getAsString(BrowseCoursesModel.COL_TITLE);
        title = vals.getAsString(BrowseCoursesModel.COL_CID);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues vals = super.toContentValues();
        vals.put(BrowseCoursesModel.COL_SUB, subject);
        vals.put(BrowseCoursesModel.COL_NUM, number);
        vals.put(BrowseCoursesModel.COL_COURSE, course);
        vals.put(BrowseCoursesModel.COL_TITLE, title);
        vals.put(BrowseCoursesModel.COL_CID, course_id);
        return vals;
    }
}
