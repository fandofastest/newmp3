package uiux.design.bottomnavigation.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uiux.design.bottomnavigation.fragments.PlaylistsFragment;
import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.fragments.HomeFragment;
import uiux.design.bottomnavigation.fragments.LocalFragment;
import uiux.design.bottomnavigation.fragments.SearchFragment;
import uiux.design.bottomnavigation.model.Song;
import uiux.design.bottomnavigation.model.SongOffline;
import uiux.design.bottomnavigation.utils.PlayerActivity;
import uiux.design.bottomnavigation.utils.PlayerService;
import uiux.design.bottomnavigation.utils.RealmHelper;
import uiux.design.bottomnavigation.utils.Tools;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

        private BottomNavigationView navigation;
        public View parent_view;
        TextView hometitle,homeartist;
        private ImageButton bt_play;
        private ProgressBar song_progressbar;
        private Handler mHandler = new Handler();
        private  LinearLayout linearLayout;
        Realm realm;
        RealmHelper realmHelper;
        private  MusicUtils utils;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            parent_view = findViewById(R.id.container);
            utils= new MusicUtils();

            initComponent();
            initrealm();
            buttonPlayerAction();

            if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)&& (!Settings.System.canWrite(this))) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);

            }


            if (PlayerService.PLAYERSTATUS.equals("PLAYING")){
                bt_play.setImageResource(R.drawable.ic_baseline_pause_24);
                hometitle.setText(PlayerService.currenttitle);
                homeartist.setText(PlayerService.currentartist);
            }
            else {
                bt_play.setImageResource(R.drawable.ic_play_arrow);
                hometitle.setText("No Song");
                homeartist.setText("");
            }


            loadFragment(new HomeFragment());

        }

        private boolean loadFragment(Fragment fragment) {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_container, fragment)
                        .commit();
                return true;
            }
            return false;
        }

        private void buttonPlayerAction() {
            bt_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // check for already playing
                    if (PlayerService.PLAYERSTATUS.equals("PLAYING")) {
                        pause();
                    } else {
                        resume();
                        // Resume song

                    }

                }
            });
        }
        public void getlocalbroadcaster(){
            LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onReceive(Context context, Intent intent) {
                    String status = intent.getStringExtra("status");
                    if (status.equals("playing")){

                        bt_play.setVisibility(View.VISIBLE);
                        bt_play.setImageResource(R.drawable.ic_baseline_pause_24);
                        hometitle.setText(PlayerService.currenttitle);
                        homeartist.setText(PlayerService.currentartist);
                        mHandler.post(mUpdateTimeTask);

                    }
                    else if (status.equals("pause")){
                        bt_play.setImageResource(R.drawable.ic_play_arrow);
                    }

                }
            }, new IntentFilter("musicplayer"));

        }
        public void pause (){
            bt_play.setImageResource(R.drawable.ic_play_arrow);
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



        public void controlClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.bt_expand: {
                    if (PlayerService.PLAYERSTATUS.equals("PLAYING")){
                        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                        intent.putExtra("from","player");
                        startActivity(intent);
                    }
                    else {
                        Snackbar.make(parent_view, "No Music", Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }

        private void initComponent() {
            hometitle=findViewById(R.id.titlehomeplayer);
            homeartist=findViewById(R.id.artishomeplayer);
            bt_play = (ImageButton) findViewById(R.id.bt_play);
            song_progressbar = (ProgressBar) findViewById(R.id.song_progressbar);
            linearLayout=findViewById(R.id.player_control);
            song_progressbar.setProgress(0);
            song_progressbar.setMax(MusicUtils.MAX_PROGRESS);
            getlocalbroadcaster();
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setBackgroundColor(getResources().getColor(R.color.green_800));
            navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_discover:
                            navigation.setBackgroundColor(getResources().getColor(R.color.green_800));
                            loadFragment(new HomeFragment());
                            return true;
                        case R.id.navigation_search:
                            navigation.setBackgroundColor(getResources().getColor(R.color.blue_800));
                            loadFragment(new SearchFragment());
                            Tools.setSystemBarColor(MainActivity.this, R.color.blue_800);
                            Tools.setSystemBarLight(MainActivity.this);
                            return true;
                        case R.id.navigation_music:
                            navigation.setBackgroundColor(getResources().getColor(R.color.pink_800));
                            loadFragment(new PlaylistsFragment());
                            Tools.setSystemBarColor(MainActivity.this, R.color.pink_800);
                            Tools.setSystemBarLight(MainActivity.this);
                            return true;
                        case R.id.navigation_local:
                            navigation.setBackgroundColor(getResources().getColor(R.color.orange_800));
                            loadFragment(new LocalFragment());
                            Tools.setSystemBarColor(MainActivity.this, R.color.orange_800);
                            Tools.setSystemBarLight(MainActivity.this);
                            return true;

                    }
                    return false;
                }
            });

            NestedScrollView nested_content = findViewById(R.id.nested_scroll_view);
            nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY < oldScrollY) { // up
                        animateNavigation(false);
                    }
                    if (scrollY > oldScrollY) { // down
                        animateNavigation(true);
                    }
                }
            });






            Tools.setSystemBarColor(this, R.color.green_800);
            Tools.setSystemBarLight(this);
        }


        boolean isNavigationHide = false;

        private void animateNavigation(final boolean hide) {
            if (isNavigationHide && hide || !isNavigationHide && !hide) return;
            isNavigationHide = hide;
            int moveY = hide ? (2 * navigation.getHeight()) : 0;
            navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
            linearLayout.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
        }

        public void  initrealm(){
            Realm.init(MainActivity.this);
            RealmConfiguration configuration = new RealmConfiguration.Builder().build();
            realm = Realm.getInstance(configuration);

        }

        public  void addtoplaylits(Song musicSongOnline){
            realmHelper = new RealmHelper(realm,getApplication());
            realmHelper.saveplaylists(musicSongOnline);
            Toast.makeText(getApplicationContext(),"Added to Playlists",LENGTH_LONG).show();


        }

        public List<Song> getrecent(){
            realmHelper = new RealmHelper(realm,getApplication());
            PlayerService.listrecent=  realmHelper.getAllSongsrecent();

            return  PlayerService.listrecent;

        }

        public List<Song> getplaylists(){
            realmHelper = new RealmHelper(realm,getApplication());
            PlayerService.listplaylist=  realmHelper.getallplaylists();

            return  PlayerService.listplaylist;

        }


        public  void removerecent(Song musicSongOnline){
            realmHelper = new RealmHelper(realm,getApplication());
            realmHelper.removefromrecent(musicSongOnline);
            Toast.makeText(getApplicationContext(),"Removed",LENGTH_LONG).show();


        }

        public boolean removeplaylists(Song musicSongOnline){
            realmHelper = new RealmHelper(realm,getApplication());
            realmHelper.removefromplaylists(musicSongOnline);
            Toast.makeText(getApplicationContext(),"Removed",LENGTH_LONG).show();

            return true  ;

        }

    public void playmusicoffline (int position ,List<SongOffline> listsong){

        PlayerService.currentlistoffline=listsong;


        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra("from","offline");
        intent.putExtra("pos",position);
        startActivity(intent);







    }


}
