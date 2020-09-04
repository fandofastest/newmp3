package uiux.design.bottomnavigation.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.activities.MusicUtils;

public class PlayerActivity extends  AppCompatActivity implements View.OnClickListener {

    private View parent_view;
    private ImageView bt_play;
    private ProgressBar song_progressbar,progressBarplay;
    TextView title,artist,totaldura,currendura;
    ImageView repeat,shuffle,nextbt,prevbt;
    ImageView imageView;


    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    String from;
    //private SongsManager songManager;
    private MusicUtils utils;
    int pos;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initComponent();
        from=getIntent().getStringExtra("from");
        parent_view=findViewById(R.id.parent);



        if (from.equals("online")){
            pos=getIntent().getIntExtra("pos",0);
            getlocalbroadcaster();
            Intent playerservice= new Intent(PlayerActivity.this, PlayerService.class);
            playerservice.putExtra("from",from);
            playerservice.putExtra("pos",pos);
            startService(playerservice);

        }

        else
        if (from.equals("offline")){
            pos=getIntent().getIntExtra("pos",0);
            getlocalbroadcaster();
            Intent playerservice= new Intent(PlayerActivity.this, PlayerService.class);
            playerservice.putExtra("from",from);

            playerservice.putExtra("pos",pos);
            startService(playerservice);

        }

        else if (from.equals("player")){

            bt_play.setVisibility(View.VISIBLE);
            bt_play.setImageResource(R.drawable.ic_baseline_pause_24);
            title.setText(PlayerService.currenttitle);
            artist.setText(PlayerService.currentartist);
            mHandler.post(mUpdateTimeTask);
            progressBarplay.setVisibility(View.GONE);
        }









    }



    private void initComponent() {

        bt_play = findViewById(R.id.playbutton);

        shuffle=findViewById(R.id.shuffle);
        repeat=findViewById(R.id.repeat);
        nextbt=findViewById(R.id.next);
        prevbt=findViewById(R.id.prev);

        shuffle.setOnClickListener(this);
        repeat.setOnClickListener(this);
        nextbt.setOnClickListener(this);
        prevbt.setOnClickListener(this);




        totaldura=findViewById(R.id.total);
        currendura=findViewById(R.id.current);
        imageView=findViewById(R.id.image);

        Tools.displayImageOriginal(PlayerActivity.this,imageView,PlayerService.currentimageurl);



        if (PlayerService.SHUFFLE.equals("OFF")){
            shuffle.setColorFilter(R.color.grey_700);
        }

        if (PlayerService.REPEAT.equals("OFF")){
            repeat.setColorFilter(R.color.grey_700);
        }
        song_progressbar = (ProgressBar) findViewById(R.id.progressplay);
        title=findViewById(R.id.title);
        artist=findViewById(R.id.artist);
        progressBarplay=findViewById(R.id.progressplay);

        // set Progress bar values
        song_progressbar.setProgress(0);
        song_progressbar.setMax(MusicUtils.MAX_PROGRESS);



        utils = new MusicUtils();

        buttonPlayerAction();
    }

    /**
     * Play button click event plays a song and changes button to pause image
     * pauses a song and changes button to play image
     */
    private void buttonPlayerAction() {
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (PlayerService.PLAYERSTATUS.equals("PLAYING")) {
                    pause();
                } else {
                    // Resume song
                    resume();

                }

            }
        });
    }



    private boolean toggleButtonColor(ImageButton bt) {
        String selected = (String) bt.getTag(bt.getId());
        if (selected != null) { // selected
            bt.setColorFilter(getResources().getColor(R.color.red_500), PorterDuff.Mode.SRC_ATOP);
            bt.setTag(bt.getId(), null);
            return false;
        } else {
            bt.setTag(bt.getId(), "selected");
            bt.setColorFilter(getResources().getColor(R.color.red_500), PorterDuff.Mode.SRC_ATOP);
            return true;
        }
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            updateTimerAndSeekbar();
            // Running this thread after 10 milliseconds
            if (PlayerService.PLAYERSTATUS.equals("PLAYING")) {
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void updateTimerAndSeekbar() {
        Intent intent = new Intent("musicplayer");
        intent.putExtra("status", "getduration");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        currendura.setText(utils.milliSecondsToTimer(PlayerService.currentduraiton));
        totaldura.setText(utils.milliSecondsToTimer(PlayerService.totalduration));
        // Updating progress bar
        int progress = (int) (utils.getProgressSeekBar(PlayerService.currentduraiton, PlayerService.totalduration));
        song_progressbar.setProgress(progress);
    }

    // stop player when destroy
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_setting_round, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.colorPrimary));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }



    public void getlocalbroadcaster(){
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onReceive(Context context, Intent intent) {
                String status = intent.getStringExtra("status");
                if (status.equals("playing")){
                    progressBarplay.setVisibility(View.GONE);
                    bt_play.setVisibility(View.VISIBLE);
                    bt_play.setImageResource(R.drawable.ic_baseline_pause_24);
                    title.setText(PlayerService.currenttitle);
                    artist.setText(PlayerService.currentartist);
                    mHandler.post(mUpdateTimeTask);
                    Tools.displayImageOriginal(PlayerActivity.this,imageView,PlayerService.currentimageurl);

                }
                else if (status.equals("stoping")){

                }

            }
        }, new IntentFilter("musicplayer"));

    }

    public void  repeat(){
        if (PlayerService.REPEAT.equals("OFF")){

            PlayerService.REPEAT="ON";
            repeat.clearColorFilter();

        }

        else {
            PlayerService.REPEAT="OFF";
            repeat.setColorFilter(R.color.grey_700);

        }



        Snackbar.make(parent_view, "Repeat" +PlayerService.REPEAT, Snackbar.LENGTH_SHORT).show();
    }

    public void shuffle(){
        if (PlayerService.SHUFFLE.equals("OFF")){

            PlayerService.SHUFFLE="ON";
            shuffle.clearColorFilter();
        }

        else {
            PlayerService.SHUFFLE="OFF";
            shuffle.setColorFilter(R.color.grey_700);
        }



        Snackbar.make(parent_view, "Shuffle" +PlayerService.SHUFFLE, Snackbar.LENGTH_SHORT).show();
    }

    public void pause (){
        bt_play.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        Intent intent = new Intent("musicplayer");
        intent.putExtra("status", "pause");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }

    public void resume (){
        bt_play.setImageResource(R.drawable.ic_baseline_pause_24);
        Intent intent = new Intent("musicplayer");
        intent.putExtra("status", "resume");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }
    public void next (){
        bt_play.setVisibility(View.GONE);
        progressBarplay.setVisibility(View.VISIBLE);
        Snackbar.make(parent_view, "Next", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent("musicplayer");
        intent.putExtra("status", "next");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }
    public void prev (){

        progressBarplay.setVisibility(View.VISIBLE);
        bt_play.setVisibility(View.GONE);
        Snackbar.make(parent_view, "Previous", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent("musicplayer");
        intent.putExtra("status", "prev");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        mHandler.post(mUpdateTimeTask);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.next:
                next();
                break;
            case R.id.prev:
                prev();
                break;
            case R.id.shuffle:
                shuffle();
                break;
            case R.id.repeat:
                repeat();
                break;
            default:
                break;
        }
    }
}

