package shubhankar30.simpledictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private ImageButton btnHelp;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setTitle( "Simple Dictionary");

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
                            String exampleofWord = null;
                            String typeofWord = null;
                            try {
                                JSONObject currentJsonObj = response.getJSONObject(i);
                                meaningOfWord = currentJsonObj.getString("definition"); //Get meaning from REST response
                                exampleofWord = currentJsonObj.getString("example");    //Get example from REST response
                                typeofWord = currentJsonObj.getString("type");

                                /*//String Cleaning (Remove symbols and characters)
                                if(meaningOfWord.contains("'")){
                                    meaningOfWord = meaningOfWord.replaceAll("'", "''");
                                }
                                if(exampleofWord.contains("'")){
                                    exampleofWord = exampleofWord.replaceAll("'", "''");
                                }
                                if(typeofWord.contains("'")){
                                    typeofWord = typeofWord.replaceAll("'", "''");
                                }*/


                                Log.e("Checking meaning",meaningOfWord);
                                Log.e("Rest Response:", response.toString());


                                    addRow(data, meaningOfWord, exampleofWord, typeofWord); //function call


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response:",error.toString());
                        toastMessage("Please enter a valid word");
                    }
                }
        );
        requestQueue.add(jsonarrayRequest);
    }

    public void addRow(String word, String meaning, String example, String type){ //Add Data to database
        boolean success = mDatabaseHelper.addRow(word, meaning, example, type);

        if(success) {
            Log.e("ROW ADDED", "SUCCESSFULLY"); //debug
        }else {
            Log.e("ROW ADDED", "FAILED"); //debug
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.information_topright,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        createInformationDialog();
        return super.onOptionsItemSelected(item);
    }

    private void createInformationDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //String link1 = Html.fromHtml("<a href='http://www.github.com/shubhankar30/SimpleDictionary'>Github Repository Link</a>");

        ((TextView) new AlertDialog.Builder(this)
                .setTitle("Info")
                .setIcon(android.R.drawable.ic_menu_info_details)
                .setMessage(Html.fromHtml("This application is mainly designed to be an on-the-go dictionary with simplistic design and an open source documentation." +
                        "<p>Just type in the required word in the provided box and click add word. This will add the word in your Simple Dictionary</p> " +
                        "<p>Click view list to see the meanings of all the words you have saved in your own dictionary. </p><br>" +
                        "<br><br><br><br>" +
                        "<p>Application repo at: <a href=\"http://www.github.com/shubhankar30/SimpleDictionary\">Github Repository link</a><br>" +
                        "Dictionary API used: <a href=\"https://owlbot.info/\">Owlbot Info</a></p>" +
                        ""))
                .show()
                // Need to be called after show(), in order to generate hyperlinks
                .findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public void onDestroy() {
        mDatabaseHelper.close();
        super.onDestroy();
    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

