package com.example.mycinemaapp2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mycinemaapp2.data.FavouriteMovie;
import com.example.mycinemaapp2.data.MainViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    public static ArrayList<Movie> movies;
    private MainViewModel viewModel;
    private Movie movie;
    private FirebaseAuth mAuth;
    User user;
    private FirebaseFirestore db;
    private MenuItem adminPanel;
    private FirebaseUser userCheck;
    int i;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        userCheck = mAuth.getCurrentUser();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.itemSignOut:
                signOut();
                break;
            case R.id.itemTickets:
                Intent intentU = new Intent(this,UserTickets.class);
                startActivity(intentU);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        floatingActionButton = findViewById(R.id.floatingActionButtonAddMovie);
        recyclerView = findViewById(R.id.recyclerViewMovies);
        adapter = new MovieAdapter();
        movies = new ArrayList<>();
        getData();
        i = 0;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mAuth = FirebaseAuth.getInstance();
        adapter.setMovieArrayList(movies);
        recyclerView.setAdapter(adapter);
        FirebaseUser userFromReg = mAuth.getCurrentUser();
        if(userFromReg != null){
            user = new User(userFromReg.getUid(),"user");
            db.collection("users").document(userFromReg.getEmail()).collection("role").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            String value = document.getString("role");
                            if(userFromReg != null){
                                user.setRole(value);
                                if(user.getRole().equals("admin")){
                                    db.collection("users").document(userFromReg.getEmail()).collection("role")
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                    db.collection("users").document(userFromReg.getEmail()).collection("role").
                                                            document(documentSnapshot.getId()).update("role","admin");
                                                }
                                            }
                                        }
                                    });
                                    floatingActionButton.setVisibility(View.VISIBLE);
                                }else {
                                    floatingActionButton.setVisibility(View.INVISIBLE);
                                }
                            }

                        }
                    }
                }
            });
        }




        adapter.setOnNoteClickListener(new MovieAdapter.OnNoteClickListener() {
            @Override
            public void onLongClick(int position) {
                Movie movie = movies.get(position);
                db.collection("users").document(userFromReg.getEmail()).collection("role").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String value = document.getString("role");
                                if(userFromReg != null){
                                    if(value.equals("admin")){
                                        remove(position);
                                        Toast.makeText(MainActivity.this, "ID: " + movie.getId(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onReserveClick(int position, String time) {
                openPage(position, time);
            }

            @Override
            public void onChangeFavouriteClick(int position, Button button) {

                Movie movie = movies.get(position);
                FavouriteMovie favouriteMovie = viewModel.getFavouriteMovieById(movie.getId());
                if(favouriteMovie == null){
                    viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
                    Toast.makeText(MainActivity.this, "Добавлено", Toast.LENGTH_SHORT).show();
                    button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_favorite_red_35dp,0);
                    movie.setFavourite(true);
                }else {
                    viewModel.deleteFavouriteMovie(favouriteMovie);
                    Toast.makeText(MainActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                    button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_favorite_border_black_35dp,0);
                    movie.setFavourite(false);
                }

            }
        });
        if(userFromReg == null){
            signOut();
        }



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddMovieActivity.class);
                startActivity(intent);
            }
        });

     /*  db.collection("allMovies").addSnapshotListener(new com.google.firebase.firestore.EventListener<QuerySnapshot>() {
           @Override
           public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
               if(value != null){
                   List<Movie> movies = value.toObjects(Movie.class);
                   adapter.setMovieArrayList(movies);
               }
           }
       }); */

    }

    public void openPage(int position, String time){
        Movie movie = movies.get(position);
        int id = movie.getId();

        Intent intent2 = new Intent(this,MakeReservation.class);

        intent2.putExtra("id", id);
        intent2.putExtra("time", time);

        startActivity(intent2);
    }

    public void getData(){


        LiveData<List<Movie>> moviesFromDB = viewModel.getMovies();
        moviesFromDB.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> moviesFromLiveData) {
                movies.clear();
                movies.addAll(moviesFromLiveData);
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void remove(int position) {
        Movie movie = movies.get(position);

        db.collection("allMovies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String titleMovie = document.getString("title");
                        String descriptionMovie = document.getString("description");
                        if(titleMovie.equals(movie.getTitle()) && descriptionMovie.equals(movie.getDescription())){
                            db.collection("allMovies").document(document.getId()).delete();
                        }
                    }
                }
            }
        });

        viewModel.deleteMovie(movie);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser userFromReg = mAuth.getCurrentUser();
                user = new User(userFromReg.getUid(),"user");


                db.collection("users").document(userFromReg.getEmail()).collection("role").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                    }
                });


                db.collection("users").document(userFromReg.getEmail()).collection("role").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String value = document.getString("role");
                                if(userFromReg != null){
                                    user.setRole(value);
                                    if(user.getRole().equals("admin")){
                                        db.collection("users").document(userFromReg.getEmail()).collection("role")
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()){
                                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                                        db.collection("users").document(userFromReg.getEmail()).collection("role").
                                                                document(documentSnapshot.getId()).update("role","admin");
                                                    }
                                                }
                                            }
                                        });
                                        floatingActionButton.setVisibility(View.VISIBLE);
                                    }else {
                                        floatingActionButton.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }
                        }
                    }
                });
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void signOut(){

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }



}