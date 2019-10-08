package com.example.rafaelanastacioalves.moby.domain.entities;

import okhttp3.ResponseBody;

public class MediaReference {
    public final ResponseBody audioResponse;
    public final ResponseBody videoResponse;

    public MediaReference(ResponseBody audio, ResponseBody video) {
        this.audioResponse = audio;
        this.videoResponse = video;
    }
}
