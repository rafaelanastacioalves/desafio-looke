package com.example.rafaelanastacioalves.moby.retrofit;

import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MediaRepository {

    public Observable<ResponseBody> getMediaFromUrl(String url){
        final APIClient apiClient = ServiceGenerator.createService(APIClient.class);

        Observable<ResponseBody> response = apiClient.getMedia(url);
        return response;
    }
}
