package com.example.kartheek.jsonexample2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ListView listView;
   ProgressDialog progressDialog;
    String values[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.list);
    }
    public void loadJson(View view){

        new AsyncOperation().execute();
    }

    private class AsyncOperation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url=new URL("https://api.myjson.com/bins/2iq8d");
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String json_string=bufferedReader.readLine();
                JSONObject jsonObject=new JSONObject(json_string);
                JSONArray jsonArray=jsonObject.getJSONArray("places");
                values=new String[jsonArray.length()];
                for(int i=0;i<jsonArray.length();i++){

                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                   String name= jsonObject1.getString("name");
                   double lat= jsonObject1.getDouble("Latitude");
                    double lang=jsonObject1.getDouble("Longitude");
                    values[i]="Place="+name+" Lattitue="+lat+" Langitude="+lang+"\n";
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            ArrayAdapter arrayAdapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_dropdown_item_1line,values);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, ""+values[position], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
