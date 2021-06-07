package com.example.mycinemaapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycinemaapp2.data.MainViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeReservation extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewTime;
    private ImageView imageViewPoster;
    private GridView gridViewSeats;
    private int id;
    private String time;
    private MainViewModel viewModel;
    MainAdapter adapter;
    public static List<Ticket> tickets;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_reservation);
        textViewTime = findViewById(R.id.textViewMovieTime);
        imageViewPoster = findViewById(R.id.moviePosterImageView);
        gridViewSeats = findViewById(R.id.gridPlaces);
        tickets = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        List<String> placesNumber = Arrays.asList("1","2","3","4","5","6","7","8","9");
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("id") && intent.hasExtra("time")){
            id = intent.getIntExtra("id",-1);
            time = intent.getStringExtra("time");
        }else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Movie movie = viewModel.getMovieById(id);

        Picasso.get().load(movie.getImageURL()).into(imageViewPoster);
        textViewTime.setText(time);
        title = movie.getTitle();

        adapter = new MainAdapter(MakeReservation.this,placesNumber,time,title,tickets);
        gridViewSeats.setAdapter(adapter);
        gridViewSeats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MakeReservation.this, "нажата" + position, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void onClickBuyTickets(View view) {

        tickets = adapter.getAllTickets();

       for(Ticket ticket : tickets){
            db.collection("users").document(user.getEmail()).collection("tickets").add(ticket).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    tickets.remove(ticket);
                }
            });
           db.collection("tickets").document(title).collection(time).add(ticket);
        }



        Toast.makeText(this, "Спасибо за покупку, Добавлено - " + tickets.size(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,UserTickets.class);
        startActivity(intent);

    }


}