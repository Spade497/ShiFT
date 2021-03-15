package mmu.edu.my.shift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DisplayAll extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Jobs> jobsList;
    private JobsAdapter2 adapter;
    private Spinner dropdown, dropdown2;
    private String State, City;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);
        progressBar=findViewById(R.id.progressbar);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dropdown2 = findViewById(R.id.filter_city);
        dropdown2.setVisibility(View.INVISIBLE);
        dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Select city")) {
                    State = parent.getItemAtPosition(position).toString();
                    fillSecondSpinner(State);
                    db.collection("Post").whereEqualTo("state",State).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    progressBar.setVisibility(View.GONE);
                                    jobsList.clear();
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot d:list){
                                            Jobs j=d.toObject(Jobs.class);
                                            jobsList.add(j);
                                        }
                                    }
                                    else
                                        Toast.makeText(DisplayAll.this, "No job have been posted yet for filtered condition.", Toast.LENGTH_SHORT).show();


                                    adapter.notifyDataSetChanged();
                                }
                            });
                } else {
                    City = parent.getItemAtPosition(position).toString();
                    db.collection("Post").whereEqualTo("district",City).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    progressBar.setVisibility(View.GONE);
                                    jobsList.clear();
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot d:list){
                                            Jobs j=d.toObject(Jobs.class);
                                            jobsList.add(j);
                                        }
                                    }
                                    else
                                        Toast.makeText(DisplayAll.this, "No job have been posted yet for filtered condition.", Toast.LENGTH_SHORT).show();


                                    adapter.notifyDataSetChanged();
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dropdown = findViewById(R.id.filter_state);
        String[] items = new String[]{"Select state","Johor", "Kedah", "Kelantan", "Kuala Lumpur", "Labuan", "Melaka", "Negeri Sembilan", "Pahang", "Penang", "Perak", "Perlis",  "Putrajaya", "Sabah", "Sarawak", "Selangor", "Terengganu"};
        ArrayAdapter<String> adapter_s = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter_s.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter_s);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("Select state")) {
                    db.collection("Post").get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot d:list){
                                            Jobs j=d.toObject(Jobs.class);
                                            jobsList.add(j);
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else Toast.makeText(DisplayAll.this, "No job have been posted yet.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    dropdown2.setVisibility(View.VISIBLE);
                    State = parent.getItemAtPosition(position).toString();
                    fillSecondSpinner(State);
                    db.collection("Post").whereEqualTo("state",State).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    progressBar.setVisibility(View.GONE);
                                    jobsList.clear();
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot d:list){
                                            Jobs j=d.toObject(Jobs.class);
                                            jobsList.add(j);
                                        }
                                    }
                                    else
                                        Toast.makeText(DisplayAll.this, "No job have been posted yet for filtered condition.", Toast.LENGTH_SHORT).show();


                                    adapter.notifyDataSetChanged();
                                }
                            });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jobsList=new ArrayList<>();
        adapter=new JobsAdapter2(this,jobsList);
        recyclerView.setAdapter(adapter);
        db.collection("Post").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        progressBar.setVisibility(View.GONE);
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d:list){
                                Jobs j=d.toObject(Jobs.class);
                                jobsList.add(j);
                            }
                            adapter.notifyDataSetChanged();
                        } else Toast.makeText(DisplayAll.this, "No job have been posted yet.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fillSecondSpinner(String u) {

        ArrayAdapter<String> adapter1;

        if (u.equals("Johor")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Johor);
        } else if (u.equals("Kedah")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Kedah);
        } else if (u.equals("Kelantan")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Kelantan);
        } else if (u.equals("Kuala Lumpur")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Kuala_Lumpur);
        } else if (u.equals("Labuan")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Labuan);
        } else if (u.equals("Melaka")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Melaka);
        } else if (u.equals("Negeri Sembilan")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Negeri);
        } else if (u.equals("Pahang")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Pahang);
        } else if (u.equals("Penang")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Penang);
        } else if (u.equals("Perak")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Perak);
        } else if (u.equals("Perlis")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Perlis);
        } else if (u.equals("Putrajaya")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Putrajaya);
        } else if (u.equals("Sabah")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Sabah);
        } else if (u.equals("Sarawak")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Sarawak);
        } else if (u.equals("Selangor")) {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Selangor);
        } else {
            adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, d_Terengganu);
        }

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown2.setAdapter(adapter1);
    }

    //list of town/district in each state
    String[] d_Johor = new String[]{"Select city","Batu Pahat","Johor Bahru","Kluang","Kota Tinggi","Kulai","Mersing","Muar","Pontian Kechil","Segamat","Tangkak"};
    String[] d_Kedah= new String[]{"Select city","Baling","Bandar Baharu","Alor Setar","Kuala Muda","Kubang Pasu","Kulim","Langkawi","Padang Terap","Pendang","Pokok Sena","Sik","Yan"};
    String[] d_Kelantan = new String[]{"Select city","Bachok","Gua Musang","Jeli","Kota Bharu","Kuala Krai","Machang","Pasir Mas","Pasir Puteh","Tanah Merah","Tumpat"};
    String[] d_Kuala_Lumpur = new String[]{"Select city","Ampang","Batu Caves","Cheras","Damansara","Hulu Kelang","Kepong","Kuala Lumpur","Petaling Jaya","Sentul","Setapak","Sungai Besi"};
    String[] d_Labuan = new String[]{"Select city","Labuan"};
    String[] d_Melaka = new String[]{"Select city","Alor Gajah","Central Melaka","Jasin"};
    String[] d_Negeri = new String[]{"Select city","Jelebu","Jempol","Kuala Pilah","Port Dickson","Rembau","Seremban","Tampin"};
    String[] d_Pahang = new String[]{"Select city","Bentong","Bera","Cameron Highlands","Jerantut","Kuantan","Lipis","Maran","Pekan","Raub","Rompin","Temerloh"};
    String[] d_Penang = new String[]{"Select city","Bukit Mertajam","Kepala Batas","George Town","Sungai Jawi","Balik Pulau"};
    String[] d_Perak = new String[]{"Select city","Bagan Datuk","Batang Padang","Hilir Perak","Hulu Perak","Kampar","Kerian","Kinta","Kuala Kangsar","Manjung","Seri Iskandar","Taiping","Tanjung Malim"};
    String[] d_Perlis = new String[]{"Select city","Kangar"};
    String[] d_Putrajaya = new String[]{"Select city","Putrajaya"};
    String[] d_Selangor = new String[]{"Select city","Gombak","Hulu Langat","Hulu Selangor","Klang","Kuala Langat","Kuala Selangor","Petaling","Sabak Bernam","Sepang"};
    String[] d_Terengganu = new String[]{"Select city","Besut","Dungun","Hulu Terengganu","Kemaman","Kuala Nerus","Kuala Terengganu","Marang","Setiu"};
    String[] d_Sabah = new String[]{"Select city","Beaufort","Keningau","Kuala Penyu","Nabawan","Sipitang","Tambunan","Tenom","Kota Marudu","Kudat","Pitas","Beluran","Kinabatangan","Snadakan","Telupid","Tongod","Kalabakan","Kunak","Lahad Datu","Semporna","Tawau","Kota Belud","Kota Kinabalu","Papar","Penampang","Putatan","Ranau","Tuaran"};
    String[] d_Sarawak = new String[]{"Select city","Betong","Kabong","Pusa","Saratok","Bintulu","Sebauh","Tatau","Belaga","Kapit","Song","Bukit Mabong","Bau","Kuching","Lundu","Lawas","Limbang","Beluru","Marudi","Miri","Subis","Telang","Dalat","Daro","Matu","Mukah","Belawai","Asajaya","Samarahan","Simunjan","Julau","Meradong","Pakan","Sarikei","Serian","Tebedu","Kanowit","Sibu","Selangau","Lubok Antu","Sri Aman"};

}