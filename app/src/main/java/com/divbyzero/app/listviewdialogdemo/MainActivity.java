package com.divbyzero.app.listviewdialogdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnDialog;
    String[] listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDialog = (Button) findViewById(R.id.btnShowDialog);
        btnDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnShowDialog){
            fetchData();
        }
    }

    public void fetchData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String URL ="https://jsonplaceholder.typicode.com/users";

        StringRequest dataRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsoArray = new JSONArray(response);
                            int len = jsoArray.length();

                            listData = new String[len];

                            for (int x = 0; x < len; x++){
                                JSONObject obj = jsoArray.getJSONObject(x);
                                listData[x] = obj.getString("name");

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                LayoutInflater inflater = getLayoutInflater();
                                View convertView = (View) inflater.inflate(R.layout.listdialog, null);
                                alertDialog.setView(convertView);
                                alertDialog.setTitle("List");
                                ListView lv = (ListView) convertView.findViewById(R.id.listView1);
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listData);
                                lv.setAdapter(adapter);
                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        Toast.makeText(getApplicationContext(), "You picked: "+listData[position], Toast.LENGTH_SHORT).show();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                        catch (JSONException je){
                            Toast.makeText(getApplicationContext(), "JSON error: "+je.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(dataRequest);
    }
}
