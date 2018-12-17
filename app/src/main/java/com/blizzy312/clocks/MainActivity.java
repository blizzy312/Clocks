package com.blizzy312.clocks;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.blizzy312.clocks.Fragments.FragmentAlarmClock;
import com.blizzy312.clocks.Fragments.FragmentClock;
import com.blizzy312.clocks.Fragments.FragmentSetting;
import com.blizzy312.clocks.Fragments.FragmentStopWatch;
import com.blizzy312.clocks.Fragments.FragmentTimer;


public class MainActivity extends AppCompatActivity {


    Fragment clockScreen = null;
    Fragment alarmScreen = null;
    Fragment timerScreen = null;
    Fragment stopWatchScreen = null;
    Fragment settingScreen = null;

    private boolean isRunning;
    private View decorView;
    // 'show' and 'hide' represent amount of height that will be hided, 170 was chosen abstractly
    private int show = 0;
    private int hide = 170;
    // amount of time after which bottom navigation bar will hide again, works only on 'Main Clock' screen
    private int delay = 5000;
    // custom size of icons for bottom navigation bar
    private int navigationIconsSize = 40;

    private int currentScreen = 1;
    BottomNavigationView bottomNavigationBar;

    private boolean touched = false;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Remove notification bar */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        setContentView(R.layout.activity_main);
        /* ------ Hide additional system bars ------ */
        hideSystemUI();

        bottomNavigationBar = findViewById(R.id.navigation);
        bottomNavigationBar.setOnNavigationItemSelectedListener(navListener);
        hideNav();
        loadFragment(new FragmentClock());
        decorView = getWindow().getDecorView();
        /*decorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;//always return true to consume event
            }
        });*/
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            Log.d("bars", "1st");
//                            hideNav();
                            waitToHideSoftKeysBottomBar();
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                            Log.d("bars", "2nd");
                        }
                    }
                });
        // Resize bottom navigation icons
//        resizeBottomNavigationIcons(navigationIconsSize);


    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_clock:
                    if(clockScreen == null){
                        clockScreen = new FragmentClock();
                    }
                    loadFragment(clockScreen);
//                    replaceFragment(R.id.frame_container,fragment);
                    setCurrentScreen(1);
                    hideNav();
                    waitTime();
                    return true;
                case R.id.navigation_alarm_clock:
                    if(alarmScreen == null){
                        alarmScreen = new FragmentAlarmClock();
                    }
//                    fragment = new FragmentAlarmClock();
                    loadFragment(alarmScreen);
//                    replaceFragment(R.id.frame_container,fragment);
                    setCurrentScreen(2);
                    return true;
                case R.id.navigation_timer:
                    if(timerScreen == null){
                        timerScreen = new FragmentTimer();
                    }
//                    fragment = new FragmentTimer();
                    loadFragment(timerScreen);
//                    replaceFragment(R.id.frame_container,fragment);
                    setCurrentScreen(3);
                    return true;
                case R.id.navigation_stop_watch:
                    if(stopWatchScreen == null){
                        stopWatchScreen = new FragmentStopWatch();
                    }
//                    fragment = new FragmentStopWatch();
                    loadFragment(stopWatchScreen);
//                    replaceFragment(R.id.frame_container,fragment);
                    setCurrentScreen(4);
                    return true;
                case R.id.navigation_setting:
                    if(settingScreen == null){
                        settingScreen = new FragmentSetting();
                    }
//                    fragment = new FragmentSetting();
                    loadFragment(settingScreen);
//                    replaceFragment(R.id.frame_container,fragment);
                    setCurrentScreen(5);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public int getCurrentScreen(){
        return currentScreen;
    }

    public void setCurrentScreen(int currentScreen){
        this.currentScreen = currentScreen;
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void waitToHideSoftKeysBottomBar(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSystemUI();
            }
        }, delay);
    }
    /* ------ Bottom Navigation configuration functions ------ */
    public void hideNav(){
        bottomNavigationBar.animate().translationY(hide);
    }
    public void showNav(){
        bottomNavigationBar.animate().translationY(show);
    }
    public void waitTime(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getCurrentScreen() == 1){
                    hideNav();
                }
            }
        }, delay);
    }
    private void resizeBottomNavigationIcons(int size){
//        BottomNavigationView bottomNavigationView = (BottomNavigationView) decorView.findViewById(R.id.navigation);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationBar.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            // set your height here
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, displayMetrics);
            // set your width here
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    // test
    /*@Override
    protected void onPostResume() {
        super.onPostResume();

        while (!deferredFragmentTransactions.isEmpty()) {
            deferredFragmentTransactions.remove().commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    public void replaceFragment(int contentFrameId, android.support.v4.app.Fragment replacingFragment) {
        if (!isRunning) {
            DeferredFragmentTransaction deferredFragmentTransaction = new DeferredFragmentTransaction() {
                @Override
                public void commit() {
                    replaceFragmentInternal(getContentFrameId(), getReplacingFragment());
                }
            };

            deferredFragmentTransaction.setContentFrameId(contentFrameId);
            deferredFragmentTransaction.setReplacingFragment(replacingFragment);

            deferredFragmentTransactions.add(deferredFragmentTransaction);
        } else {
            replaceFragmentInternal(contentFrameId, replacingFragment);
        }
    }

    private void replaceFragmentInternal(int contentFrameId, Fragment replacingFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(contentFrameId, replacingFragment)
                .commit();
    }*/
}
