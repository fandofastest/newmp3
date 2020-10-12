package com.musicapp.tubidyapp.model;

public class Genre {
    String genre;
    String img;
    String genrelower;

    public Genre(String genre) {
        this.genre=genre;
        String genrelower=genre.toLowerCase();
        this.genrelower = genrelower.replaceAll("\\s+","");

    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
