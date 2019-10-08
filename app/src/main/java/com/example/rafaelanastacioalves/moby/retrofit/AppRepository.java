package com.example.rafaelanastacioalves.moby.retrofit;

import android.arch.lifecycle.LiveData;

import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;

import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class AppRepository {

    public LiveData<Resource<MainEntity>> getMainEntityList() {
        final APIClient apiClient = ServiceGenerator.createService(APIClient.class);
        return new NetworkBoundResource<MainEntity, MainEntity>() {
            @Override
            protected void onFetchFailed() {

            }

            @Override
            protected Call<MainEntity> createCall() {
                return apiClient.getTripPackageList("media","964a35bb-53d0-45aa-a3dd-ecad72a2f14c" );
            }
        }.asLiveData();
    }

}



