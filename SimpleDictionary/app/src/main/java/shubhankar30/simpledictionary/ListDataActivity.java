package shubhankar30.simpledictionary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shubhankarranade30 on 05-03-2018.
 */

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView");

        //get Data and append to a List
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listWord = new ArrayList<>();
        //ArrayList<String> listMeaning = new ArrayList<>();
        while (data.moveToNext()) {
            //get value from database in column 1
            //then add it to arraylist
            listWord.add(data.getString(1)); //COLUMN 1 contains words
            // listMeaning.add(data.getString(2));
        }
    }

        //customizable toast

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
