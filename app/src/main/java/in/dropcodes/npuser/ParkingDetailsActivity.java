package in.dropcodes.npuser;

import android.app.Activity;
import android.app.ProgressDialog;
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
    public TextView mName, mPlace,mTotal, mPark;
    public Button mCheckIn, mCheckOut;
    public FirebaseDatabase mDatabse;
    public DatabaseReference mReference;
    private String Total,Park;
    public ProgressDialog mProgressDialog;

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
        mTotal = findViewById(R.id.pd_total);
        mPark = findViewById(R.id.pd_park);
        mCheckIn = findViewById(R.id.pd_check_in);
        mCheckOut = findViewById(R.id.pd_check_out);

        //ProgressDialog
        mProgressDialog = new ProgressDialog(ParkingDetailsActivity.this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setTitle("Fetching data");
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        //FireBaseReference
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String place = dataSnapshot.child("area").getValue().toString();
                Total = dataSnapshot.child("total").getValue().toString();
                Park = dataSnapshot.child("parked").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();


                mName.setText("Parking place: " + name);
                mPlace.setText("Address: " + place);
                mTotal.setText("Total parking provided: " + Total);
                mPark.setText("Available parking place: " + Park);
                Picasso.get().load(image).fit().centerInside().placeholder(R.drawable.loadingimg).into(mImage);


                if (Park.equals(Total)){

                    mCheckOut.setVisibility(View.INVISIBLE);
                }else {

                    mCheckOut.setVisibility(View.VISIBLE);

                }
                if (Park.equals("0")){
                    mCheckIn.setVisibility(View.INVISIBLE);
                }else {
                    mCheckIn.setVisibility(View.VISIBLE);
                }



                mProgressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ParkingDetailsActivity.this, "There  was some error Please Check your Internet connection", Toast.LENGTH_SHORT).show();
                mProgressDialog.hide();
            }
        });

        mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ci = new Intent(ParkingDetailsActivity.this,CheckInActivity.class);
                ci.putExtra("uid",uid);
                startActivity(ci);

            }
        });

        mCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ParkingDetailsActivity.this,CheckOutActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
