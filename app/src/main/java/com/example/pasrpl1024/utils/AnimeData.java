package com.example.pasrpl1024.utils;

import java.io.Serializable;

public class AnimeData implements Serializable {
    private String id, title, desc, releasedOn, imageLink, posterLink;
    int episode;

    public AnimeData() {
    }

    public AnimeData(String title, String desc, String releasedOn, int episode, String imageLink, String posterLink) {
        this.title = title;
        this.desc = desc;
        this.releasedOn = releasedOn;
        this.episode = episode;
        this.imageLink = imageLink;
        this.posterLink = posterLink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(String releasedOn) {
        this.releasedOn = releasedOn;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) { this.posterLink = posterLink; }
}
