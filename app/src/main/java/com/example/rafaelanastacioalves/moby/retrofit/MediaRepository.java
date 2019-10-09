package com.example.rafaelanastacioalves.moby.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class MediaRepository {

    public Observable<ResponseBody> getMediaFromUrl(String url){
        final APIClient apiClient = ServiceGenerator.buildReactiveService(APIClient.class);

        Observable<ResponseBody> response = apiClient.getMedia(url);
        return response;
    }
}
