package com.example.mycinemaapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mycinemaapp2.data.MoviesDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    public static ArrayList<Movie> movies;
    private MoviesDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.floatingActionButtonAddMovie);
        recyclerView = findViewById(R.id.recyclerViewMovies);
        adapter = new MovieAdapter();
        movies = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = MoviesDatabase.getInstance(this);
        getData();
        adapter.setMovieArrayList(movies);
        recyclerView.setAdapter(adapter);
        adapter.setOnNoteClickListener(new MovieAdapter.OnNoteClickListener() {
            @Override
            public void onLongClick(int position) {
                Toast.makeText(MainActivity.this, "ID: " + position, Toast.LENGTH_SHORT).show();
                remove(position);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddMovieActivity.class);
                startActivity(intent);

            }
        });

    }

    public void getData(){

        List<Movie> moviesFromDB = database.moviesDao().getAllMovies();
        movies.clear();
        movies.addAll(moviesFromDB);

    }

    private void remove(int position){
        Movie movie = movies.get(position);
        database.moviesDao().deleteMovie(movie);
        getData();
        adapter.notifyDataSetChanged();

        }

}