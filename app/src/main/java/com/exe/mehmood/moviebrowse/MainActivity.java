package com.exe.mehmood.moviebrowse;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.exe.mehmood.moviebrowse.adapter.MoviesAdapter;
import com.exe.mehmood.moviebrowse.api.Service;
import com.exe.mehmood.moviebrowse.data.MovieViewModel;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.exe.mehmood.moviebrowse.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This is the Main Activity consisting of RecyclerView.
 * We store the sort-order in shared preference and according to the sort-order we load the values
 * in recycler view.
 */

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getName();
    MovieViewModel movieViewModel;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AppCompatActivity activity = MainActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);


        initViews();

    }

    /**
     * Initialize Views
     */
    private void initViews() {

        mRecyclerView = findViewById(R.id.recycler_view);

        List<Movie> movieList = new ArrayList<>();
        mAdapter = new MoviesAdapter(this, movieList);


        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        mSwipeRefreshLayout = findViewById(R.id.main_content);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        checkSortOrder();

    }


    /**
     * Load saved favourite movies from the local database
     */

    private void loadFavouriteMovies() {
        mRecyclerView = findViewById(R.id.recycler_view);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SharedPreferences sortBy = this.getSharedPreferences(getResources().getString(R.string.sortByFileKey), Context.MODE_PRIVATE);
        final String sortOrder = sortBy.getString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.popular_movies));
        movieViewModel.getAllFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                //Here we have put this if condition because since favMovie data is coming from
                // live data so when we are adding any value in fav database  this onchanged method is called
                //and values in recycler view are changing to the favlist.
                //which is not the required behaviour.
                //so we have put the check condition.
                if (Objects.equals(sortOrder, getResources().getString(R.string.favourite))) {
                    mAdapter = new MoviesAdapter(MainActivity.this, movies);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    /**
     * Load Popular Movies By making an Api call to tmdb.org.
     */
    private void loadPopularMovies() {
        mSwipeRefreshLayout.setEnabled(true);
        try {
            Service apiService =
                    com.exe.mehmood.moviebrowse.api.Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    List<Movie> movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load Upcoming Movies By making an Api call to tmdb.org.
     */
    private void loadUpComingMovies() {
        mSwipeRefreshLayout.setEnabled(true);
        try {


            Service apiService =
                    com.exe.mehmood.moviebrowse.api.Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getUpComingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    List<Movie> movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load TopRated Movies By making an Api call to tmdb.org.
     */
    private void loadTopRatedMovies() {
        mSwipeRefreshLayout.setEnabled(true);

        try {

            Service apiService =
                    com.exe.mehmood.moviebrowse.api.Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    List<Movie> movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load NowPlaying Movies By making an Api call to tmdb.org.
     */
    private void loadNowPlayingMovies() {
        mSwipeRefreshLayout.setEnabled(true);

        try {
            Service apiService =
                    com.exe.mehmood.moviebrowse.api.Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getNowPlayingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    List<Movie> movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * search Movies By making an Api call to tmdb.org.
     * <p>
     * para : query "is the string which we have to search for"
     */

    private void loadMovieSearch(String query) {
        try {
            Service apiService = com.exe.mehmood.moviebrowse.api.Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.findMovie(query, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            Log.d("TAG", "" + call.request().url());
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    List<Movie> movies = response.body().getResults();
                    mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, movies));
                    mRecyclerView.smoothScrollToPosition(0);
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //searchView used for searching the movie.which is shown in toolbar.
        MenuItem searchViewItem = menu.findItem(R.id.searchView);
        final SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                loadMovieSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    loadMovieSearch(newText);
                } else {
                    checkSortOrder();
                }
                return true;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Here we update the value of sort-order which is stored in shared preference
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sortBy = activity.getSharedPreferences(getResources().getString(R.string.sortByFileKey), MODE_PRIVATE);
        SharedPreferences.Editor edit = sortBy.edit();
        switch (item.getItemId()) {
            case R.id.popular:
                Log.d(LOG_TAG, getResources().getString(R.string.popular_movies) + " , " + "Pop");
                edit.putString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.popular_movies));
                edit.apply();
                checkSortOrder();
                return true;
            case R.id.now_playing:
                Log.d(LOG_TAG, getResources().getString(R.string.now_playing_movies) + " , " + "now");
                edit.putString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.now_playing_movies));
                edit.apply();
                checkSortOrder();
                return true;
            case R.id.up_coming:
                Log.d(LOG_TAG, getResources().getString(R.string.upcoming_movies) + " , " + "up");
                edit.putString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.upcoming_movies));
                edit.apply();
                checkSortOrder();
                return true;
            case R.id.top_rated:
                Log.d(LOG_TAG, getResources().getString(R.string.top_rated_movies) + " , " + "top");
                edit.putString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.top_rated_movies));
                edit.apply();
                checkSortOrder();
                return true;
            case R.id.favourite:
                Log.d(LOG_TAG, getResources().getString(R.string.favourite) + " , " + "top");
                edit.putString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.favourite));
                edit.apply();
                checkSortOrder();
                return true;
            default:
                edit.apply();
                checkSortOrder();
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This method check the value in sort-order and load the data accordingly in Recycler View
     */
    private void checkSortOrder() {
        SharedPreferences sortBy = this.getSharedPreferences(getResources().getString(R.string.sortByFileKey), Context.MODE_PRIVATE);
        String sortOrder = sortBy.getString(getResources().getString(R.string.pref_sort_order_key), getResources().getString(R.string.popular_movies));
        Log.d(LOG_TAG, sortOrder);
        if (Objects.equals(sortOrder, getResources().getString(R.string.popular_movies))) {
            Log.d(LOG_TAG, "Sorting by most popular");
            loadPopularMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.favourite))) {
            Log.d(LOG_TAG, "Sorting by favorite");
            loadFavouriteMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.top_rated_movies))) {
            Log.d(LOG_TAG, "Sorting by top_rated");
            loadTopRatedMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.upcoming_movies))) {
            Log.d(LOG_TAG, "Sorting by Up_coming");
            loadUpComingMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.now_playing_movies))) {
            Log.d(LOG_TAG, "Sorting by now_playing");
            loadNowPlayingMovies();

        }
    }
}

