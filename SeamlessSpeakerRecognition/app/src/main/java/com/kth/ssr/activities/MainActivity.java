package com.kth.ssr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;

import com.kth.ssr.R;
import com.kth.ssr.broadcast.RecognitoBroadcastReceiver;
import com.kth.ssr.fragments.MainFragment;
import com.kth.ssr.fragments.interfaces.ChangeMainFragmentCallback;


public class MainActivity extends FragmentActivity implements ChangeMainFragmentCallback {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            MainFragment mainFragment = new MainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_holder, mainFragment, null);
            fragmentTransaction.commit();
        }

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(new RecognitoBroadcastReceiver(this), RecognitoBroadcastReceiver.createIntentFilter());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = ApplicationSettingsActivity.getLaunchIntent(this);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void changeMainFragment(Fragment fragment) {
        FragmentManager fragmentManager= getSupportFragmentManager();
        fragmentManager.popBackStack("init", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction().replace(R.id.content_holder, fragment, null).addToBackStack("init").commit();
    }
}
