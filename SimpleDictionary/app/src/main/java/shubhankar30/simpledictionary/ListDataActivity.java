package shubhankar30.simpledictionary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        populateListView();
        toastMessage("Activity refreshed");
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
                words = new WordList(data.getString(1), data.getString(2)); //COLUMN 1 contains words, COLUMN 2 contains meanings
                wordList.add(words);
            }
            CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_list_adapter, wordList);
            mListView.setAdapter(adapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // String name = adapterView.getItemAtPosition(i).toString();

                String name = ((TextView) view.findViewById(R.id.wordId)).getText().toString();

                Log.d(TAG, "onItemClick: You clicked" + name);
                //toastMessage("Pressed " + debug_1);

                Cursor data = mDatabaseHelper.getItemId(name);
                int itemId = -1;
                while(data.moveToNext()){
                    itemId = data.getInt(0);
                }
                if(itemId > -1){
                    Log.d(TAG, "onItemClick: ID is : " + itemId);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);

                   // startActivityForResult(editScreenIntent, 1);

                    editScreenIntent.putExtra("name", name);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with " + name);
                }
            }
        });
    }

    //Customizable toast
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
