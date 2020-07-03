package com.example.forest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
import com.hudomju.swipe.adapter.RecyclerViewAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.recyclerview.widget.ItemTouchHelper.LEFT;
//Volley
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
//JSON
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskActivity extends AppCompatActivity {
    private ListView lv;
    public Adapter adapter;
    public ArrayList<Model> modelArrayList;
    public JSONArray data_array=new JSONArray();

    private String urlJsonArry = "https://forestweb.herokuapp.com/apptask";
    private String urltask = "https://forestweb.herokuapp.com/gettask";

    private static String TAG = MainActivity.class.getSimpleName();

    private String jsonResponse;

    private String[] myList = new String[]{"Benz", "Bike",
            "Car","Carrera"
            ,"Ferrari","Harly",
            "Lamborghini","Silver"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        lv = (ListView) findViewById(R.id.listview);
        modelArrayList=populateList();
        Log.d("before",modelArrayList.toString());

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("wait","wait function");
                try {
                    modelArrayList=makeJsonArrayRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("hello",modelArrayList.toString());
            }

            public void onFinish() {
//                adapter = new Adapter(this,modelArrayList);
//                lv.setAdapter(adapter);
                next();
            }
        }.start();
//        adapter = new Adapter(this,modelArrayList);
//        lv.setAdapter(adapter);
//
//        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
//                new SwipeToDismissTouchListener<>(
//                        new ListViewAdapter(lv),
//                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
//                            @Override
//                            public boolean canDismiss(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public void onDismiss(ListViewAdapter view, int position) {
//                                adapter.remove(position);
//                            }
//                        });
//
//        lv.setOnTouchListener(touchListener);
//        lv.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (touchListener.existPendingDismisses()) {
//                    touchListener.undoPendingDismiss();
//                } else {
//                    Toast.makeText(TaskActivity.this, "Position " + position, LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    public void next(){

        adapter = new Adapter(this,modelArrayList);
        lv.setAdapter(adapter);

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(lv),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                adapter.remove(position);
                                try {
                                    sendrequest(position);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
                    Toast.makeText(TaskActivity.this, "Position " + position, LENGTH_SHORT).show();
                }
            }
        });
    }

    private ArrayList<Model> populateList(){

        ArrayList<Model> list = new ArrayList<>();

        for(int i = 0; i < myList.length; i++){
            Model model = new Model();
            model.setName(myList[i]);
            list.add(model);
        }

        return list;

    }

    //server request
    private ArrayList<Model> makeJsonArrayRequest() throws JSONException {

//        showpDialog();
        final ArrayList<Model> list = new ArrayList<>();
        String temp = "";
        FileInputStream fin = null;
        try {
            fin = openFileInput("mytextfile.txt");


            int c;
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }

            fin.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject d=new JSONObject();
        d.put("id",temp);
        JSONArray xyz=new JSONArray();
        xyz.put(temp);
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST,urltask,xyz,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("json array", response.toString());

                        try {
//                            JSONObject jsonObject = new JSONObject();
//                            jsonObject.put("task", response.toString());

                            JSONArray tasks = new JSONArray(response.toString());

                            Log.d("list",tasks.toString());

                            for(int i = 0; i < tasks.length(); i++){
                                Model model = new Model();
                                JSONObject curr = tasks.getJSONObject(i);
                                model.setName(curr.getString("task_info"));
                                list.add(model);
                            }

                            Log.d("add list",list.toString());
                            data_array=tasks;
//                            return(list);

                        }
                     catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

//         Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(req);
        Log.d("function",list.toString());
        return list;
    }

    private void sendrequest(int i) throws JSONException {
        JSONObject rem= (JSONObject) data_array.get(i);
        rem.remove("status");
        rem.put("status","complete");
        JsonObjectRequest req=new JsonObjectRequest(urlJsonArry,rem,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject data){
                        Log.d("response",data.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidepDialog();
                    }
                });

        // Adding request to request queue
        Appcontroller.getInstance().addToRequestQueue(req);
        Log.d("function",modelArrayList.toString());

    }

}


