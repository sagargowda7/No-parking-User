package in.dropcodes.npuser;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    public TextInputEditText mEmail,mPassword;
    public Button mLogIn, mReset;
    public String email,password;
    public FirebaseAuth mAuth;
    public ProgressDialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportActionBar().setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.log_in_email_id);
        mPassword =findViewById(R.id.log_in_password_id);
        mLogIn =findViewById(R.id.logIn_btn);
        mReset = findViewById(R.id.forgot_password_btn);

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SignInActivity.this);
                alertDialog.setTitle("FORGOT PASSWORD");
                alertDialog.setMessage("Please enter your Email Id");
                LayoutInflater inflater = LayoutInflater.from(SignInActivity.this);
                View forgot_password_layout = inflater.inflate(R.layout.forgot_password_dialog,null);
                final TextInputEditText forgit_email = forgot_password_layout.findViewById(R.id.forgot_password_edittxt);
                alertDialog.setView(forgot_password_layout);

                alertDialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final ProgressDialog progresss = new ProgressDialog(SignInActivity.this);
                        progresss.setTitle("Sending reset request");
                        progresss.setMessage("Please wait...");
                        progresss.setCanceledOnTouchOutside(false);
                        progresss.show();

                        String reset_emeil_id = forgit_email.getEditableText().toString();


                        if(!TextUtils.isEmpty(reset_emeil_id)){
                            mAuth.sendPasswordResetEmail(reset_emeil_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        dialogInterface.dismiss();
                                        progresss.dismiss();
                                        Snackbar.make(view, "Reset password link has been sent to your Email Id", Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Snackbar.make(view, "There was some Error Please try again", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar.make(view, "Email Id doesn't exist", Snackbar.LENGTH_LONG).show();

                                }
                            });
                        }else {
                            Snackbar.make(view,"Please enter Email Id ",Snackbar.LENGTH_LONG).show();
                            progresss.dismiss();
                        }
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });

        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                mdialog = new ProgressDialog(SignInActivity.this);
                mdialog.setTitle("Signing In");
                mdialog.setMessage("Please wait....");
                mdialog.setCanceledOnTouchOutside(false);
                mdialog.setIcon(R.mipmap.ic_launcher);
                mdialog.show();;
                email = mEmail.getEditableText().toString();
                password = mPassword.getEditableText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){


                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){

                                Intent LogInIntent = new Intent(SignInActivity.this,MainActivity.class);
                                LogInIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(LogInIntent);
                                finish();
                                mdialog.dismiss();
                                Snackbar.make(view,"LogIn Successful",Snackbar.LENGTH_LONG).show();
                            }else
                            {
                                mdialog.hide();
                                Snackbar.make(view,"LogIn Unsuccessful",Snackbar.LENGTH_LONG).show();
                            }

                        }
                    });

                }else {
                    mdialog.hide();
                    Snackbar.make(view,"Please enter credential details",Snackbar.LENGTH_LONG).show();                }


            }
        });


    }
}