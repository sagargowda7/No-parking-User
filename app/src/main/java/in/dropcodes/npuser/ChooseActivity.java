package in.dropcodes.npuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseActivity extends AppCompatActivity {

    private Button mSignIn , mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        getSupportActionBar().setTitle("No Parking");

        mSignIn = findViewById(R.id.sign_in_btn);
        mSignUp = findViewById(R.id.sign_up_btn);


        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(ChooseActivity.this,SignInActivity.class);
                startActivity(signIn);
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp =  new Intent(ChooseActivity.this,SignUpActivity.class);
                startActivity(signUp);
            }
        });

    }
}
