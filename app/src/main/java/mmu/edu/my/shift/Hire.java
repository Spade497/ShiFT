package mmu.edu.my.shift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Hire extends AppCompatActivity {

    private EditText name, district, desc, wage, info;
    private String Name, District, Desc, Wage, Info, State=null;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    private Spinner dropdown;
    //private CollectionReference col = db.collection(userid);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);
        name=findViewById(R.id.editTextTextPersonName);
        district=findViewById(R.id.editTextDistrict);
        desc=findViewById(R.id.editTextTextMultiLine);
        wage=findViewById(R.id.editTextWage);
        info=findViewById(R.id.editTextContact);

        dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan", "Melaka", "Negeri Sembilan", "Pahang", "Perak", "Perlis", "Pulau Pinang", "Putrajaya", "Sabah", "Sarawak", "Selangor", "Terengganu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TextView errorText = (TextView)dropdown.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(Color.RED);//just to highlight that this is an error
                errorText.setText("No selected item");
            }
        });

        Button post = findViewById(R.id.btnpost);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString().trim();
                District = district.getText().toString().trim();
                Desc = desc.getText().toString().trim();
                Wage = wage.getText().toString().trim();
                Info = info.getText().toString().trim();

                if (!validateInputs(Name,State,District,Desc,Wage,Info)){
                    /*Map<String,Object> jobs;
                    jobs = new HashMap<>();
                    jobs.put("Name", Name);
                    jobs.put("State", State);
                    jobs.put("District", District);
                    jobs.put("Desc", Desc);
                    jobs.put("Wage", Wage);
                    jobs.put("Info", Info);*/

                    CollectionReference dbJobs=db.collection(userid);
                    Jobs jobs = new Jobs(
                            Name,
                            State,
                            District,
                            Desc,
                            Wage,
                            Info
                    );

                    dbJobs.add(jobs)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    startActivity(new Intent(Hire.this,DisplayActivity.class));
                                    Toast.makeText(Hire.this,"Job Posted",Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Hire.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        TextView view=findViewById(R.id.textViewJob);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Hire.this,DisplayActivity.class));
            }
        });
    }

    private boolean validateInputs (String n, String s, String d, String de, String w, String i) {
        if(n.isEmpty()){
            name.setError("Name required");
            name.requestFocus();
            return true;
        }

        if(s.isEmpty()){
            TextView errorText = (TextView)dropdown.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);//just to highlight that this is an error
            errorText.setText("No selected item");//changes the selected item text to this
        }

        if(d.isEmpty()){
            district.setError("Name required");
            district.requestFocus();
            return true;
        }

        if(de.isEmpty()){
            desc.setError("Name required");
            desc.requestFocus();
            return true;
        }

        if(w.isEmpty()){
            wage.setError("Name required");
            wage.requestFocus();
            return true;
        }

        if(i.isEmpty()){
            info.setError("Name required");
            info.requestFocus();
            return true;
        }

        return false;
    }
}