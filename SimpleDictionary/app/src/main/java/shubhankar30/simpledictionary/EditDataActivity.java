package shubhankar30.simpledictionary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by shubhankarranade30 on 17-03-2018.
 * Github link: https://github.com/shubhankar30
 * Email-id: shubhankarranade30@gmail.com
 */

public class EditDataActivity extends AppCompatActivity{

    private static final String TAG = "EditDataActivity";
    private Button btnDelete;
    private Button btnBack;
    private TextView word_item;
    private TextView meaning_item;
    private TextView example_item;
    private TextView type_item;
    private String selectedMeaning;
    private String selectedWord;
    private String selectedExample;
    private String selectedType;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnBack = (Button) findViewById(R.id.btnBackEditDataActivity);
        word_item = (TextView) findViewById(R.id.word_item);
        meaning_item = (TextView) findViewById(R.id.meaning_item);
        example_item = (TextView) findViewById(R.id.example_item);
        type_item = (TextView) findViewById(R.id.type_item);

        mDatabaseHelper = new DatabaseHelper(this);

        //Add back button to toolbar
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent receivedIntent = getIntent();

        //selectedId = receivedIntent.getIntExtra("id", -1); //-1 is default value
        selectedMeaning = receivedIntent.getStringExtra("meaning");

        //Database calls to get example and word from meaning
        Cursor data = mDatabaseHelper.getRowInfo(selectedMeaning);
        if (data.getCount() >= 1) {
            while (data.moveToNext()) {
                selectedWord = data.getString(0);
                selectedExample = data.getString(1);
                selectedType = data.getString(2);
            }
        }

        word_item.setText(selectedWord);
            meaning_item.setText(Html.fromHtml(selectedMeaning));

            if(selectedType.equals("null")){
                type_item.setText("(Type not available)");
            } else {
                type_item.setText("(" + selectedType + ")");
            }

            if(selectedExample.equals("null")) { //If there is no example for specific word
                example_item.setText("Example not available for this context");
            }else{
                example_item.setText(Html.fromHtml(selectedExample));
            }

           // toastMessage("NAME ADDED:" + selectedMeaning); Debug

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabaseHelper.deleteWord(selectedWord);
                    toastMessage("Word deleted");
                    finish();
                }
            });

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

    //To end activity when back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.information_topright,menu);
        return super.onCreateOptionsMenu(menu);
    }

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
                "<p>You can see the example for the respective word for the context meaning that you have selected.</p> " +
                "<p>To delete the word from your dictionary, press the delete word button given below. </p>" +
                "<p>To just delete one single meaning from your dictionary, press the delete meaning button below.<br>" +
                "<br><br><br><br>" +
                "<p>Application repo at: <a href=\"http://www.github.com/shubhankar30/SimpleDictionary\">Github Repository link</a><br>" +
                ""))
        .show()
        // Need to be called after show(), in order to generate hyperlinks
        .findViewById(android.R.id.message))
        .setMovementMethod(LinkMovementMethod.getInstance());
    }


    //Customizable toast
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
