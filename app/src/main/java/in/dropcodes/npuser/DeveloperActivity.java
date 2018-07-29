package in.dropcodes.npuser;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeveloperActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;
    public FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        getSupportActionBar().setTitle("About Developer");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void instagram(View view) {
        Uri instaUri = Uri.parse("https://www.instagram.com/drop_codes/");
        Intent insta = new Intent(Intent.ACTION_VIEW, instaUri);
        insta.setPackage("com.instagram.android");
        try {
            startActivity(insta);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW));
            Uri.parse("https://www.instagram.com/drop_codes/");
        }
    }

    public void facebook(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.facebook.com/DropCodes"));
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        Intent in = new Intent(DeveloperActivity.this,LogInheckActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
        finish();
    }
}