package in.dropcodes.npuser;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParkingDetailsActivity extends AppCompatActivity {

    public String uid;
    public CircleImageView mImage;
    public TextView mName,mPlace,mParking,mAvaParking;
    public Button mScan;
    public RadioGroup mRadioCheckGroup;
    public RadioButton mRadioCheckButton;
    public FirebaseDatabase mDatabse;
    public DatabaseReference mReference;
    public int filledInt,total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        //Bundles
        Bundle bundle =getIntent().getExtras();
        uid = bundle.getString("uid");

        //FireBase
        mDatabse = FirebaseDatabase.getInstance();
        mReference = mDatabse.getReference().child("parking").child(uid);

        //Initialization
        mImage = findViewById(R.id.pd_iamge);
        mName =findViewById(R.id.pd_name);
        mPlace = findViewById(R.id.pd_place);
        mParking = findViewById(R.id.pd_parking);
        mAvaParking =findViewById(R.id.pd_ava);
        mScan =findViewById(R.id.pd_qr);
        mRadioCheckGroup =findViewById(R.id.radio_group_check);


        //RadioButton for Check In and check Out
        int radioCheckId = mRadioCheckGroup.getCheckedRadioButtonId();
        mRadioCheckButton = findViewById(radioCheckId);


        //FireBaseReference
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name =dataSnapshot.child("name").getValue().toString();
                String place = dataSnapshot.child("area").getValue().toString();
                String Total = dataSnapshot.child("total").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String filled = dataSnapshot.child("filled").getValue().toString();


                mName.setText("Parking place: "+name);
                mPlace.setText("Address: "+place);
                mParking.setText("Total Car parking provided: "+Total);
                Picasso.get().load(image).fit().centerInside().placeholder(R.drawable.loadingimg).into(mImage);

                //converting String to integer
                total =Integer.parseInt(Total);
                filledInt = Integer.parseInt(filled);


                int AvaParking = total - filledInt;

                //Converting Int to String
                String avaPaking = String.valueOf(AvaParking);
                mAvaParking.setText("Available Bike parking: "+avaPaking);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ParkingDetailsActivity.this, "There  was some error Please Check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    public void CheckScan(View view) {
        int radioCheckId = mRadioCheckGroup.getCheckedRadioButtonId();
        mRadioCheckButton = findViewById(radioCheckId);
        Snackbar.make(view,"You have selected:"+mRadioCheckButton.getText(),Snackbar.LENGTH_LONG).show();

    }



}