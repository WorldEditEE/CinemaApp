package com.example.mycinemaapp2;

import android.annotation.SuppressLint;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movieArrayList;
    private int mExpandedPosition = -1;
    private OnNoteClickListener onNoteClickListener;

    public MovieAdapter(){
        movieArrayList = new ArrayList<>();
    }

    public OnNoteClickListener getOnNoteClickListener() {
        return onNoteClickListener;
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    interface OnNoteClickListener{
        void onLongClick(int position);
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieArrayList.get(position);

        holder.title.setText(movie.getTitle());
        holder.description.setText(movie.getDescription());
        holder.genre.setText(movie.getGenre());
        holder.rating.setText(String.format("%s",movie.getRating()));
        String firstShow = movie.getFirstShow();
        String secondShow = movie.getSecondShow();
        String thirdShow = movie.getThirdShow();

        String urlToImage = movie.getImageURL();
        if(urlToImage != null && !urlToImage.isEmpty()){
            Picasso.get().load(urlToImage).into(holder.poster);
        }

        List<String> times = new ArrayList<>();
        times.add(firstShow);
        times.add(secondShow);
        times.add(thirdShow);

        for(int i = 0; i < times.size() ; i++){
            holder.buttons.get(i).setText(times.get(i));
            holder.buttons.get(i).setVisibility(View.VISIBLE);
        }


        final boolean isExpanded = position == mExpandedPosition;
        holder.detailsOnExpand.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        int expandedIconId = isExpanded ? R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp;
        holder.expandIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, expandedIconId, 0);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(holder.detailsOnExpand);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public ArrayList<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public void setMovieArrayList(ArrayList<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
        notifyDataSetChanged();
    }

    public void addMovies(ArrayList<Movie> movies){
        this.movieArrayList.addAll(movies);
        notifyDataSetChanged();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        private TextView description;
        private TextView genre;
        private TextView rating;
        private ImageView poster;
        List<RadioButton> buttons = new ArrayList<>();
        private RelativeLayout detailsOnExpand;
        private TextView expandIcon;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            genre = itemView.findViewById(R.id.text_view_genre);
            rating = itemView.findViewById(R.id.text_view_rating);
            description = itemView.findViewById(R.id.text_view_description);
            poster = itemView.findViewById(R.id.film_poster);
            detailsOnExpand = itemView.findViewById(R.id.details_on_expand);
            expandIcon = itemView.findViewById(R.id.expand_icon);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onNoteClickListener != null){
                        onNoteClickListener.onLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });


            List<Integer> ids = Arrays.asList(R.id.radio1, R.id.radio2, R.id.radio3, R.id.radio4);
            for (int i = 0; i < ids.size(); i++) {
                final RadioButton button = itemView.findViewById(ids.get(i));
                buttons.add(button);
            }



        }
    }
}
