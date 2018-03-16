package shubhankar30.simpledictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shubhankarranade30 on 05-03-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "table_words_10";
    private static final String COL1 = "ID";
    private static final String COL2 = "word";
    private static final String COL3 = "meaning";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY  AUTOINCREMENT," +
                COL2 + " TEXT," +
                COL3 + " TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);//if exists
        onCreate(db);
    }


    public boolean addRow(String word, String meaning){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, word);
        cv.put(COL3, meaning);

        Log.d(TAG, "addRow: Adding:" + word + " to " + COL2 + " and " + meaning + " to " + COL3); //debug

        long testResult = db.insert(TABLE_NAME, null, cv);

        if(testResult == -1) {
            return false;
        } else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
