package com.example.rafaelanastacioalves.moby.common;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.example.rafaelanastacioalves.moby.BuildConfig;

import java.io.File;

public class MediaReferenceHelper {

    public static Uri getMediaUriFrom(@NonNull File file, Context context){
        return Uri.fromFile(file);

    }
}
