package com.example.rafaelanastacioalves.moby.domain.interactors;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Environment;
import android.util.Log;

import com.example.rafaelanastacioalves.moby.domain.entities.MainEntity;
import com.example.rafaelanastacioalves.moby.domain.entities.MediaReference;
import com.example.rafaelanastacioalves.moby.domain.entities.Resource;
import com.example.rafaelanastacioalves.moby.retrofit.MediaRepository;

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


        Observable<MediaReference> mediaReferenceObservable = Observable.combineLatest(
                appRepository.getMediaFromUrl(changeToHttp(requestValues.objects.getSg())).subscribeOn(Schedulers.io()),
                appRepository.getMediaFromUrl(changeToHttp(requestValues.objects.getBg())).subscribeOn(Schedulers.io()),
                        (audio, video) -> {
                            return new MediaReference(audio,video);
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
                            File videoFile = persistMedia(mediaReference.videoResponse, requestValues.objects.getName().trim()+ "-" + "video.mp4");
                            File audioFile = persistMedia(mediaReference.audioResponse, requestValues.objects.getName().trim() + "-" + "audio.mp3");
                            if (videoFile != null && audioFile != null){
                                mediaReference.setVideoFile(videoFile);
                                mediaReference.setAudioFile(audioFile);
                                resourceLiveData.postValue(Resource.success(mediaReference));
                            }else{
                                resourceLiveData.postValue(Resource.error(Resource.Status.GENERIC_ERROR,null, null));
                            }
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

    private File persistMedia(ResponseBody body, String name) {
        try {
            // todo change the file location/name according to your needs
            File file = new File(Environment.getExternalStorageDirectory() + "/"  + name);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                Log.d(TAG, "file path: " + file.getPath());
                return file;
            } catch (IOException e) {
                return null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return null;
        }
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
