package in.dropcodes.npuser;

import android.app.Activity;
import android.content.Intent;
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
    public TextView mName, mPlace, mCar, mBike, mAvaCar, mAvaBike;
    public Button mCheckIn, mCheckOut;
    public RadioGroup mRadioCheckGroup;
    public RadioButton mRadioCheckButton;
    public FirebaseDatabase mDatabse;
    public DatabaseReference mReference;
    private String child;

    public void CheckScan(View view) {
        int radioCheckId = mRadioCheckGroup.getCheckedRadioButtonId();
        mRadioCheckButton = findViewById(radioCheckId);
        child = mRadioCheckButton.getText().toString();
        Snackbar.make(view, "You have selected:" + mRadioCheckButton.getText(), Snackbar.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_details);

        //Bundles
        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");

        //FireBase
        mDatabse = FirebaseDatabase.getInstance();
        mReference = mDatabse.getReference().child("parking").child(uid);

        //Initialization
        mImage = findViewById(R.id.pd_iamge);
        mName = findViewById(R.id.pd_name);
        mPlace = findViewById(R.id.pd_place);
        mCar = findViewById(R.id.pd_car);
        mBike = findViewById(R.id.pd_bike);
        mAvaCar = findViewById(R.id.pd_ava_car);
        mAvaBike = findViewById(R.id.pd_ava_bike);
        mCheckIn = findViewById(R.id.pd_check_in);
        mCheckOut = findViewById(R.id.pd_check_out);
        mRadioCheckGroup = findViewById(R.id.radio_group);



        //FireBaseReference
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String place = dataSnapshot.child("area").getValue().toString();
                String car = dataSnapshot.child("car").getValue().toString();
                String bike = dataSnapshot.child("bike").getValue().toString();
                String Avacar = dataSnapshot.child("carPark").getValue().toString();
                String Avabike = dataSnapshot.child("bikePark").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();


                mName.setText("Parking place: " + name);
                mPlace.setText("Address: " + place);
                mCar.setText("Total Car parking provided: " + Avacar);
                mBike.setText("Total Bike parking provided: " + Avabike);
                mAvaCar.setText("Available Car parking: " + car);
                mAvaBike.setText("Available Bike parking: " + bike);

                Picasso.get().load(image).fit().centerInside().placeholder(R.drawable.loadingimg).into(mImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ParkingDetailsActivity.this, "There  was some error Please Check your Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        //Radio button
        int radioCheckId = mRadioCheckGroup.getCheckedRadioButtonId();
        mRadioCheckButton = findViewById(radioCheckId);
        child = mRadioCheckButton.getText().toString();

        mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ci = new Intent(ParkingDetailsActivity.this,CheckInActivity.class);
                ci.putExtra("uid",uid);
                ci.putExtra("child",child);
                startActivity(ci);

            }
        });

        mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }



}
