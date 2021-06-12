package com.example.pasrpl1024.utils;

import java.util.List;

public class UserAnime {

    private UserClass user;
    private List<AnimeUserData> animeData;

    public UserAnime() {
    }

    public UserAnime(UserClass user, List<AnimeUserData> animeData) {
        this.user = user;
        this.animeData = animeData;
    }

    public UserClass getUser() {
        return user;
    }

    public void setUser(UserClass user) {
        this.user = user;
    }

    public List<AnimeUserData> getAnimeData() {
        return animeData;
    }

    public void setAnimeData(List<AnimeUserData> animeData) {
        this.animeData = animeData;
    }
}
