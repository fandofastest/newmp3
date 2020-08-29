package uiux.design.bottomnavigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import uiux.design.bottomnavigation.R;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    Animation animFadeIn;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent goToMain = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(goToMain);
                finish();

            }
        }, SPLASH_TIME_OUT);*/

        View decorView = getWindow().getDecorView();

        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.

        // load the animation
        animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_fade_in);

        // set animation listener
        animFadeIn.setAnimationListener(this);

        // animation for image
        relativeLayout = findViewById(R.id.splashLayout);

        // start the animation
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.startAnimation(animFadeIn);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    public void onAnimationEnd(Animation animation) {

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
