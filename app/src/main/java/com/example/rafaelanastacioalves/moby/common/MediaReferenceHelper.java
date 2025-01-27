package com.example.rafaelanastacioalves.moby.common;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.rafaelanastacioalves.moby.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

public class MediaReferenceHelper {

    public static Uri getMediaUriFrom(@NonNull File file, Context context){
        return Uri.fromFile(file);

    }

    public static void persistMedia(ResponseBody body, String name) throws Exception {
            // todo change the file location/name according to your needs
            File file = new File(convertToStandardPath(name));

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
            } catch (IOException e) {
                throw e;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }

    }

    public static boolean hasMediaWithName(String name){
        File file = new File(convertToStandardPath(name));
        return file.exists();
    }

    public static File getMediaForName(String saveName) {
        return new File(convertToStandardPath(saveName));
    }

    private static String convertToStandardPath(String saveName) {
        return Environment.getExternalStorageDirectory() + "/" + saveName;
    }
}
