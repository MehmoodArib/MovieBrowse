package com.exe.mehmood.moviebrowse;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exe.mehmood.moviebrowse.adapter.MoviesAdapter;
import com.exe.mehmood.moviebrowse.data.MovieViewModel;
import com.exe.mehmood.moviebrowse.data.NetworkResponse;
import com.exe.mehmood.moviebrowse.model.Movie;

import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.exe.mehmood.moviebrowse.SharedPreferencesUtility.Key.SORT_ORDER;

/**
 * This is the Main Activity consisting of RecyclerView.
 * We store the sort-order in shared preference and according to the sort-order we load the values
 * in recycler view.
 */

public class MainActivity extends AppCompatActivity implements Observer<NetworkResponse<List<Movie>>> {
    public static final String LOG_TAG = MainActivity.class.getName();
    MovieViewModel movieViewModel;
    private ProgressBar progressBar;
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
        String sortOrder = SharedPreferencesUtility.getInstance(this).getString(SORT_ORDER, getResources().getString(R.string.popular_movies));
        setTitle(sortOrder);
        mRecyclerView = findViewById(R.id.recycler_view);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSwipeRefreshLayout = findViewById(R.id.main_content);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkSortOrder();
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        checkSortOrder();
    }

    /**
     * Load saved favourite movies from the local database
     */

    private void loadFavouriteMovies() {
        setTitle("Favourite Movies");
        movieViewModel.getAllFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                //Here we have put this if condition because since favMovie data is coming from
                // live data so when we are adding any value in fav database  this onchanged method is called
                //and values in recycler view are changing to the favlist.
                //which is not the required behaviour.
                //so we have put the check condition.
                String sortOrder = SharedPreferencesUtility.getInstance(MainActivity.this).getString(SORT_ORDER, getResources().getString(R.string.popular_movies));
                if (Objects.equals(sortOrder, getResources().getString(R.string.favourite))) {
                    Log.d(LOG_TAG, "On changed called " + sortOrder);
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
        setTitle("Popular Movies");
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(true);
        movieViewModel.getPopularMovies().observe(this, this);
    }
    /**
     * Load TopRated Movies By making an Api call to tmdb.org.
     */
    private void loadTopRatedMovies() {
        setTitle("Top Rated Movies");
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(true);
        movieViewModel.getTopRatedMovies().observe(this, this);
    }
    /**
     * Load NowPlaying Movies By making an Api call to tmdb.org.
     */
    private void loadNowPlayingMovies() {
        setTitle("Now Playing Movies");
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(true);
        movieViewModel.getNowPlayingMovies().observe(this, this);
    }

    /**
     * Load Upcoming Movies By making an Api call to tmdb.org.
     */
    void loadUpComingMovies() {
        setTitle("UpComing Movies");
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(true);
        movieViewModel.getUpComingMovies().observe(this, this);
    }

    /**
     *
     * @param query  "is the string which we have to search for"
     */
    private void loadMovieSearch(String query) {
        mRecyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(true);
        movieViewModel.searchMovie(query).observe(this, this);
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
        SharedPreferencesUtility.getInstance(this).put(SORT_ORDER, getResources().getString(R.string.popular_movies));
        switch (item.getItemId()) {
            case R.id.popular:
                SharedPreferencesUtility.getInstance(this).put(SORT_ORDER, getResources().getString(R.string.popular_movies));
                checkSortOrder();
                return true;
            case R.id.now_playing:
                SharedPreferencesUtility.getInstance(this).put(SORT_ORDER, getResources().getString(R.string.now_playing_movies));
                checkSortOrder();
                return true;
            case R.id.up_coming:
                SharedPreferencesUtility.getInstance(this).put(SORT_ORDER, getResources().getString(R.string.upcoming_movies));
                checkSortOrder();
                return true;
            case R.id.top_rated:
                SharedPreferencesUtility.getInstance(this).put(SORT_ORDER, getResources().getString(R.string.top_rated_movies));
                checkSortOrder();
                return true;
            case R.id.favourite:
                SharedPreferencesUtility.getInstance(this).put(SORT_ORDER, getResources().getString(R.string.favourite));
                checkSortOrder();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This method check the value in sort-order and load the data accordingly in Recycler View
     */
    private void checkSortOrder() {
        String sortOrder = SharedPreferencesUtility.getInstance(this).getString(SORT_ORDER, getResources().getString(R.string.popular_movies));
        if (Objects.equals(sortOrder, getResources().getString(R.string.popular_movies))) {
            loadPopularMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.favourite))) {
            loadFavouriteMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.top_rated_movies))) {
            loadTopRatedMovies();

        } else if (Objects.equals(sortOrder, getResources().getString(R.string.upcoming_movies))) {
            loadUpComingMovies();
        } else if (Objects.equals(sortOrder, getResources().getString(R.string.now_playing_movies))) {
            loadNowPlayingMovies();
        }
    }

    public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
        switch (listNetworkResponse.getStatus()) {
            case SUCCESS:
                progressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setAdapter(new MoviesAdapter(MainActivity.this, listNetworkResponse.getData()));
                mRecyclerView.smoothScrollToPosition(0);
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                break;
            case LOADING:
                mRecyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, listNetworkResponse.getMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

}

