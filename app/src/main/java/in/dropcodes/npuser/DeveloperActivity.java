package in.dropcodes.npuser;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcoscg.licenser.Library;
import com.marcoscg.licenser.License;
import com.marcoscg.licenser.LicenserDialog;

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

    public void licenses(View view) {

        new LicenserDialog(this)
                .setTitle("Licenses")
                .setCustomNoticeTitle("Notices for files:")
                .setLibrary(new Library("Android Support Libraries",
                        "https://developer.android.com/topic/libraries/support-library/index.html",
                        License.APACHE))
                .setLibrary(new Library("Picasso",
                        "http://square.github.io/picasso/",
                        License.APACHE))
                .setLibrary(new Library("Licenser",
                        "https://github.com/marcoscgdev/Licenser",
                        License.MIT))
                .setLibrary(new Library("CircleImageView","https://github.com/hdodenhof/CircleImageView",License.APACHE))
                .setLibrary(new Library("barcodescanner","https://github.com/dm77/barcodescanner",License.APACHE))
                .setLibrary(new Library("android-gif-drawable","https://github.com/koral--/android-gif-drawable",License.MIT))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       finish();
                    }
                })
                .show();

    }
}