package shubhankar30.simpledictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private boolean isFirstRun = true;
    private SharedPreferences prefs;


    DatabaseHelper mDatabaseHelper;

    private ListView mListView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);

        //Add back button to toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        //Create AlertDialog only once
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = prefs.getBoolean("isFirstRun", true);
        if(isFirstRun){
            createInformationDialog();
        }
        isFirstRun = false;
        prefs.edit().putBoolean("isFirstRun", isFirstRun).commit();

        populateListView();
    }

    @Override
    protected void onResume() { //To refresh the ListDataActivity page when user backtracks to Activity
        super.onResume();
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        populateListView();
        //toastMessage("Activity refreshed"); Debug
    }


    //Show information icon on top right of toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.information_topright,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Choose between back button and information button on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
            createInformationDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    //Create information dialog for page
    private void createInformationDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListDataActivity.this);
        alertDialog.setTitle("This is your Dictionary");
        String alert1= "Type in any word and press add word to add it to your local dictionary.";
        String alert2 = "Click view dictionary to see your local dictionary";
        String alert3 = "You can select the individual words in your dictionary to see their respective examples. You can even delete any word you do not need anymore.";
        alertDialog.setMessage(alert1 +"\n\n"+ alert2 +"\n\n"+ alert3);
        alertDialog.setPositiveButton("OK",null);
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    //Fill the ListView with items
    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView");

        wordList = new ArrayList<>();
        Cursor data = mDatabaseHelper.getData();
        int numRows = data.getCount();
        if (numRows == 0) {
            toastMessage("Nothing in database");
        } else {
            while (data.moveToNext()) { //Move to next Row
                words = new WordList(data.getString(1), data.getString(2), data.getString(4)); //COLUMN 1 contains words, COLUMN 2 contains meanings
                wordList.add(words);
            }
            CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_list_adapter, wordList);
            mListView.setAdapter(adapter);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String meaning = ((TextView) view.findViewById(R.id.meaningId)).getText().toString();

               // toastMessage("Pressed " + meaning);

                //Single Quotes Error
                if(meaning.contains("'")){
                    meaning = meaning.replaceAll("'", "''");
                }

                Log.d(TAG, "onItemClick: You clicked" + meaning);
                //toastMessage("Pressed " + meaning);

                Cursor data = mDatabaseHelper.getItemId(meaning);
                int itemId = -1;
                while(data.moveToNext()){
                    itemId = data.getInt(0);
                }
                if(itemId > -1){
                    Log.d(TAG, "onItemClick: ID is : " + itemId);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);

                    editScreenIntent.putExtra("meaning", meaning); //To send meaning of row to EditDataActivity
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with " + meaning);
                }
            }
        });
    }

    //Customizable toast
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
