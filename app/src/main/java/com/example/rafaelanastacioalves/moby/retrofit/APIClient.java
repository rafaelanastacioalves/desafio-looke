package com.example.rafaelanastacioalves.moby.retrofit;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIClient {

    @GET("/looke/assets.json")
    Call<MainEntity> getTripPackageList();

    @GET("{soundUrl}")
    Call<EntityDetails> getSound(@Path("soundUrl") String soundUrl);

    @GET
    Observable<ResponseBody> getMedia(@Url String url);
}
