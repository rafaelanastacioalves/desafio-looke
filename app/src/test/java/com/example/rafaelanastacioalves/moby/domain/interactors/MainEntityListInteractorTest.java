package com.example.rafaelanastacioalves.moby.domain.interactors;

import android.arch.lifecycle.LiveData;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.example.rafaelanastacioalves.moby.repository.AppRepository;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

public class MainEntityListInteractorTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AppRepository appRepository;

    @Captor
    private ArgumentCaptor<Callback> callbackArgumentCaptor;

    @Mock
    private Call<List<MainEntity>> mockedCall;

    @Test
    public void shouldReturnLiveDataAfterExecute(){
        MainEntityListInteractor interactor = new MainEntityListInteractor(appRepository);
        MainEntityListInteractor.RequestValues resquestValue = null;

        when(appRepository.getMainEntityList()).thenReturn(
                new LiveData<Resource<List<MainEntity>>>() {

                }
        );

        Object returnedObject = interactor.execute(resquestValue);
        Assert.assertThat(returnedObject, instanceOf(LiveData.class));
    }


}
