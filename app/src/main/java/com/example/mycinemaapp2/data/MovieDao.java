package com.example.mycinemaapp2.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mycinemaapp2.Movie;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM `movie.db`")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM `movie.db` WHERE id == :movieId")
    Movie getMovieById(int movieId);

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM `movie.db`")
    void deleteAllMovie();

    @Query("DELETE FROM `movie.db`")
    void deleteAllFromMovie();

}
