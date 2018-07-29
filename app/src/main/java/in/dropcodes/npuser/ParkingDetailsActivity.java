package in.dropcodes.npuser;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
        mProgressDialog.setIcon(R.mipmap.ic_launcher_foreground);
        mProgressDialog.show();


        //Checking Network Exist
        if(!isConnected(ParkingDetailsActivity.this)) builderDialog(ParkingDetailsActivity.this).show();
        else {

            //FireBaseReference
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("name").getValue().toString();
                    String place = dataSnapshot.child("area").getValue().toString();
                    Total = dataSnapshot.child("total").getValue().toString();
                    Park = dataSnapshot.child("parked").getValue().toString();
                    String image = dataSnapshot.child("image").getValue().toString();


                    mName.setText("Parking Name: " + name);
                    mPlace.setText("Address: " + place);
                    mTotal.setText("Total parking provided: " + Total);
                    mPark.setText(" "+ Park);
                    Picasso.get().load(image).fit().centerInside().placeholder(R.drawable.placeholder).into(mImage);


                    //Making Check Out Button InVisible
                    if (Park.equals(Total)){
                        mCheckOut.setVisibility(View.INVISIBLE);
                    }else {
                        mCheckOut.setVisibility(View.VISIBLE);
                    }
                    //Making Check In Button InVisible
                    if (Park.equals("0")){
                        mCheckIn.setVisibility(View.INVISIBLE);
                        mPark.setText("0");
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


        }

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


    public boolean isConnected(Context context){

        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        }else return false;
    }
    public AlertDialog.Builder builderDialog (Context c){
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setCancelable(false);
        builder.setMessage("You need to have Mobile Data or WiFi Connection");
        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

    }
}
