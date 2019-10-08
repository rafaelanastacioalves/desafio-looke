package com.example.rafaelanastacioalves.moby.domain.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;
import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.example.rafaelanastacioalves.moby.retrofit.AppRepository;
import com.example.rafaelanastacioalves.moby.retrofit.MediaRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class EntityDetailingInteractor implements Interactor<EntityDetailingInteractor.RequestValues> {

    private final MediaRepository appRepository;

    @Inject
    EntityDetailingInteractor(MediaRepository appRepository){
        this.appRepository = appRepository;
    }

    @Override
    public LiveData<Resource<MediaReference>> execute(RequestValues requestValues) {
        MutableLiveData<Resource<MediaReference>> resourceLiveData = new MutableLiveData();


        Observable<MediaReference> mediaReferenceObservable = Observable.combineLatest(
                appRepository.getMediaFromUrl(requestValues.objects.getSg()).subscribeOn(Schedulers.io()),
                appRepository.getMediaFromUrl(requestValues.objects.getBg()).subscribeOn(Schedulers.io()),
                        (audio, video) -> {
                            return new MediaReference(audio,video);
                        });

        mediaReferenceObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MediaReference>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MediaReference mediaReference) {
                        persistAudio(mediaReference.audioResponse);
                        persistVideo(mediaReference.videoResponse);
                        resourceLiveData.postValue(Resource.success(mediaReference));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        // aqui podemos aplicar transformações baseadas em regras de negócio usando
        // Transformations. Ex.: Transformations.map()

        return resourceLiveData;
    }

    private void persistVideo(ResponseBody videoResponse) {

    }

    private void persistAudio(ResponseBody audioResponse) {

    }

    public static final class RequestValues implements Interactor.RequestValues {
        private final MainEntity.Objects objects;

        public RequestValues(MainEntity.Objects objects) {
            this.objects = objects;
        }

        public MainEntity.Objects getObjects() {
            return objects;
        }
    }


}
