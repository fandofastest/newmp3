package uiux.design.bottomnavigation.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.fragments.GenreFragment;
import uiux.design.bottomnavigation.fragments.HomeFragment;
import uiux.design.bottomnavigation.fragments.LocalFragment;
import uiux.design.bottomnavigation.fragments.SearchFragment;
import uiux.design.bottomnavigation.utils.Tools;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();

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

    private void initComponent() {


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
                        return true;
                    case R.id.navigation_music:
                        navigation.setBackgroundColor(getResources().getColor(R.color.pink_800));
                        loadFragment(new GenreFragment());
                        return true;
                    case R.id.navigation_local:
                        navigation.setBackgroundColor(getResources().getColor(R.color.orange_800));
                        loadFragment(new LocalFragment());
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






        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }


    boolean isNavigationHide = false;

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }




}
