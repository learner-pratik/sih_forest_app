package com.example.forest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertActivity extends AppCompatActivity {

    public static HashMap<String, List<String>> map = new HashMap();
    Button alertButton;

    public static List<List<String>> map1 = new ArrayList<>();
    FileInputStream is;
    BufferedReader reader;
    File folder;
    File file;
    Button alertButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        if (getIntent().getIntExtra("callingActivity", 0) == 1001) {
            readFromFile();
            getAlerts();
        } else {
            getNotificationAlerts();
        }

    }

    private void readFromFile() {
        folder = new File(getFilesDir()+"/forest");
        file = new File(folder.getAbsolutePath()+"/alert.txt");

        if (file.exists()) {
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(line != null){
                Log.d("StackOverflow", line);
                String[] values = line.split("/");
                map1.add(Arrays.asList(values));
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendLocation1(View view) {
        alertButton1 = (Button) findViewById(R.id.label1);
        System.out.println("method called");
        String text = (String) alertButton1.getText();
        String[] values = text.split(" ");
        String id = values[4];
        int val = 0;
        for (int i=0; i<map1.size(); i++) {
            if (map1.get(i).get(0) == id){
                val = i;
                break;
            }
        }
        List<String> list = AlertActivity.map1.get(val);
        System.out.println(list);
        String latitude = list.get(1);
        String longitude = list.get(2);
//        Intent intent = new Intent(AlertActivity.this, MapActivity.class);
//        intent.putExtra("latitude", Double.parseDouble(latitude));
//        intent.putExtra("longitude", Double.parseDouble(longitude));
//        startActivity(intent);
    }

    public void sendLocation(View view) {
        alertButton = (Button) findViewById(R.id.label);
        System.out.println("method called");
        String text = (String) alertButton.getText();
        String[] values = text.split(" ");
        String id = values[4];
        List<String> list = AlertActivity.map.get(id);
        System.out.println(list);
        String latitude = list.get(0);
        String longitude = list.get(1);
//        Intent intent = new Intent(AlertActivity.this, MapActivity.class);
//        intent.putExtra("latitude", Double.parseDouble(latitude));
//        intent.putExtra("longitude", Double.parseDouble(longitude));
//        startActivity(intent);
    }

    private void getNotificationAlerts() {
        final List<String> camera = new ArrayList<>();
        for (Map.Entry<String, List<String>> set : map.entrySet()) {
            String s = set.getKey();
            camera.add("Alert spotted at camera "+s);
        }
        String[] camera_id = camera.toArray(new String[0]);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, camera_id);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        map.clear();
    }

    private void getAlerts() {
        final List<String> camera = new ArrayList<>();
        for (List<String> strings : map1) {
            String s = strings.get(0);
            camera.add("Alert spotted at camera "+s);
        }
        String[] camera_id = camera.toArray(new String[0]);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview1, camera_id);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
    }
}
