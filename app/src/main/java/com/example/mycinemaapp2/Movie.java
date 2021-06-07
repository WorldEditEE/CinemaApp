package com.example.mycinemaapp2;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie.db")
public class Movie {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private String genre;
    private double rating;
    private String firstShow;
    private String secondShow;
    private String thirdShow;
    private String imageURL;
    private Boolean isFavourite;

    public Movie(int id, String title, String description, String genre, double rating, String firstShow, String secondShow, String thirdShow, String imageURL){
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.firstShow = firstShow;
        this.secondShow = secondShow;
        this.thirdShow = thirdShow;
        this.imageURL = imageURL;
        this.isFavourite = false;
    }

    @Ignore
    public Movie(String title, String description, String genre, double rating, String firstShow, String secondShow, String thirdShow, String imageURL){
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.rating = rating;
        this.firstShow = firstShow;
        this.secondShow = secondShow;
        this.thirdShow = thirdShow;
        this.imageURL = imageURL;
        this.isFavourite = false;
    }

    @Ignore
    public Movie(){

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getFirstShow() {
        return firstShow;
    }

    public void setFirstShow(String firstShow) {
        this.firstShow = firstShow;
    }

    public String getSecondShow() {
        return secondShow;
    }

    public void setSecondShow(String secondShow) {
        this.secondShow = secondShow;
    }

    public String getThirdShow() {
        return thirdShow;
    }

    public void setThirdShow(String thirdShow) {
        this.thirdShow = thirdShow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }
}


