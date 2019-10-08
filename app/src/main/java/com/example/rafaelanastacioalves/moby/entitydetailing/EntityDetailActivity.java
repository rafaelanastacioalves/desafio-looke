package com.example.rafaelanastacioalves.moby.entitydetailing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.rafaelanastacioalves.moby.R;

import dagger.android.AndroidInjection;
import timber.log.Timber;


public class EntityDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_package_detail);
        setupActionBar();
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Timber.i("PullRequestsFragment ARG PACKAGE: " + getIntent().getStringExtra(EntityDetailsFragment.ARG_OBJECTS));
            Bundle arguments = new Bundle();
            arguments.putString(EntityDetailsFragment.ARG_OBJECTS,
                    getIntent().getStringExtra(EntityDetailsFragment.ARG_OBJECTS));
            EntityDetailsFragment fragment = new EntityDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.package_detail_fragment_container, fragment)
                    .commit();
            supportPostponeEnterTransition();
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

    }

}
