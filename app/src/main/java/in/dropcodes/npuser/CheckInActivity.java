package in.dropcodes.npuser;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.widget.Toast.*;

public class CheckInActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public ZXingScannerView zXingScannerView;
    public String uid;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    public String value;
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


            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    value = dataSnapshot.child("parked").getValue().toString();
                    Toast.makeText(CheckInActivity.this, value, Toast.LENGTH_SHORT).show();

                    int vValue =Integer.parseInt(value);
                    int vFinal = vValue - 1;
                    String fin = String.valueOf(vFinal);
                    mReference.child("parked").setValue(fin);
                    finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(CheckInActivity.this,"There was some error ",Toast.LENGTH_LONG).show();

                }
            });

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
