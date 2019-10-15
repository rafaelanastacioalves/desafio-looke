package com.example.rafaelanastacioalves.moby.domain.entities;

import java.io.File;

import okhttp3.ResponseBody;

public class MediaReference {

    private File videoFile;
    private File audioFile;

    public MediaReference(File audio, File video) {
        this.audioFile = audio;
        this.videoFile = video;
    }

    public void setVideoFile(File file) {
        this.videoFile = file;
    }

    public File getVideoFile() {
        return videoFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public File getAudioFile() {
        return audioFile;
    }
}
