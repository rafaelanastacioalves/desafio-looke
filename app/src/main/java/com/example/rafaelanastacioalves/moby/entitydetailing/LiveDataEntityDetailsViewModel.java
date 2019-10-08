package com.example.rafaelanastacioalves.moby.entitydetailing;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.example.rafaelanastacioalves.moby.domain.interactors.EntityDetailingInteractor;


public class LiveDataEntityDetailsViewModel extends ViewModel {

    private EntityDetailingInteractor mEntityDetailInteractor;
    private LiveData<Resource<MediaReference>> mEntityDetails;

    public LiveDataEntityDetailsViewModel(EntityDetailingInteractor mInteractor) {
        this.mEntityDetailInteractor = mInteractor;
    }

    public LiveData<Resource<MediaReference>> getEntityDetails(MainEntity.Objects objects) {
        EntityDetailingInteractor.RequestValues requestValues = new EntityDetailingInteractor.RequestValues(objects);
        mEntityDetails = mEntityDetailInteractor.execute(requestValues);
        return mEntityDetails;
    }


}

