package com.example.forest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import timber.log.Timber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LocationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String,Object> option = new HashMap<String,Object>();

    // Set for storing keys of documents
    Set<String> k = new HashSet<String>();

    // Object value corresponding to key recieved in firebase response which will be converted to list of strings
    Object v ;

    // Temporary List for spinner2 adapter
    String[] vi = {"All"};

    //Debug tag
    String TAG = "DEBUG";

    // Spinner declaration
    Spinner spin1;
    Spinner spin2;

    // Button declaration
    Button btnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        db.collection("animals_list")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                k.add(document.getId());
                                option.put(document.getId(),(document.getData()).get("id"));
                                Timber.d(document.getId() + " => " + document.getData());
                            }
                            k.add("All");
                            setGroup(k);
                        } else {
                            Timber.d(task.getException(), "Error getting documents: ");
                        }
                    }
                });


        //Declare spinner
        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin2 = (Spinner) findViewById(R.id.spinner2);


        //spinner 2
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vi);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapter2);
        spin2.setSelection(0);
        spin2.setOnItemSelectedListener(this);
        spin2.setEnabled(false);
        spin2.setClickable(false);


        // Button code
        btnLoc=findViewById(R.id.btnLoc);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send selected option from spinner as map to next activity
                Intent a = new Intent(LocationActivity.this,MapActivity.class);
                if(spin1.getSelectedItemPosition()==0){
                    HashMap<String, Object> copy = new HashMap<String, Object>(option);
                    a.putExtra("map", copy);
                }
                else{
                    if(spin2.getSelectedItemPosition()==0){
                        HashMap<String, Object> copy = new HashMap<String, Object>();
                        copy.put(spin1.getSelectedItem().toString(),option.get(spin1.getSelectedItem().toString()));
                        a.putExtra("map",copy);
                    }
                    else{
                        HashMap<String, Object> copy = new HashMap<String, Object>();
                        copy.put(spin1.getSelectedItem().toString(),spin2.getSelectedItem());
                        a.putExtra("map",copy);
                    }
                }
                startActivity(a);
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), "Selected User: "+ key[position] ,Toast.LENGTH_SHORT).show();
        String temp;
        switch (parent.getId()){
            case R.id.spinner1:
                //Do something
                if(parent.getSelectedItemPosition()!=0){
                    spin2.setEnabled(true);
                    spin2.setClickable(true);
                    temp = parent.getSelectedItem().toString();
                    v = option.get(temp);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, convertObjectToList(v));
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adapter2);
                    spin2.setSelection(0);
                    spin2.setOnItemSelectedListener(this);
                }
                else{
                    spin2.setSelection(0);
                    spin2.setEnabled(false);
                    spin2.setClickable(false);
                }
                //Toast.makeText(this, "Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.spinner2:
                //Do another thing
                //Toast.makeText(this, "Selected: " + parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO - Custom Code


    }

    public static String[] convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        String[] r = new String[1];
        r[0] = "All";
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
            String[] value = new String[list.size()+1];
            value[0] = "All";
            int j = 1;
            for (Object object : list) {
                value[j] = (object != null ? object.toString() : null);
                j++;
            }
            return(value);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
            String[] value = new String[list.size()+1];
            value[0]="All";
            int j =1;
            for (Object object : list) {
                value[j] = (object != null ? object.toString() : null);
                j++;
            }
            return(value);
        }
        return r;
    }

    public void setGroup(Set<String> g){
        String[] my_key = new String[g.size()];
        int i=0;
        for(String x:g){
            my_key[i++] = x;
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, my_key);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adapter1);
        spin1.setSelection(0);
        spin1.setOnItemSelectedListener(this);
    }
}