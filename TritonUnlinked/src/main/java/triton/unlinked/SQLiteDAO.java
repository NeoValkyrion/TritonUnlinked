package triton.unlinked;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Abstract Base DAO for other Models to extend.
 */
public abstract class SQLiteDAO
{
    // Database Properties
    private static final String DATABASE_NAME = "triton.db";
    private static final int DATABASE_VERSION = 1;
    protected final String DB_TABLE;

    //Global Column Names
    public static final String COL_ID    = "_id";
    public static final String COL_NAME  = "name";
    public static final String COL_CDATE = "date_created";
    public static final String COL_MDATE = "date_modified";

    /**
     * DB connection object, subclassed for the specific DB params we need
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        private Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            Log.v("DB", "DB onCreate BEGIN");

            // Fetch the creation scripts
            InputStream sqlfile = context.getResources().openRawResource(
                    R.raw.db_create);
            InputStream insfile = context.getResources().openRawResource(
                    R.raw.db_create_inserts);

            byte[] reader;
            String sqltext;
            String[] statements;

            try {
                // Read in the creation script
                reader = new byte[sqlfile.available()];
                while (sqlfile.read(reader) != -1){}
                sqltext = new String(reader);
                statements = sqltext.split("--###--");

                // Create all tables
                Log.v("DB", "Creating database...");
                for (int ii=0; ii<statements.length; ii++) {
                    db.execSQL(statements[ii]);
                }
                Log.v("DB", "Done creating database");

                // Read in script with the data for prepopulation
                reader = new byte[insfile.available()];
                while (insfile.read(reader) != -1) {}
                sqltext = new String(reader);
                statements = sqltext.split("--###--");

                // Prepopulate the db
                Log.v("DB", "Inserting data...");
                for (int ii=0; ii<statements.length; ii++) {
                    db.execSQL(statements[ii]);
                }
                Log.v("DB", "Done inserting data");
            } catch (SQLException e) {
                Log.e("DB", "Error occurred during creation");
            } catch (IOException e) {
                Log.e("DB", "Error reading DB creation files");
            }

            Log.v("DB", "DB onCreate END");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int over, int nver)
        {
            InputStream sqlfile = context.getResources().openRawResource(
                    R.raw.db_delete);
            byte[] reader;
            String sqltext;
            String[] statements;

            try {
                // Read in the deletion script
                reader = new byte[sqlfile.available()];
                while (sqlfile.read(reader) != -1){}
                sqltext = new String(reader);
                statements = sqltext.split("--###--");

                // Delete the db
                Log.v("DB", "Deleting data...");
                for (int ii=0; ii<statements.length; ii++) {
                    db.execSQL(statements[ii]);
                }
            } catch (SQLException e) {
                Log.e("DB", "Error occurred during creation");
            } catch (IOException e) {
                Log.e("DB", "Error reading DB creation files");
            }

            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db)
        {
            super.onOpen(db);
            if (!db.isReadOnly()) {
                // This enables foreign keys (Android 2.2+ only)
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }

    private DatabaseHelper DBHelper;
    protected static SQLiteDatabase db;

    public SQLiteDAO(String table, Context ctx) {
        DB_TABLE = table;
        DBHelper = new DatabaseHelper(ctx);
    }

    private String getWhereClause(String[] cols)
    {
        String sql = "";
        if (cols == null)
            return sql;

        // Build the WHERE clause (append each col-val)
        if (cols.length > 0) {
            sql += " WHERE ";
        }
        for (int ii = 0; ii < cols.length; ii++) {
            if (ii != 0)
                sql += ", ";
            sql += cols[ii] + " = ?";
        }
        return sql;
    }

    protected long insert(ContentValues cv)
    {
        Long time = Calendar.getInstance().getTimeInMillis();

        // Automated input for global columns
        cv.put(COL_ID, (Integer)null); // Always let this autoincrement
        cv.put(COL_MDATE, time);
        cv.put(COL_CDATE, time);

        return db.insert(DB_TABLE, null, cv);
    }

    protected int update(ContentValues cv, String where)
    {
        if (where == null)
            return -1;

        cv.remove(COL_ID);
        cv.remove(COL_CDATE);
        cv.remove(COL_MDATE);
        cv.put(COL_MDATE, Calendar.getInstance().getTimeInMillis());

        return db.update(DB_TABLE, cv, where, null);
    }


    protected int delete(String where)
    {
        if (where == null)
            return -1;

        return db.delete(DB_TABLE, where, null);
    }

    protected Cursor select(String[] cols, String[] vals)
            throws SQLException
    {
        return select(cols, vals, null, -1);
    }

    protected Cursor select(String[] cols, String[] vals, String order, int limit)
            throws SQLException
    {
        String sql = "SELECT * FROM " + DB_TABLE;

        sql += getWhereClause(cols);
        if (order != null) sql += " ORDER BY " + order;
        if (limit > 0) sql += " LIMIT " + limit;

        return db.rawQuery(sql, vals);
    }

    protected int selectCount(String[] cols, String[] vals) throws SQLException
    {
        String sql = "SELECT COUNT(*) as count FROM " + DB_TABLE;
        sql += getWhereClause(cols);
        Cursor cr = db.rawQuery(sql, vals);

        if (cr == null) {
            return -1;
        }
        if (!cr.moveToFirst()) {
            cr.close();
            return -1;
        }

        int ind = cr.getColumnIndexOrThrow("count");
        int result = cr.getInt(ind);
        cr.close();
        return result;
    }

    protected Cursor selectByID(long id) throws SQLException
    {
        return db.rawQuery(
                "SELECT * FROM " + DB_TABLE + " WHERE " + COL_ID + " = " + id,
                null);
    }

    protected String selectNameByID(String table, long id) throws SQLException
    {
        Cursor cr = db.rawQuery(
                "SELECT * FROM " + table + " WHERE " + COL_ID + "=?",
                new String[] { String.valueOf(id) });
        if (cr == null) {
            return null;
        }
        if (cr.getCount() < 1) {
            cr.close();
            return null;
        }

        int col = cr.getColumnIndexOrThrow(COL_NAME);
        cr.moveToFirst();
        String result = cr.getString(col);
        cr.close();
        return result;
    }

    protected long selectIDByName(String table, String name) throws SQLException
    {
        Cursor cr = db.rawQuery(
                "SELECT * FROM " + table + " WHERE " + COL_NAME + "=?",
                new String[] { name });
        if (cr == null) {
            return -1;
        }
        if (cr.getCount() < 1) {
            cr.close();
            return -1;
        }

        int col = cr.getColumnIndexOrThrow(COL_ID);
        cr.moveToFirst();
        long out = cr.getLong(col);
        cr.close();
        return out;
    }

    protected void fetchBaseData(Cursor cr, SQLiteRow row,
                                 int ind_id, int ind_dm, int ind_dc)
    {
        row._id = cr.getLong(ind_id);
        // Gets as milliseconds
        row.date_modified = cr.getLong(ind_dm);
        row.date_created = cr.getLong(ind_dc);
    }

    /**
     * This must be called prior to any DB access methods to open a connection
     */
    public void open() throws SQLException
    {
        if (db == null) {
            db = DBHelper.getWritableDatabase();
        }
    }

    /**
     * Closes the DB connection if it is open
     *
     * This should be called after all DB transactions are completed. Not
     * calling this can cause problems with hanging cursors if mulitple
     * threads are used.
     */
    public void close()
    {
        db.close();
        DBHelper.close();
        db = null;
    }

}

