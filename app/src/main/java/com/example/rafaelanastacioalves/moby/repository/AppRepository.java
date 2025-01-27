package com.example.rafaelanastacioalves.moby.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;

import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class AppRepository {

    public MutableLiveData<Resource<MainEntity>> getMainEntityList() {
        final APIClient apiClient = ServiceGenerator.buildService(APIClient.class);
        return new NetworkBoundResource<MainEntity, MainEntity>() {
            @Override
            protected void onFetchFailed() {

            }

            @Override
            protected Call<MainEntity> createCall() {
                return apiClient.getTripPackageList();
            }
        }.asLiveData();
    }

}



