package in.dropcodes.npuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInheckActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null){

            Intent i = new Intent(LogInheckActivity.this,ChooseActivity.class);
            startActivity(i);
            finish();
        }else{
            Intent ii =  new Intent(LogInheckActivity.this,MainActivity.class);
            startActivity(ii);
            finish();
        }
    }
}
