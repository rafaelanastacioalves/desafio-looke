package com.example.rafaelanastacioalves.moby.domain.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Environment;
import android.util.Log;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.example.rafaelanastacioalves.moby.repository.MediaRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

public class EntityDetailingInteractor implements Interactor<EntityDetailingInteractor.RequestValues> {

    private final MediaRepository appRepository;

    @Inject
    EntityDetailingInteractor(MediaRepository appRepository){
        this.appRepository = appRepository;
    }

    @Override
    public LiveData<Resource<MediaReference>> execute(RequestValues requestValues) {
        MutableLiveData<Resource<MediaReference>> resourceLiveData = new MutableLiveData();

        resourceLiveData.postValue(Resource.loading(null));
        Observable<MediaReference> mediaReferenceObservable = Observable.combineLatest(
                appRepository.getMediaFrom(changeToHttp(requestValues.objects.getSg()),
                        requestValues.objects.getName().trim() + "-" + "audio.mp3")
                        .subscribeOn(Schedulers.newThread()),
                appRepository.getMediaFrom(changeToHttp(requestValues.objects.getBg()),
                        requestValues.objects.getName().trim()+ "-" + "video.mp4")
                        .subscribeOn(Schedulers.newThread()),
                        (audioFile, videoFile) -> {
                            return new MediaReference(audioFile,videoFile);
                        });

        mediaReferenceObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MediaReference>() {
                    private MediaReference mediaReference;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MediaReference mediaReference) {
                        this.mediaReference = mediaReference;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (mediaReference != null) {
                           resourceLiveData.postValue(Resource.success(mediaReference));
                        }
                    }
                });

        // aqui podemos aplicar transformações baseadas em regras de negócio usando
        // Transformations. Ex.: Transformations.map()

        return resourceLiveData;
    }

    private String changeToHttp(String url) {
        return url.replace("https","http");
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
