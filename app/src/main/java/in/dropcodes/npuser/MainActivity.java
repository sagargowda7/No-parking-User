package in.dropcodes.npuser;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import in.dropcodes.npuser.Adapter.MainAdapter;
import in.dropcodes.npuser.Model.MainModel;

public class MainActivity extends AppCompatActivity {

    public RecyclerView mRecycler;
    public MainAdapter adapter;
    public DatabaseReference mDatabaseReference;
    public List<MainModel> mainModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecycler = findViewById(R.id.recycler_view);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        mainModels = new ArrayList<>();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Fetching data ");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("parking");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot parkingsnapshot : dataSnapshot.getChildren()){
                    MainModel model = parkingsnapshot.getValue(MainModel.class);
                    mainModels.add(model);
                }
                adapter = new MainAdapter(MainActivity.this,mainModels);
                mRecycler.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error while fetching data. Please check your internet connection", Toast.LENGTH_LONG).show();
                progressDialog.hide();
            }
        });
    }
}
