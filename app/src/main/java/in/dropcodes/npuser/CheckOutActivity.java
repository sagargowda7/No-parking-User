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
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckOutActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    public ZXingScannerView ScannerView;
    public String uid , child;
    public FirebaseDatabase mDatabaseR;
    public DatabaseReference mReferenceR;
    public String values;


    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundl = getIntent().getExtras();
        uid = bundl.getString("uid");
        child = bundl.getString("child");
        Toast.makeText(this, child, Toast.LENGTH_LONG).show();

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


            mReferenceR.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    values = dataSnapshot.child(child).getValue().toString();
                    Toast.makeText(CheckOutActivity.this, values, Toast.LENGTH_SHORT).show();

                    int v =Integer.parseInt(values);
                    int vFinal = v + 1;
                    String fin = String.valueOf(vFinal);
                    mReferenceR.child(child).setValue(fin);
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
