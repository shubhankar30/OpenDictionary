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

    private static final String TABLE_NAME = "table_words_39";
    private static final String COL1 = "ID";
    private static final String COL2 = "word";
    private static final String COL3 = "meaning";
    private static final String COL4 = "example";
    private static final String COL5 = "type";

    public DatabaseHelper(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY  AUTOINCREMENT," +
                COL2 + " TEXT," +
                COL3 + " TEXT," +
                COL4 + " TEXT," +
                COL5 + " TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);//if exists
        onCreate(db);
    }


    public boolean addRow(String word, String meaning, String example, String typeTemp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, word);
        cv.put(COL3, meaning);
        cv.put(COL4, example);
        cv.put(COL5, typeTemp);

        Log.d(TAG, "addRow: Adding:" + word + " to " + COL2 + " and " + meaning + " to " + COL3 + " and " + example + " to " + COL4 + " and " + typeTemp + " to " + COL5); //debug

        long testResult = db.insert(TABLE_NAME, null, cv);

        if(testResult == -1) {
            return false;
        } else{
            return true;
        }
    }

    public boolean checkIfPresent(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = null;
        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + "='" + word + "'";
        data = db.rawQuery(query,null);

        if(data.getCount()>0){
            return true;
        }else{
            return false;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public Cursor getItemId(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("The current db call:", name);

        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL3 + " = '" + name + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteWord(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL2 + " = '" + name + "'";

        Log.d(TAG, "deleting record" + name + "from database");
        db.execSQL(query);
    }

    public Cursor getRowInfo(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL2 + "," + COL4 + "," + COL5 + " FROM " + TABLE_NAME +
                " WHERE " + COL3 + " = '" + name + "'";

        Cursor data = db.rawQuery(query, null);
        return data;
    }



}
