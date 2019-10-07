package com.example.rafaelanastacioalves.moby.domain.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MainEntity implements Serializable {

    @SerializedName("objects")
    private final List<Objects> objects;

    public MainEntity(List<Objects> objects) {
        this.objects = objects;
    }

    public List<Objects> getObjects() {
        return objects;
    }

    public class Objects implements Serializable {
        @SerializedName("name")
        private final String name;

        @SerializedName("bg")
        private final String bg;

        @SerializedName("im")
        private final String im;

        @SerializedName("sg")
        private final String sg;

        public Objects(String name, String bg, String im, String sg) {
            this.name = name;
            this.bg = bg;
            this.im = im;
            this.sg = sg;
        }

        public String getName() {
            return name;
        }

        public String getBg() {
            return bg;
        }

        public String getIm() {
            return im;
        }

        public String getSg() {
            return sg;
        }
    }
}
