package triton.unlinked;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by NeoValkryion on 2/19/14.
 */
public class DatabaseSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_COURSES = "courses";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_COURSE = "course";

    private static final String DATABASE_NAME = "triton.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create_table"
            + TABLE_COURSES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_COURSE
            + " text not null);";

    public DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        Log.w(DatabaseSQLiteHelper.class.getName(),
                "Upgrading database from version" + oldversion + " to "
                    + newversion + ", which will destroy all the old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        onCreate(db);
    }
}
