package com.example.rafaelanastacioalves.moby.repository;

import com.example.rafaelanastacioalves.moby.common.MediaReferenceHelper;

import java.io.File;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.internal.operators.observable.ObservablePublish;
import okhttp3.ResponseBody;

public class MediaRepository {

    public Observable<File> getMediaFrom(String url, String saveName){
        return Observable.create(emitter -> {
            final APIClient apiClient = ServiceGenerator.buildReactiveService(APIClient.class);
            ResponseBody responseBody = apiClient.getMedia(url).execute().body();

            try {
            }catch (Exception e){
                emitter.onError(e);
            }
            if (responseBody!=null){
                emitter.onNext(MediaReferenceHelper.persistMedia(responseBody, saveName));
            }else {
                emitter.onError(new NullPointerException("Response Body null"));
            }

        });
    }
}
