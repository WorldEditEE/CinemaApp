package com.example.mycinemaapp2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>{

    private List<Ticket> ticketsList;

    public TicketsAdapter() {
        this.ticketsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item,parent,false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {

        Ticket ticket = ticketsList.get(position);
        holder.textViewTitle.setText(ticket.getTitleFromTicket());
        holder.textViewTime.setText(ticket.getTimeFromTicket());
        holder.textViewDetail.setText("Место : " + ticket.getPlaceTicket());
    }

    @Override
    public int getItemCount() {
        return ticketsList.size();
    }

    public void setTicketsList(List<Ticket> tickets){
        this.ticketsList = tickets;
        notifyDataSetChanged();
    }

    public List<Ticket> getTicketsList(){
        return ticketsList;
    }

    class TicketViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewTitle;
        private TextView textViewTime;
        private TextView textViewDetail;

        public TicketViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewTitle = itemView.findViewById(R.id.film_title_text);
            textViewTime = itemView.findViewById(R.id.starting_time_text);
            textViewDetail = itemView.findViewById(R.id.detail_button);

        }

    }
}
