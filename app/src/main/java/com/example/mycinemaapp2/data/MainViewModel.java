package com.example.mycinemaapp2.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mycinemaapp2.Movie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static MoviesDatabase database;
    private LiveData<List<Movie>> movies;


    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
    }


    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void setMovies(LiveData<List<Movie>> movies) {
        this.movies = movies;
    }


    public void deleteAllMovie(){
        new deleteAllTask().execute();
    }

    public Movie getMovieById(int id){
        try {
            return new getMovieTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class getMovieTask extends AsyncTask<Integer,Void,Movie>{

        @Override
        protected Movie doInBackground(Integer... integers) {
            if(integers != null && integers.length > 0){
                return database.moviesDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    public void insertMovie(Movie movie){
        new insertTask().execute(movie);
    }

    public void deleteMovie(Movie movie){
        new deleteTask().execute(movie);
    }

    private static class insertTask extends AsyncTask<Movie,Void,Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0 ){
                database.moviesDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    private static class deleteTask extends AsyncTask<Movie,Void,Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            if(movies != null && movies.length > 0 ){
                database.moviesDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }


    private static class deleteAllTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... movies) {
            database.moviesDao().deleteAllMovie();
            return null;
        }
    }
}
