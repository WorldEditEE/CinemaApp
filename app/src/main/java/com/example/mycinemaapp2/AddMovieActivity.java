package com.example.mycinemaapp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycinemaapp2.data.MainViewModel;
import com.example.mycinemaapp2.data.MoviesDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AddMovieActivity extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextGenre;
    private EditText editTextRating;
    private EditText editTextFirstShow;
    private EditText editTextSecondShow;
    private EditText editTextThirdShow;
    private EditText editTextURLImages;
    private MainViewModel viewModel;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        db = FirebaseFirestore.getInstance();
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextGenre = findViewById(R.id.editTextTextGenre);
        editTextRating = findViewById(R.id.editTextTextRating);
        editTextFirstShow = findViewById(R.id.editTextTextFirstShow);
        editTextSecondShow = findViewById(R.id.editTextTextSecondShow);
        editTextThirdShow = findViewById(R.id.editTextTextThirdShow);
        editTextURLImages = findViewById(R.id.editTextURLImages);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }



    public void onClickAddMovie(View view) {

        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        double rating = Double.parseDouble(editTextRating.getText().toString().trim());
        String genre = editTextGenre.getText().toString().trim();
        String firstShow = editTextFirstShow.getText().toString().trim();
        String secondShow = editTextSecondShow.getText().toString().trim();
        String thirdShow = editTextThirdShow.getText().toString().trim();
        String imageViewPoster = editTextURLImages.getText().toString().trim();

        if(isField(title,genre,description,rating,firstShow,secondShow,thirdShow,imageViewPoster)){
            Movie movie = new Movie(title,description,genre,rating,firstShow,secondShow,thirdShow,imageViewPoster);
            viewModel.insertMovie(movie);
            db.collection("allMovies").add(movie).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Intent intent = new Intent(AddMovieActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddMovieActivity.this, "Ошибка добавления в Firestore", Toast.LENGTH_SHORT).show();
                }
            });

        }else {
            Toast.makeText(this, "Все поля должны быть заполненны", Toast.LENGTH_SHORT).show();

        }

    }

    private boolean isField(String title,String genre,String description, Double rating, String firstShow, String secondShow, String thirdShow, String imageViewPoster){

        return !title.isEmpty() && !description.isEmpty() && !genre.isEmpty() && rating!= null && !firstShow.isEmpty() && !secondShow.isEmpty() && !thirdShow.isEmpty() && !imageViewPoster.isEmpty();

    }
}