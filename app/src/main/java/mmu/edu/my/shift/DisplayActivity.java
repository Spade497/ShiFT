package mmu.edu.my.shift;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.Job;

public class DisplayActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<Jobs> jobsList;
    private JobsAdapter adapter;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        progressBar=findViewById(R.id.progressbar);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        jobsList=new ArrayList<>();
        adapter=new JobsAdapter(this,jobsList);
        adapter.setOnItemClickListener(new JobsAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                i = position;
                alert();
            }
        });
        recyclerView.setAdapter(adapter);
        display();
    }

    private void display() {
        db.collection("Post").whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
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
                        } else Toast.makeText(DisplayActivity.this, "No job have been posted yet.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void alert(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure to delete this job?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String desc = jobsList.get(i).getDesc();
                        String wage = jobsList.get(i).getWage();
                        String info = jobsList.get(i).getInfo();
                        db.collection("Post")
                                .whereEqualTo("id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .whereEqualTo("desc", desc).whereEqualTo("wage", wage).whereEqualTo("info", info)
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                                        for (DocumentSnapshot snapshot: snapshotList){
                                            writeBatch.delete(snapshot.getReference());
                                        }
                                        writeBatch.commit();
                                    }
                                });
                        jobsList.remove(i);
                        adapter.notifyItemRemoved(i);
                        Toast.makeText(DisplayActivity.this, "Job deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

   /* @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(DisplayActivity.this,MainActivity.class));
    }*/
}

