package in.dropcodes.npuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.text.SimpleDateFormat;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckOutActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public ZXingScannerView ScannerView;
    public String uid ;
    public FirebaseDatabase mDatabaseR;
    public DatabaseReference mReferenceR,mUserRef;
    public String values,UserUID;
    public FirebaseAuth mAuth;
    public FirebaseUser mUser;


    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundl = getIntent().getExtras();
        uid = bundl.getString("uid");

        ScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(ScannerView);
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        ScannerView.resumeCameraPreview(this);
        final String UID = result.getText();

        if (UID.equals(uid)){

            mDatabaseR = FirebaseDatabase.getInstance();
            mReferenceR = mDatabaseR.getReference().child("parking").child(uid);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            UserUID = mUser.getUid();
            mUserRef = mReferenceR.child("record").child(UserUID);

            //GetCurrentTime
            long current_time = System.currentTimeMillis();
            SimpleDateFormat sdfMin = new SimpleDateFormat("mm");
            SimpleDateFormat sdfHrs = new SimpleDateFormat("hh");
            String minString = sdfMin.format(current_time);
            String HrsString = sdfHrs.format(current_time);
            int minInt = Integer.parseInt(minString);
            int hrsInt = Integer.parseInt(HrsString);

            //Converting Hrs to Min
            int HrsToMin = hrsInt * 60;
            final int totalTime = minInt+ HrsToMin;


            //checking if user already CheckIn
            mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("check_in_time")){
                        mUserRef.child("check_out_time").setValue(totalTime);
                        getData();
                    }else {

                        Toast.makeText(CheckOutActivity.this, "You have not check In yet", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(CheckOutActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
                }
            });


            Intent intent = new Intent(CheckOutActivity.this,PaymentActivity.class);
            intent.putExtra("uid",uid);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();


        }else {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckOutActivity.this);
            LayoutInflater inflater = LayoutInflater.from(CheckOutActivity.this);
            View licenses_layout = inflater.inflate(R.layout.wrong_place, null);
            alertDialog.setView(licenses_layout);
            alertDialog.setTitle("Woops!");
            alertDialog.setIcon(R.mipmap.ic_launcher_foreground);
            alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();

        }

    }

    private void getData(){

        mReferenceR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                values = dataSnapshot.child("parked").getValue().toString();

                int v =Integer.parseInt(values);
                int vFinal = v + 1;
                String fin = String.valueOf(vFinal);
                mReferenceR.child("parked").setValue(fin);
                Toast.makeText(CheckOutActivity.this, "You are checked out", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
