package in.dropcodes.npuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public TextInputEditText mEmail,mPassword,mReEnterPassword;
    public Button mSignUp;
    public String email,password,rePassword;
    public FirebaseAuth mAuth;
    public DatabaseReference mReference;
    public ProgressDialog mProgress;
    public FirebaseDatabase mDtabase;
    public FirebaseUser mUser;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        getSupportActionBar().setTitle("Sign Up");

        //FireBase
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.sign_in_email_id);
        mPassword =findViewById(R.id.sign_in_passwowrd_id);
        mReEnterPassword = findViewById(R.id.sign_in_renter_paswrd_id);
        mSignUp = findViewById(R.id.sign_up_button);

        mProgress = new ProgressDialog(this);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                mProgress.setMessage("Authenticating");
                mProgress.setTitle("Creating new Account");
                mProgress.setCanceledOnTouchOutside(false);
                mProgress.setIcon(R.mipmap.ic_launcher);
                mProgress.show();

                email =mEmail.getEditableText().toString();
                password = mPassword.getEditableText().toString();
                rePassword = mReEnterPassword.getEditableText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(rePassword)){

                    if (password.matches(rePassword)){

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    mUser = mAuth.getCurrentUser();
                                    mDtabase = FirebaseDatabase.getInstance();
                                    uid = mUser.getUid();
                                    mReference = mDtabase.getReference().child("parking").child(uid);
                                    Map defaultData = new HashMap();
                                    defaultData.put("name","");
                                    defaultData.put("email",email);
                                    defaultData.put("uid",uid);
                                    mReference.updateChildren(defaultData);

                                    Intent signInIntent = new Intent(SignUpActivity.this, MainActivity.class);
                                    signInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(signInIntent);
                                    mProgress.dismiss();
                                    finish();
                                    Snackbar.make(view, "SignUp Successful", Snackbar.LENGTH_LONG).show();


                                }else {
                                    Snackbar.make(view,"SigUp Unsuccessful",Snackbar.LENGTH_LONG).show();
                                    mProgress.hide();
                                }

                            }
                        });

                    }else {
                        Snackbar.make(view,"Entered Password doesn't match",Snackbar.LENGTH_LONG).show();
                        mProgress.hide();
                    }

                }else {
                    Snackbar.make(view,"Please enter credential details",Snackbar.LENGTH_LONG).show();
                    mProgress.hide();
                }



            }
        });


    }

}
