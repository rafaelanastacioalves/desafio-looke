package com.example.rafaelanastacioalves.moby.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.rafaelanastacioalves.moby.domain.entities.Resource;

import java.io.IOException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

/*
 * Normalmente usa-se o ResultType para quando você finalmente carrega do DB.
 * No, caso, como não temos DB, ignoramos o RequestType e consideramos o ResultType
 * direto.
 */
public abstract class NetworkBoundResource<ResultType, RequestType> {

    public static final String ERRO_DE_NETWORK = "Erro de Network";
    private static final String ERRO_DE_API = "Erro de API";
    private MutableLiveData<Resource<ResultType>> result = new MutableLiveData<>();

    public NetworkBoundResource(){
        result.setValue((Resource<ResultType>) Resource.loading(null));
        fetchFromNetwork();
    }

    private void fetchFromNetwork() {
        Call<ResultType> call = createCall();
        call.enqueue(new Callback<ResultType>() {
            @Override
            public void onResponse(Call<ResultType> call, Response<ResultType> response) {
                if (response.isSuccessful()) {
                    if(response.code() == 200){
                        Timber.d("API response: Successful");
                        setValue(Resource.success(response.body()));
                    }else {
                        Timber.w("API response: NOT Successful");
                        setValue((Resource<ResultType>) Resource.error(Resource.Status.GENERIC_ERROR,null, null));
                    }

                }
            }

            @Override
            public void onFailure(Call<ResultType> call, Throwable t) {
                onFetchFailed();
                //Aqui pode ter uma mensagem personalizada... Não deveria ter mensagem
                //De throwable
                Timber.e("Erro de network: " + t.getMessage());
                if (t instanceof HttpException){

                    HttpException httpException = (HttpException) t;

                    if (httpException.code() == HttpsURLConnection.HTTP_INTERNAL_ERROR){
                        setValue((Resource<ResultType>) Resource.error(Resource.Status.INTERNAL_SERVER_ERROR, null, null));
                    }
                }if(t instanceof IOException) {
                    setValue((Resource<ResultType>) Resource.error(Resource.Status.CONNECTIVITY_ERROR, null, t.getMessage()));
                }else{
                    setValue((Resource<ResultType>) Resource.error(Resource.Status.GENERIC_ERROR,null, null));
                }
            }
        });
    }

    protected abstract void onFetchFailed();

    private <T> void setValue(Resource<ResultType> newValue) {
        result.setValue(newValue);
    }

    protected abstract Call<ResultType> createCall();

    public MutableLiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

}
