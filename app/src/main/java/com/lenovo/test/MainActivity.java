package com.lenovo.test;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationView mBottomNav;
    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }

        selectFragment(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = mBottomNav.getMenu().getItem(0);

        if (mSelectedItem != homeItem.getItemId()) {
            // select home item
            // homeItem.setCheckable(true);

            Log.d("backpressids", "****  " + mSelectedItem);
            selectFragment(homeItem);
            mBottomNav.setSelectedItemId(homeItem.getItemId());

        } else {

            super.onBackPressed();

        }
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;
        item.setCheckable(true);
        // init corresponding fragment
        switch (item.getItemId()) {
            case R.id.menu_rides:

                FragmentRides fragmentone = new FragmentRides();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragmentone);
                ft.commit();


                break;
            case R.id.menu_search:


                FragmentSearch fragmenttwo = new FragmentSearch();
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.container, fragmenttwo);
                ft2.commit();
                break;
            case R.id.menu_offer:
                FragmentOffer fragmentOffer = new FragmentOffer();
                FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.container, fragmentOffer);
                ft3.commit();




                break;

//            case R.id.menu_inbox:
//                FragmentInbox fragment = new FragmentInbox();
//                FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
//                ft4.replace(R.id.container, fragment);
//                ft4.commit();
//
//                break;

            case R.id.menu_profile:
                FragmentProfile fra = new FragmentProfile();
                FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                ft5.replace(R.id.container, fra);
                ft5.commit();

                break;
        }

        // update selected item
        mSelectedItem = item.getItemId();


        updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentRides fragmentone = new FragmentRides();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragmentone);
            ft.commit();
        }
    }

    private void updateToolbarText(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }


}


