package com.example.mycinemaapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> users;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser fireUser;

    public UserAdapter(){
        users = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        User user = users.get(position);
        String userId = user.getId();

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String value = document.getString("id");
                        if(userId.equals(value)){
                            String email = document.getId();
                            holder.textViewUserEmail.setText(email);
                        }
                    }
                }
            }
        });

        holder.textViewAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setRole("admin");
            }
        });



    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public List<User> getUsers(){
        return users;
    }

    public void setUserList(List<User> userList){
        this.users = userList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUserEmail;
        private TextView textViewAddAdmin;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserEmail = itemView.findViewById(R.id.user_title_text);
            textViewAddAdmin = itemView.findViewById(R.id.text_user_add_admin);

        }


    }
}
