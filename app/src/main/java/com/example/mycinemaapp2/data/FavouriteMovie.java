package com.example.mycinemaapp2.data;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.mycinemaapp2.Movie;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int id, String title, String description, String genre, double rating, String firstShow, String secondShow, String thirdShow, String imageURL) {
        super(id, title, description, genre, rating, firstShow, secondShow, thirdShow, imageURL);
    }

    @Ignore
    public FavouriteMovie(Movie movie){
        super(movie.getId(),movie.getTitle(),movie.getDescription(),movie.getGenre(),movie.getRating(),movie.getFirstShow(),movie.getSecondShow(),movie.getThirdShow(),movie.getImageURL());
    }


}
