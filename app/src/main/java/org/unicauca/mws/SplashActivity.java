package org.unicauca.mws;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

public class SplashActivity extends Activity {
    private Handler handler;
    private Runnable runnable;
    private static final long SPLASH_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        runnable = new Runnable() {

            @Override
            public void run() {
                finish();
                selectActivity();

            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, SPLASH_TIME);
    }

    private void selectActivity() {
        Intent main = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main);


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            Log.i("SplashActivity", "Call removed from queue");
        }
    }

}
