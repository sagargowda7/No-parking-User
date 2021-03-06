package in.dropcodes.npuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread.sleep(2500);
                    Intent intent = new Intent(SplashActivity.this,LogInheckActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });thread.start();


    }
}
