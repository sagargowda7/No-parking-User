package in.dropcodes.npuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteChildActivity extends AppCompatActivity {
    public FirebaseAuth mAuth;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mRef;
    public Thread thread;
    public String Uid,UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         Bundle bundle = getIntent().getExtras();
         Uid = bundle.getString("uid");

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        UID = user.getUid();

        mRef = mDatabase.getReference().child("parking").child(Uid).child("record");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(DeleteChildActivity.this,MainActivity.class);
                mRef.child(UID).child("check_in_time").removeValue();
                mRef.child(UID).child("check_out_time").removeValue();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });thread.start();

    }
}
