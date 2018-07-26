package in.dropcodes.npuser;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DeveloperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        getSupportActionBar().setTitle("About Developer");
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
}