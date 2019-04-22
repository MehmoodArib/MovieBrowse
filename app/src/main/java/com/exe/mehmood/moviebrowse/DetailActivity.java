package com.exe.mehmood.moviebrowse;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exe.mehmood.moviebrowse.adapter.TrailerAdapter;
import com.exe.mehmood.moviebrowse.api.Service;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.exe.mehmood.moviebrowse.model.Trailer;
import com.exe.mehmood.moviebrowse.model.TrailerResponse;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    TextView mNameOfMovieTextView, mPlotSynopsisTextView, mReleaseDate, mUserRatingText;
    RatingBar mUserRatingBar;
    ImageView mThumbNailImageView;
    Movie movie;
    String thumbnail, movieName, synopsis, dateOfRelease;
    float rating;
    int movie_id;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        mThumbNailImageView = findViewById(R.id.thumbnail_image_header);
        mNameOfMovieTextView = findViewById(R.id.title);
        mPlotSynopsisTextView = findViewById(R.id.plotsynopsis);
        mUserRatingBar = findViewById(R.id.userrating);
        mReleaseDate = findViewById(R.id.releasedate);
        mUserRatingText = findViewById(R.id.userratingText);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("movies")) {

            movie = (Movie) getIntent().getSerializableExtra("movies");

            thumbnail = movie.getPosterPath();
            movieName = movie.getOriginalTitle();
            synopsis = movie.getOverview();
            rating = movie.getVoteAverage().floatValue() / 2;
            dateOfRelease = movie.getReleaseDate();
            movie_id = movie.getId();

            String poster = "https://image.tmdb.org/t/p/w500" + thumbnail;

            Glide.with(this)
                    .load(poster)
                    .placeholder(R.drawable.load)
                    .into(mThumbNailImageView);

            mNameOfMovieTextView.setText(movieName);
            mPlotSynopsisTextView.setText(synopsis);
            mUserRatingBar.setRating(rating);
            mUserRatingText.setText(String.valueOf(rating));
            mReleaseDate.setText(dateOfRelease);

        } else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
        initViews();

    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movieName);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(movieName);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(movieName);
                    isShow = false;
                }
            }
        });
    }

    /**
     * Initilize views
     */

    private void initViews() {
        List<Trailer> trailerList = new ArrayList<>();
        TrailerAdapter trailerAdapter = new TrailerAdapter(this, trailerList);

        mRecyclerView = findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();

        loadJSON();

    }

    /**
     * * Load  Movie trailers By making an Api call to tmdb.org.
     */
    private void loadJSON() {

        try {
            Service apiService = com.exe.mehmood.moviebrowse.api.Client.getClient().create(Service.class);
            Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                    assert response.body() != null;
                    List<Trailer> trailer = response.body().getResults();
                    mRecyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                    mRecyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(DetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
