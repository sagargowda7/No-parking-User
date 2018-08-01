package in.dropcodes.npuser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
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

        if (!isConnected(ChooseActivity.this)) builderDialog(ChooseActivity.this).show();
        else {


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
}
