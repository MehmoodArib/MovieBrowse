package com.exe.mehmood.moviebrowse.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.exe.mehmood.moviebrowse.DetailActivity;
import com.exe.mehmood.moviebrowse.R;
import com.exe.mehmood.moviebrowse.data.ViewModels.MainActivityViewModel;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;


/***
 * Movie Adapter used with movie card in Main Activity.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private MainActivityViewModel mainActivityViewModel;
    private AppCompatActivity mContext;
    private List<Movie> movieList;


    public MoviesAdapter(AppCompatActivity mContext, List<Movie> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
        this.mainActivityViewModel = ViewModelProviders.of(mContext).get(MainActivityViewModel.class);

    }


    @NonNull
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.title.setText(movieList.get(i).getOriginalTitle());
        String vote = Double.toString(movieList.get(i).getVoteAverage() / 2);
        viewHolder.rating.setRating(Float.parseFloat(vote));
        viewHolder.userRatingText.setText(vote);

        mainActivityViewModel.getMovie(movieList.get(i).getId()).observe(mContext, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    viewHolder.favImageView.setTag(R.drawable.ic_fav2);
                    viewHolder.favImageView.setImageResource(R.drawable.ic_fav2);
                } else {
                    viewHolder.favImageView.setTag(R.drawable.ic_fav);
                    viewHolder.favImageView.setImageResource(R.drawable.ic_fav);
                }
            }
        });


        String poster = "https://image.tmdb.org/t/p/w500" + movieList.get(i).getPosterPath();

        Glide.with(mContext)
                .load(poster)
                .placeholder(R.drawable.load)
                .apply(new RequestOptions().override(400, 400))
                .centerCrop()
                .into(viewHolder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, userRatingText;
        ImageView thumbnail, favImageView;
        RatingBar rating;

        MyViewHolder(final View view) {
            super(view);
            title = view.findViewById(R.id.title);
            rating = view.findViewById(R.id.userRating);
            thumbnail = view.findViewById(R.id.thumbnail);
            favImageView = view.findViewById(R.id.imageViewFav);
            userRatingText = view.findViewById(R.id.userRatingText);


            favImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int) favImageView.getTag();
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        final Movie clickedMovie = movieList.get(pos);

                        if (id == R.drawable.ic_fav) {


                            favImageView.setTag(R.drawable.ic_fav2);
                            favImageView.setImageResource(R.drawable.ic_fav2);

                            mainActivityViewModel.insert(clickedMovie);

                            Snackbar snackbar = Snackbar.make(view, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.setAction(R.string.undo_string, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mainActivityViewModel.deleteMovie(clickedMovie);
                                }
                            });
                            snackbar.show();


                        } else {


                            favImageView.setTag(R.drawable.ic_fav);
                            favImageView.setImageResource(R.drawable.ic_fav);
                            mainActivityViewModel.deleteMovie(clickedMovie);
                            Snackbar snackbar = Snackbar.make(view, "Removed from Favorite",
                                    Snackbar.LENGTH_SHORT);
                            snackbar.setAction(R.string.undo_string, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mainActivityViewModel.insert(clickedMovie);
                                }
                            });
                            snackbar.show();


                        }
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Movie clickedDataItem = movieList.get(pos);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra(mContext.getResources().getString(R.string.movie), clickedDataItem);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
