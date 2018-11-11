package shubhankar30.opendictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shubhankarranade30 on 05-03-2018.
 * Github link: https://github.com/shubhankar30
 * Email-id: shubhankarranade30@gmail.com
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);

        //Set toolbar details
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setTitle( "Open Dictionary");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wordPresent = false;
                String newEntry = editText.getText().toString();
                wordPresent = mDatabaseHelper.checkIfPresent(newEntry);
                if(editText.length()!= 0 && wordPresent == false){
                    queryData(newEntry);
                    editText.setText("");
                }else if( wordPresent == true){
                    toastMessage("Word is already present in Dictionary");
                } else{
                    toastMessage("You must enter something in the text field");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(intent);
            }
        });
    }

    public void queryData(final String data){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://owlbot.info/api/v2/dictionary/" + data +"?format=json";

        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length()==0){
                            toastMessage("Enter a valid word");
                        }

                        for( int i=0 ; i<response.length();i++){
                            String meaningOfWord = null;
                            String exampleOfWord = null;
                            String typeOfWord = null;
                            try {
                                JSONObject currentJsonObj = response.getJSONObject(i);
                                meaningOfWord = currentJsonObj.getString("definition"); //Get meaning from REST response
                                exampleOfWord = currentJsonObj.getString("example");    //Get example from REST response
                                typeOfWord = currentJsonObj.getString("type");

                                //Log.e("Checking meaning",meaningOfWord); //Debug
                                //Log.e("Rest Response:", response.toString());//Debug

                                //Database add function call
                                addRow(data, meaningOfWord, exampleOfWord, typeOfWord);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        toastMessage("Please enter a valid word");
                    }
                }
        );
        requestQueue.add(jsonarrayRequest);
    }

    //Add Data to Database
    public void addRow(String word, String meaning, String example, String type){
        boolean success = mDatabaseHelper.addRow(word, meaning, example, type);

        /*//Condition to check if row was successfully added to database
        if(success) {
            //Log.e("ROW ADDED", "SUCCESSFULLY"); //Debug
        }else {
            //Log.e("ROW ADDED", "FAILED"); //Debug
        }*/
    }

    //Create Menu items in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.information_topright,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Select Menu Items on toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        createInformationDialog();
        return super.onOptionsItemSelected(item);
    }

    //Create App Info Dialogue
    private void createInformationDialog(){
        ((TextView) new AlertDialog.Builder(this)
                .setTitle("Info")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(Html.fromHtml("This application is mainly designed to be an on-the-go dictionary with simplistic design and an open source mindset with contribution and documentation made by the public." +
                        "<p>Just type in the required word in the provided box and click add word. This will add the word in your Simple Dictionary</p> " +
                        "<p>Click view list to see the meanings of all the words you have saved in your own dictionary. </p><br>" +
                        "<br><br><br><br>" +
                        "<p>Application repo at: <a href=\"http://www.github.com/shubhankar30/SimpleDictionary\">Github Repository link</a><br>" +
                        ""))
                .show()
                // Need to be called after show(), in order to generate hyperlinks
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    //Close database connection
    @Override
    public void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }

    //customizable toast message
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

