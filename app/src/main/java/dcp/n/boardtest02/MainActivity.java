package dcp.n.boardtest02;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private Menu menu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_seek, R.id.navigation_user)
//                .build();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        setMenuUI(mAuth);
    }

    public void setNavigationUI() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, this.menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.btn_write_board: {
                startActivity(new Intent(this, WriteActivity.class));
            } break;
            case R.id.action_settings: {
//                startActivity(new Intent(this, SettingsActivity.class));
            } break;
        }
        return false;
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
    public void setMenuUI(FirebaseAuth mAuth) {
        findViewById(R.id.nav_view).setVisibility(View.GONE);
        findViewById(R.id.nav_view_guest).setVisibility(View.GONE);
        BottomNavigationView navView;
        AppBarConfiguration appBarConfiguration;
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        if(mAuth.getCurrentUser() != null) {
            navView = findViewById(R.id.nav_view);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_seek, R.id.navigation_user, R.id.navigation_settings)
                    .build();
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    switch (destination.getId()) {
                        case R.id.navigation_seek: {
                            menu.clear();
                            getMenuInflater().inflate(R.menu.menu_seek, menu);
                        } break;
                        default: {
                            if(menu != null) {
                                menu.clear();
                                getMenuInflater().inflate(R.menu.main, menu);
                            }
                        } break;
                    }
                }
            });
        }
        else {
            navView = findViewById(R.id.nav_view_guest);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_settings)
                    .build();
        }
        navView.setVisibility(View.VISIBLE);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}