package in.dropcodes.npuser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    public String UID,UserUID;
    public String checkIn,checkOut;
    public TextView mTime,mCharge;
    public Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        //Initializing
        mTime = findViewById(R.id.parking_time);
        mCharge = findViewById(R.id.charge);

        //Bundles for Extras
        Bundle bundle = getIntent().getExtras();
        UID = bundle.getString("uid");

        //FireBase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        UserUID = mUser.getUid();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference().child("parking").child(UID).child("record").child(UserUID);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                checkIn = dataSnapshot.child("check_in_time").getValue().toString();
                checkOut = dataSnapshot.child("check_out_time").getValue().toString();

                //Converting time to int from string
                int in = Integer.parseInt(checkIn);
                int out = Integer.parseInt(checkOut);
                int total = out -in;
                String tot = String.valueOf(total);
                mTime.setText("Your car was parked for: "+tot+" min");

                //calculating cost for parking
                if (total == 0){
                    mCharge.setText("Parking charge: "+"RS 10");
                }else {
                    if (total > 240)
                    {
                        mCharge.setText("Parking charge: "+"RS 60");
                    }else {
                        mCharge.setText("Parking charge: "+"RS 40");
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(PaymentActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        //Handling threads

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread.sleep(15000);
                    Intent i = new Intent(PaymentActivity.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });thread.start();


    }
}
