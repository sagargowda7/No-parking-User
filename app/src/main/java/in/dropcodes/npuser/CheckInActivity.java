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
    public String uid,child;
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    public String value;
    @Override
    protected void onStart() {
        super.onStart();


        Bundle bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        child = bundle.getString("child");
        Toast.makeText(this, child, Toast.LENGTH_LONG).show();

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

                    value = dataSnapshot.child(child).getValue().toString();
                    Toast.makeText(CheckInActivity.this, value, Toast.LENGTH_SHORT).show();

                    int vv =Integer.parseInt(value);
                    int vvv = vv - 1;
                    String fin = String.valueOf(vvv);
                    mReference.child(child).setValue(fin);
                    finish();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
