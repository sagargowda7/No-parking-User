package in.dropcodes.npuser;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import in.dropcodes.npuser.Adapter.MainAdapter;
import in.dropcodes.npuser.Model.MainModel;

public class SearchActivity extends AppCompatActivity {
    
    public EditText mSearchText;
    public ImageButton msearchBtn;
    public RecyclerView mRecyclerView;
    public MainAdapter adapter;
    public DatabaseReference mDatabaseReference;
    public List<MainModel> mainModels;
    public String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("Search Parking Place");
        
        mSearchText = findViewById(R.id.search_edit);
        msearchBtn = findViewById(R.id.search_btn);
        mRecyclerView = findViewById(R.id.search_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainModels = new ArrayList<>();

        msearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search = mSearchText.getEditableText().toString();
                if (!TextUtils.isEmpty(search)){

                    firebaseSearch(search);
                    mSearchText.setText("");
                }else {
                    Snackbar.make(view,"Please enter parking name ",Snackbar.LENGTH_LONG).show();
                }


            }
        });

    }

    private void firebaseSearch(String search) {

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("parking");

        Query query = mDatabaseReference.orderByChild("name").startAt(search).endAt(search + "\uf8ff");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot parkingSnapshot : dataSnapshot.getChildren()){
                        MainModel model = parkingSnapshot.getValue(MainModel.class);
                        mainModels.add(model);
                    }
                    adapter = new MainAdapter(SearchActivity.this,mainModels);
                    adapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(SearchActivity.this, "Error while fetching data. Please check your internet connection", Toast.LENGTH_LONG).show();

                }
            });

    }
}
