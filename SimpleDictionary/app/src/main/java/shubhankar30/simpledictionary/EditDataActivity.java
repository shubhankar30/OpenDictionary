package shubhankar30.simpledictionary;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by shubhankarranade30 on 17-03-2018.
 */

public class EditDataActivity extends AppCompatActivity{

    private static final String TAG = "EditDataActivity";
    private Button btnDelete;
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
        word_item = (TextView) findViewById(R.id.word_item);
        meaning_item = (TextView) findViewById(R.id.meaning_item);
        example_item = (TextView) findViewById(R.id.example_item);
        type_item = (TextView) findViewById(R.id.type_item);

        mDatabaseHelper = new DatabaseHelper(this);

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
            meaning_item.setText(selectedMeaning);

            if(selectedType.equals("null")){
                type_item.setText("(Type not available)");
            } else {
                type_item.setText("(" + selectedType + ")");
            }

            //toastMessage("Example is:"+selectedExample); debug
            if(selectedExample.equals("null")) { //If there is no example for specific word
                example_item.setText("Example not available for this context");
            }else{
                example_item.setText(selectedExample);
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
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //customizable toast
    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
