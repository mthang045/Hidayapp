package com.example.hidaymovie.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.hidaymovie.fragment.FavoritesFragment;
import com.example.hidaymovie.fragment.HomeFragment;
import com.example.hidaymovie.fragment.ProfileFragment;
import com.example.hidaymovie.fragment.SearchFragment;
import com.hidaymovie.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.menu_search) {
                selectedFragment = new SearchFragment();
            } else if (itemId == R.id.menu_favorites) {
                selectedFragment = new FavoritesFragment();
            } else if (itemId == R.id.menu_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Mặc định load HomeFragment khi bắt đầu
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
    