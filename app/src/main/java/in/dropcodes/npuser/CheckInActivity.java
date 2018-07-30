package in.dropcodes.npuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.widget.Toast.*;

public class CheckInActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public ZXingScannerView zXingScannerView;
    public String uid;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference,mUserRef;
    public String value,UserUID;
    public FirebaseAuth mAuth;
    public FirebaseUser mUser;
    @Override
    protected void onStart() {
        super.onStart();


        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");

        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        zXingScannerView.resumeCameraPreview(this);
        final String UID = result.getText();

        if (UID.equals(uid)){

            mDatabase = FirebaseDatabase.getInstance();
            mReference = mDatabase.getReference().child("parking").child(uid);
            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            UserUID = mUser.getUid();
            mUserRef = mReference.child("record").child(UserUID);

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
            int totalTime = minInt+ HrsToMin;
            mUserRef.child("check_in_time").setValue(totalTime);

            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    value = dataSnapshot.child("parked").getValue().toString();
                    int vValue =Integer.parseInt(value);
                    int vFinal = vValue - 1;
                    String fin = String.valueOf(vFinal);
                    mReference.child("parked").setValue(fin);
                    Toast.makeText(CheckInActivity.this, "You are checked in", Toast.LENGTH_SHORT).show();
                    finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(CheckInActivity.this,"There was some error ",Toast.LENGTH_LONG).show();

                }
            });

            Intent intent =new Intent(CheckInActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }else {


            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckInActivity.this);
            LayoutInflater inflater = LayoutInflater.from(CheckInActivity.this);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
