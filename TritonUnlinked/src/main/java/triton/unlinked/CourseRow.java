package triton.unlinked;

import android.content.ContentValues;

/**
 * CourseModel entry struct
 *
 * This is a customized data container to hold an entry from the
 * table. Every DAO Model will have its own Row class definition.
 */
public class CourseRow extends SQLiteRow {

    public String subject;
    public String number;
    public String title;
    public String description;

    public CourseRow() {}

    public CourseRow(ContentValues vals) {
        super(vals);
        subject = vals.getAsString(CourseModel.COL_SUB);
        number = vals.getAsString(CourseModel.COL_NUM);
        description = vals.getAsString(CourseModel.COL_TITLE);
        description = vals.getAsString(CourseModel.COL_DESC);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues vals = super.toContentValues();
        vals.put(CourseModel.COL_SUB, subject);
        vals.put(CourseModel.COL_NUM, number);
        vals.put(CourseModel.COL_TITLE, title);
        vals.put(CourseModel.COL_DESC, description);
        return vals;
    }

}
