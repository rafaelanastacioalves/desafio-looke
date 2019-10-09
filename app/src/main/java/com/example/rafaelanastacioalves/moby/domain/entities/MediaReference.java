package com.example.rafaelanastacioalves.moby.domain.entities;

import java.io.File;

import okhttp3.ResponseBody;

public class MediaReference {
    public final ResponseBody audioResponse;
    public final ResponseBody videoResponse;
    public File getVideoReference;
    private File videoFile;

    public MediaReference(ResponseBody audio, ResponseBody video) {
        this.audioResponse = audio;
        this.videoResponse = video;
    }

    public void setVideoFile(File file) {
        this.videoFile = file;
    }

    public File getVideoFile() {
        return videoFile;
    }
}
