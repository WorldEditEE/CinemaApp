package com.example.mycinemaapp2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AdminPanel extends AppCompatActivity {

    RecyclerView recyclerViewUsers;
    UserAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        adapter = new UserAdapter();
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    List<User> users = value.toObjects(User.class);
                    Toast.makeText(AdminPanel.this, "ХАЙ", Toast.LENGTH_SHORT).show();
                    adapter.setUserList(users);
                }
            }
        });
    }
}