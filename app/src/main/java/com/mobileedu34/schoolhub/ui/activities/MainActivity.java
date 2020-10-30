package com.mobileedu34.schoolhub.ui.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.helpers.UserRole;
import com.mobileedu34.schoolhub.preferences.AppPreferences;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static UserRole USER_ROLE = null;
    private NavigationView navigationView;
    private NavGraph navGraph;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar_layout);
        setSupportActionBar(toolbar);
        appBarLayout.setTargetElevation(0f);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navGraph = navController.getNavInflater().inflate(R.navigation.navigation_graph);

        setupDrawerItemsVisibility();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_lecturers, R.id.nav_students,
                R.id.nav_classrooms, R.id.nav_lecturer_students,
                R.id.nav_lecturer_classroom, R.id.nav_student_lectures,
                R.id.nav_account, R.id.nav_settings,
                R.id.nav_help_feedback, R.id.nav_lecturer_lectures,
                R.id.nav_classrooms)
                .setDrawerLayout(drawer)
                .build();



        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setupDrawerItemsVisibility() {
        int role = new AppPreferences(this).getUserRole();
        switch (role) {
            case 0:
                USER_ROLE = UserRole.STUDENT;
                navGraph.setStartDestination(R.id.nav_student_lectures);
                navigationView.getMenu().setGroupVisible(R.id.manager_section, false);
                navigationView.getMenu().setGroupVisible(R.id.lecturer_section, false);
                break;
            case 1:
                USER_ROLE = UserRole.LECTURER;
                navGraph.setStartDestination(R.id.nav_lecturer_students);
                navigationView.getMenu().setGroupVisible(R.id.manager_section, false);
                navigationView.getMenu().setGroupVisible(R.id.student_section, false);
                break;
            case 2:
                USER_ROLE = UserRole.SCHOOL_MANAGER;
                navGraph.setStartDestination(R.id.nav_lecturers);
                navigationView.getMenu().setGroupVisible(R.id.lecturer_section, false);
                navigationView.getMenu().setGroupVisible(R.id.student_section, false);
                break;
        }
        navController.setGraph(navGraph);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}