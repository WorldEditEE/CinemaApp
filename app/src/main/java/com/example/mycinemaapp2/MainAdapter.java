package com.example.mycinemaapp2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.mycinemaapp2.MakeReservation;
import com.example.mycinemaapp2.R;
import com.example.mycinemaapp2.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class MainAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> placeNumber;
    private String time;
    private String title;
    private List<String> pickPlaces;
    private  List<Ticket> tickets1;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;


    public MainAdapter(MakeReservation makeReservation, List<String> placeNumber, String time, String title, List<Ticket> tickets) {

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        context = makeReservation;
        this.placeNumber = placeNumber;
        this.time = time;
        this.title = title;
        pickPlaces = Arrays.asList("0","0","0","0","0","0","0","0","0");
        tickets1 = tickets;
    }

    @Override
    public int getCount() {
        return placeNumber.size();
    }

    @Override
    public Object getItem(int position) {
        return placeNumber.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null){
            inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_item,null);
        }
        Button button;
        button = convertView.findViewById(R.id.buttonPlace);
        button.setText(placeNumber.get(position));
        button.setBackgroundColor(Color.GREEN);
        Ticket ticket = new Ticket(title,time,placeNumber.get(position));

        db.collection("tickets").document(title).collection(time).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String place = document.getString("placeTicket");
                        if(place.equals(ticket.getPlaceTicket())){
                            button.setEnabled(false);
                            button.setBackgroundColor(Color.LTGRAY);
                        }
                    }
                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!pickPlaces.get(position).equals(placeNumber.get(position))){
                    pickPlaces.set(position,placeNumber.get(position));
                    tickets1.add(ticket);
                    Toast.makeText(context, "Добавлено в firestore", Toast.LENGTH_SHORT).show();
                    v.setBackgroundColor(Color.BLUE);
                }else {
                    pickPlaces.set(position,"0");
                    tickets1.remove(ticket);

                    v.setBackgroundColor(Color.GREEN);
                }

            }
        });

        return convertView;
    }

    public List<String> getPlaceNumber() {
        return placeNumber;
    }

    public List<Ticket> getAllTickets(){
        return tickets1;
    }

}
