package com.example.rafaelanastacioalves.moby.retrofit;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.EntityDetails;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIClient {

    @GET("/v0/b/desafio-dev-android.appspot.com/o/assets.json")
    Call<MainEntity> getTripPackageList(@Query("alt")String alt, @Query("token")String token);

    @GET("{soundUrl}")
    Call<EntityDetails> getSound(@Path("soundUrl") String soundUrl);

    @GET("{mediaUrl}")
    Observable<ResponseBody> getMedia(@Path("mediaUrl") String url);
}
