package shubhankar30.simpledictionary;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);
        mDatabaseHelper = new DatabaseHelper(this);






        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CODE FOR DATABASE ENTRY
                String newEntry = editText.getText().toString();
                if(editText.length()!= 0){
                    addData(newEntry);
                    editText.setText("");
                }else {
                    toastMessage("You must put something in the text field");
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

    public void queryData(String data){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://owlbot.info/api/v2/dictionary/" + data +"?format=json";

        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for( int i=0 ; i<response.length();i++){
                            String meaningOfWord = null;
                            try {
                                JSONObject currentJsonObj = response.getJSONObject(i);
                                meaningOfWord = currentJsonObj.getString("definition");
                                addMeaning(meaningOfWord);
                                Log.e("Checking meaning",meaningOfWord);
                                Log.e("Rest Response:", response.toString());
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

                    }
                }
        );

        requestQueue.add(jsonarrayRequest);

    }

    public void addData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if (insertData) {
            toastMessage("Data successfully inserted");
            queryData(newEntry);
        } else {
            toastMessage("Error in inserting data");
        }
    }

    public void addMeaning(String newMeaning){
        boolean insertMeaning = mDatabaseHelper.addMeaning(newMeaning);

        if(insertMeaning){
            Log.e("MEANING ", "ADDED");
        } else {
            Log.e("MEANING UNSUCCESSFULL","");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    //CODE FOR REST API



}

