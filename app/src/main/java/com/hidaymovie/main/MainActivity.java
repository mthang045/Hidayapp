package com.hidaymovie.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.hidaymovie.fragment.FavoritesFragment;
import com.hidaymovie.fragment.HomeFragment;
import com.hidaymovie.fragment.ProfileFragment;
import com.hidaymovie.fragment.SearchFragment;
import com.hidaymovie.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // Khởi tạo các fragment một lần duy nhất và lưu trữ chúng
    final Fragment homeFragment = new HomeFragment();
    final Fragment searchFragment = new SearchFragment();
    final Fragment favoritesFragment = new FavoritesFragment();
    final Fragment profileFragment = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment activeFragment = homeFragment; // Fragment đang hiển thị

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Thêm tất cả các fragment vào container, ẩn đi và chỉ hiện HomeFragment
        // Dùng tag để FragmentManager có thể tìm lại chúng sau này nếu cần
        fm.beginTransaction().add(R.id.fragment_container, profileFragment, "4").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, favoritesFragment, "3").hide(favoritesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, searchFragment, "2").hide(searchFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment targetFragment = null;

            if (itemId == R.id.menu_home) {
                targetFragment = homeFragment;
            } else if (itemId == R.id.menu_search) {
                targetFragment = searchFragment;
            } else if (itemId == R.id.menu_favorites) {
                targetFragment = favoritesFragment;
            } else if (itemId == R.id.menu_profile) {
                targetFragment = profileFragment;
            }

            if (targetFragment != null) {
                // Ẩn fragment hiện tại và hiển thị fragment được chọn
                fm.beginTransaction().hide(activeFragment).show(targetFragment).commit();
                activeFragment = targetFragment;
            }

            return true;
        });

        // Đặt mục được chọn mặc định nếu cần (dù logic ở trên đã xử lý)
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.menu_home);
        }
    }
}