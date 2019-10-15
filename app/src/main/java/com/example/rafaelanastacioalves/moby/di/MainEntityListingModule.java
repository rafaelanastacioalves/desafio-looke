package com.example.rafaelanastacioalves.moby.di;

import com.example.rafaelanastacioalves.moby.entitymainlisting.EntityMainModule;
import com.example.rafaelanastacioalves.moby.entitymainlisting.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainEntityListingModule {

    @ContributesAndroidInjector(modules = EntityMainModule.class)
    abstract MainActivity bindMainActivity();




}
