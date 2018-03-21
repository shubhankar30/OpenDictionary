package shubhankar30.simpledictionary;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
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

    private void createInformationDialog(){
        ((TextView) new AlertDialog.Builder(this)
                .setTitle("Info")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setMessage(Html.fromHtml("" +
                        "<p>Scroll through your list to see any word you have added to your dictionary</p> " +
                        "<p>You can click on any word to have the option to delete it and see its example with the related meaning. </p><br>" +
                        "<br><br><br><br>" +
                        "<p>Application repo at: <a href=\"http://www.github.com/shubhankar30/SimpleDictionary\">Github Repository link</a><br>" +
                        "Dictionary API used: <a href=\"https://owlbot.info/\">Owlbot Info</a></p>" +
                        ""))
                .show()
                // Need to be called after show(), in order to generate hyperlinks
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
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
