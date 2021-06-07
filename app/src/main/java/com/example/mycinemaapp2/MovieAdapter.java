package com.example.mycinemaapp2;

import android.annotation.SuppressLint;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycinemaapp2.data.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieArrayList;
    private int mExpandedPosition = -1;
    private OnNoteClickListener onNoteClickListener;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public MovieAdapter(){
        movieArrayList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public OnNoteClickListener getOnNoteClickListener() {
        return onNoteClickListener;
    }

    public void setOnNoteClickListener(OnNoteClickListener onNoteClickListener) {
        this.onNoteClickListener = onNoteClickListener;
    }

    interface OnNoteClickListener{
        void onLongClick(int position);
        void onReserveClick(int position, String time);
        void onChangeFavouriteClick(int position, Button button);
    }

    interface setFavourite{
        void setFavouriteMovie(int position);
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
        holder.movieObject = movie;

        db.collection("allMovies").whereEqualTo("title",movie.getTitle()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        String titleMovie = document.getString("title");
                        if(titleMovie.equals(movie.getTitle())){
                            db.collection("allMovies").document(document.getId()).update("id",movie.getId());
                        }
                    }
                }
            }
        });

        holder.title.setText(movie.getTitle());
        holder.description.setText(movie.getDescription());
        holder.genre.setText(movie.getGenre());
        holder.rating.setText(String.format("%s",movie.getRating()));
        String firstShow = movie.getFirstShow();
        String secondShow = movie.getSecondShow();
        String thirdShow = movie.getThirdShow();

        boolean isFavourite = movie.getFavourite();
        if(isFavourite){
            holder.favouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_favorite_red_35dp,0);
        }else{
            holder.favouriteButton.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_favorite_border_black_35dp,0);
        }

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

    public List<Movie> getMovieArrayList() {
        return movieArrayList;
    }

    public void setMovieArrayList(List<Movie> movieArrayList) {
        this.movieArrayList = movieArrayList;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> movies){
        this.movieArrayList.addAll(movies);
        notifyDataSetChanged();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder{

        private Movie movieObject;
        private TextView title;
        private TextView description;
        private TextView genre;
        private TextView rating;
        private ImageView poster;
        List<RadioButton> buttons = new ArrayList<>();
        private RelativeLayout detailsOnExpand;
        private TextView expandIcon;
        private Button reserveButton;
        private RadioGroup radioGroup;
        private Button favouriteButton;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_view_title);
            genre = itemView.findViewById(R.id.text_view_genre);
            rating = itemView.findViewById(R.id.text_view_rating);
            description = itemView.findViewById(R.id.text_view_description);
            poster = itemView.findViewById(R.id.film_poster);
            detailsOnExpand = itemView.findViewById(R.id.details_on_expand);
            expandIcon = itemView.findViewById(R.id.expand_icon);
            reserveButton = itemView.findViewById(R.id.openPage);
            radioGroup = itemView.findViewById(R.id.hour_choices);
            favouriteButton = itemView.findViewById(R.id.heart);


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(onNoteClickListener != null){
                        onNoteClickListener.onLongClick(getAdapterPosition());
                    }
                    return true;
                }
            });

            reserveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onNoteClickListener != null){
                        int selectedId = radioGroup.getCheckedRadioButtonId();
                        RadioButton selectedButton = (RadioButton) itemView.findViewById(selectedId);
                        if (selectedButton != null) {
                            String selectedTime = (String) selectedButton.getText();
                            onNoteClickListener.onReserveClick(getAdapterPosition(),selectedTime);
                        }

                    }
                }
            });

            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onNoteClickListener != null){
                        onNoteClickListener.onChangeFavouriteClick(getAdapterPosition(),favouriteButton);
                    }else{

                    }
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
