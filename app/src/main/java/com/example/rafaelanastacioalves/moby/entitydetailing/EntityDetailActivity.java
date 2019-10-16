package com.example.rafaelanastacioalves.moby.entitydetailing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.rafaelanastacioalves.moby.R;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class EntityDetailActivity extends AppCompatActivity {

    @BindView(R.id.previous_music)
    AppCompatImageView previousButton;

    @BindView(R.id.next_music)
    AppCompatImageView nextButton;
    private int position;
    private MainEntity mainEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_package_detail);
        ButterKnife.bind(this);
        setupActionBar();
        setupListeners();
        if (savedInstanceState == null) {
            recoverVariables();

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            checkPermission();
        }else {
            showFragmentForListPosition(position);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recoverVariablesFromPreviousState(savedInstanceState);
    }

    private void recoverVariablesFromPreviousState(Bundle savedInstanceState) {
        position = savedInstanceState.getInt(EntityDetailsFragment.ARG_POSITION);
        mainEntity = (MainEntity) savedInstanceState.getSerializable(EntityDetailsFragment.MAIN_ENTITY);
    }

    private void setupListeners() {
        nextButton.setOnClickListener(v -> onClickNext());
        previousButton.setOnClickListener(v -> onClickPrevious() );
    }

    @Override
    protected void onStart() {
        super.onStart();
        showFragmentForListPosition(position);

    }

    private void recoverVariables() {
            this.position = getIntent().getIntExtra(EntityDetailsFragment.ARG_POSITION, 0);
            this.mainEntity = (MainEntity) getIntent().getSerializableExtra(EntityDetailsFragment.MAIN_ENTITY);


    }

    private void showFragmentForListPosition(int position) {
        Bundle arguments = new Bundle();
        arguments.putSerializable(EntityDetailsFragment.MAIN_ENTITY,
                mainEntity.getObjects().get(position));
        EntityDetailsFragment fragment = new EntityDetailsFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.package_detail_fragment_container, fragment)
                .commit();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

    }

    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        showFragmentForListPosition(position);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        finish();
                    }
                })
                .check();
    }

    private void onClickNext() {
        if (position + 1 < mainEntity.getObjects().size()){
            position++;
            showFragmentForListPosition(position);
        }else{
            Toast.makeText(this, "End Of List", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickPrevious() {
        if (position -1 > -1){
            position--;
            showFragmentForListPosition(position);
        }else{
            Toast.makeText(this, "End Of List", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EntityDetailsFragment.MAIN_ENTITY, mainEntity);
        outState.putInt(EntityDetailsFragment.ARG_POSITION, position);
    }
}
