package com.example.rafaelanastacioalves.moby.di;

import android.content.Context;

import com.example.rafaelanastacioalves.moby.application.MainApplication;
import com.example.rafaelanastacioalves.moby.repository.AppRepository;
import com.example.rafaelanastacioalves.moby.repository.MediaRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    Context provideContext(MainApplication application) {
        return application.getApplicationContext();
    }

    @Singleton
    @Provides
    AppRepository provideAppRepository(){
        return new AppRepository();
    }

    @Singleton
    @Provides
    MediaRepository provideMediaRepository(){
        return new MediaRepository();
    }

}
