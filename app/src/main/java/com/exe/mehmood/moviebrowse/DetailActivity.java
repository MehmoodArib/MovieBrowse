package com.exe.mehmood.moviebrowse;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.exe.mehmood.moviebrowse.adapter.TrailerAdapter;
import com.exe.mehmood.moviebrowse.data.ViewModels.DetailActivityViewModel;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.exe.mehmood.moviebrowse.model.NetworkResponse;
import com.exe.mehmood.moviebrowse.model.Trailer;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DetailActivity extends AppCompatActivity {
    private TextView mNameOfMovieTextView, mPlotSynopsisTextView, mReleaseDate, mUserRatingText;
    private RatingBar mUserRatingBar;
    private ImageView mThumbNailImageView;
    private Movie movie;
    private String thumbnail, movieName, synopsis, dateOfRelease;
    private float rating;
    private int movie_id;
    private DetailActivityViewModel detailActivityViewModel;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        detailActivityViewModel = ViewModelProviders.of(this).get(DetailActivityViewModel.class);

        mThumbNailImageView = findViewById(R.id.thumbnail_image_header);
        mNameOfMovieTextView = findViewById(R.id.title);
        mPlotSynopsisTextView = findViewById(R.id.plotSynopsis);
        mUserRatingBar = findViewById(R.id.userRating);
        mReleaseDate = findViewById(R.id.releaseDate);
        mUserRatingText = findViewById(R.id.userRatingText);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null)
            if (intentThatStartedThisActivity.hasExtra(this.getResources().getString(R.string.movie))) {

                movie = (Movie) getIntent().getSerializableExtra(this.getResources().getString(R.string.movie));

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
     * Initialize views
     */

    private void initViews() {
        List<Trailer> trailerList = new ArrayList<>();
        TrailerAdapter trailerAdapter = new TrailerAdapter(this, trailerList);

        mRecyclerView = findViewById(R.id.recycler_view1);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();

        loadMovieTrailer(movie_id);

    }

    /**
     * * Load  Movie trailers By making an Api call to tmdb.org.
     */
    void loadMovieTrailer(int movie_id) {
        detailActivityViewModel.getMovieTrailer(movie_id).observe(this, new Observer<NetworkResponse<List<Trailer>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Trailer>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mRecyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), listNetworkResponse.getData()));
                        mRecyclerView.smoothScrollToPosition(0);
                        break;
                    case LOADING:

                        break;
                    case ERROR:

                        break;
                }
            }
        });
    }
}
