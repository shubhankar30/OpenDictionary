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
    ArrayList<WordList> wordList;
    WordList words;

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

        wordList = new ArrayList<>();
        Cursor data = mDatabaseHelper.getData();
        int numRows = data.getCount();
        if (numRows == 0) {
            toastMessage("Nothing in database");
        } else {
            while (data.moveToNext()) {
                words = new WordList(data.getString(1), data.getString(2));
                wordList.add(words); //COLUMN 1 contains words
            }
            CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_list_adapter, wordList);

            //ListAdapter adapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, listWord);
            mListView.setAdapter(adapter);
        }
    }
        //customizable toast

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
