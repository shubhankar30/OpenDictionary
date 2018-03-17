package shubhankar30.simpledictionary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shubhankarranade30 on 17-03-2018.
 */

public class EditDataActivity extends AppCompatActivity{

    private static final String TAG = "EditDataActivity";
    private Button btnDelete;
    private TextView word_item;

    DatabaseHelper mDatabaseHelper;

    private String selectedName;
    private int selectedId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        word_item = (TextView) findViewById(R.id.word_item);
        mDatabaseHelper = new DatabaseHelper(this);

        Intent receivedIntent = getIntent();

        //selectedId = receivedIntent.getIntExtra("id", -1); //-1 is default value

        selectedName = receivedIntent.getStringExtra("name");

        word_item.setText(selectedName);
        toastMessage("NAME ADDED:" + selectedName);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseHelper.deleteWord(selectedName);
                //word_item.setText("");
                toastMessage("Word removed from database");
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
