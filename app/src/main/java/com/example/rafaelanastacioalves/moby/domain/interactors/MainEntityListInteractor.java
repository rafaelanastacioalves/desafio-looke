package com.example.rafaelanastacioalves.moby.domain.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.example.rafaelanastacioalves.moby.repository.AppRepository;

import javax.inject.Inject;

public class MainEntityListInteractor implements Interactor<MainEntityListInteractor.RequestValues>{

    private final AppRepository appRepository;

    @Inject
    MainEntityListInteractor(AppRepository appRepository){
        this.appRepository = appRepository;
    }
    @Override
    public LiveData<Resource<MainEntity>> execute(RequestValues requestValues) {
        MutableLiveData<Resource<MainEntity>> repositoryLiveData = (MutableLiveData<Resource<MainEntity>>) appRepository.getMainEntityList();
        repositoryLiveData.postValue(Resource.loading(null));
        // aqui podemos aplicar transformações baseadas em regras de negócio usando
        // Transformations. Ex.: Transformations.map()
        Transformations.map(repositoryLiveData, resource -> {

            switch (resource.status){
                case GENERIC_ERROR:
                    resource.message = "Main entity list error";
            }

            return resource;

        });
        return repositoryLiveData ;
    }

    public static class RequestValues implements Interactor.RequestValues {
        // in this case we don't need nothing for this use case
    }
}
