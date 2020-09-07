package uiux.design.bottomnavigation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.utils.Ads;
import uiux.design.bottomnavigation.utils.Constants;

public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {

    Animation animFadeIn;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent goToMain = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(goToMain);
//                finish();
//
//            }
//        }, 3000);

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

       getKey();
       getStatusApp(Constants.getUrlstatus());
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public void getKey(){
        String url="https://fando.id/soundcloud/getapi.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {

                    Constants.setKey(response.replaceAll("^\"|\"$", ""));

                    // Display the first 500 characters of the response string.

                }, error -> {

        });



        Volley.newRequestQueue(SplashActivity.this).add(stringRequest);


    }


    private void getStatusApp(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, response -> {


            try {
                Constants.setBanner(response.getString("banner"));
                Constants.setInter(response.getString("inter"));
                Constants.setStatusapp(response.getString("statusapp"));
                Constants.setAppupdate(response.getString("appupdate"));
                Constants.setAds(response.getString("ads"));
                Constants.setBannerfan(response.getString("fanbanner"));
                Constants.setInterfan(response.getString("faninter"));
                Constants.setAppid(response.getString("appid"));
                Constants.setStatususer(response.getString("statususer"));


                if (Constants.getStatusapp().equals("suspend")){

                    showDialog(Constants.getAppupdate());

                }
                else {
                    Ads ads = new Ads();
                    ads.dualadsinter(SplashActivity.this,Constants.getInter(),Constants.getInterfan());
                    ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                        @Override
                        public void onAdsfinish() {
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onRewardOk() {

                        }
                    });
                }












            } catch (JSONException e) {
                Log.e("errr",e.getMessage());
            }


        }, error -> {


            System.out.println("errrrrr"+error.getMessage());

        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }




    private void  showDialog(String appupdate){
        new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("App Was Discontinue")
                .setContentText("Please Install Our New Music App")
                .setConfirmText("Install")

                .setConfirmClickListener(sDialog -> {
                    sDialog
                            .setTitleText("Install From Playstore")
                            .setContentText("Please Wait, Open Playstore")
                            .setConfirmText("Go")


                            .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(
                                "https://play.google.com/store/apps/details?id="+appupdate));
                        intent.setPackage("com.android.vending");
                        startActivity(intent);
//                                Do something after 100ms
                    }, 3000);



                })
                .show();
    }
}
