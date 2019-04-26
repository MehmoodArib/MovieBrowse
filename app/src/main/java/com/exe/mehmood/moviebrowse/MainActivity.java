package com.exe.mehmood.moviebrowse;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exe.mehmood.moviebrowse.adapter.MoviesAdapter;
import com.exe.mehmood.moviebrowse.data.ViewModels.MainActivityViewModel;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.exe.mehmood.moviebrowse.model.NetworkResponse;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
/**
 * This is the Main Activity consisting of five RecyclerViews.
 *
 */

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private LinearLayout mLinearLayout;
    private ProgressBar mProgressBarPopularMovies, mProgressBarNowPlayingMovies, mProgressBarUpcomingMovies,
            mProgressBarTopRatedMovies;
    private TextView mTextViewPopularMovies, mTextViewNowPlayingMovies, mTextViewUpcomingMovies,
            mTextViewTopRatedMovies;
    private Menu menu;
    private RecyclerView mRecyclerViewPopularMovies, mRecyclerViewTopRatedMovies, mRecyclerViewNowPlayingMovies,
            mRecyclerViewFavouriteMovies, mRecyclerViewUpComingMovies;
    private MoviesAdapter mMoviesAdapterPopularMovies, mMoviesAdapterTopRatedMovies, mMoviesAdapterNowPlayingMovies,
            mMoviesAdapterFavouriteMovies, mMoviesAdapterUpComingMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        initViews();
        loadUpComingMovies();
        loadFavouriteMovies();
        loadNowPlayingMovies();
        loadPopularMovies();
        loadTopRatedMovies();
    }

    /**
     * Initialize Views
     */
    private void initViews() {
        mLinearLayout = findViewById(R.id.main_content);

        mRecyclerViewFavouriteMovies = findViewById(R.id.recycler_view_favouriteMovies);
        mRecyclerViewUpComingMovies = findViewById(R.id.recycler_view_upComingMovies);
        mRecyclerViewNowPlayingMovies = findViewById(R.id.recycler_view_nowPlayingMovies);
        mRecyclerViewTopRatedMovies = findViewById(R.id.recycler_view_topRatedMovies);
        mRecyclerViewPopularMovies = findViewById(R.id.recycler_view_popularMovies);

        mProgressBarPopularMovies = findViewById(R.id.progress_popularMovies);
        mProgressBarUpcomingMovies = findViewById(R.id.progress_upComingMovies);
        mProgressBarNowPlayingMovies = findViewById(R.id.progress_nowPlayingMovies);
        mProgressBarTopRatedMovies = findViewById(R.id.progress_TopRatedMovies);

        mTextViewPopularMovies = findViewById(R.id.textView_popularMovies);
        mTextViewUpcomingMovies = findViewById(R.id.textView_upComingMovies);
        mTextViewNowPlayingMovies = findViewById(R.id.textView_nowPlayingMovies);
        mTextViewTopRatedMovies = findViewById(R.id.textView_TopRatedMovies);


    }

    /**
     * Load saved favourite movies from the local database
     */

    private void loadFavouriteMovies() {
        mainActivityViewModel.getAllFavMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMoviesAdapterFavouriteMovies = new MoviesAdapter(MainActivity.this, movies);
                mRecyclerViewFavouriteMovies.setAdapter(mMoviesAdapterFavouriteMovies);
                mMoviesAdapterFavouriteMovies.notifyDataSetChanged();
                mRecyclerViewFavouriteMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }
        });
    }

    /**
     * Load Popular Movies By making an Api call to tmdb.org.
     */
    private void loadPopularMovies() {
        mainActivityViewModel.getPopularMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mRecyclerViewPopularMovies.setVisibility(View.VISIBLE);
                        mTextViewPopularMovies.setVisibility(View.GONE);
                        mProgressBarPopularMovies.setVisibility(View.GONE);

                        mMoviesAdapterPopularMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewPopularMovies.setAdapter(mMoviesAdapterPopularMovies);
                        mRecyclerViewPopularMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        mRecyclerViewPopularMovies.smoothScrollToPosition(0);
                        break;
                    case LOADING:
                        mRecyclerViewPopularMovies.setVisibility(View.GONE);
                        mTextViewPopularMovies.setVisibility(View.GONE);
                        mProgressBarPopularMovies.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mRecyclerViewPopularMovies.setVisibility(View.GONE);
                        mTextViewPopularMovies.setVisibility(View.VISIBLE);
                        mProgressBarPopularMovies.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * Load TopRated Movies By making an Api call to tmdb.org.
     */
    private void loadTopRatedMovies() {
        mainActivityViewModel.getTopRatedMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mRecyclerViewTopRatedMovies.setVisibility(View.VISIBLE);
                        mTextViewTopRatedMovies.setVisibility(View.GONE);
                        mProgressBarTopRatedMovies.setVisibility(View.GONE);

                        mMoviesAdapterTopRatedMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewTopRatedMovies.setAdapter(mMoviesAdapterTopRatedMovies);
                        mRecyclerViewTopRatedMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        mRecyclerViewTopRatedMovies.smoothScrollToPosition(0);
                        break;
                    case LOADING:
                        mRecyclerViewTopRatedMovies.setVisibility(View.GONE);
                        mTextViewTopRatedMovies.setVisibility(View.GONE);
                        mProgressBarTopRatedMovies.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mRecyclerViewTopRatedMovies.setVisibility(View.GONE);
                        mTextViewTopRatedMovies.setVisibility(View.VISIBLE);
                        mProgressBarTopRatedMovies.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * Load NowPlaying Movies By making an Api call to tmdb.org.
     */
    private void loadNowPlayingMovies() {
        mainActivityViewModel.getNowPlayingMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mRecyclerViewNowPlayingMovies.setVisibility(View.VISIBLE);
                        mTextViewNowPlayingMovies.setVisibility(View.GONE);
                        mProgressBarNowPlayingMovies.setVisibility(View.GONE);

                        mMoviesAdapterNowPlayingMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewNowPlayingMovies.setAdapter(mMoviesAdapterNowPlayingMovies);
                        mRecyclerViewNowPlayingMovies.smoothScrollToPosition(0);
                        mRecyclerViewNowPlayingMovies.setLayoutManager(new GridLayoutManager(MainActivity.this, 1, RecyclerView.HORIZONTAL, false));
                        break;
                    case LOADING:
                        mRecyclerViewNowPlayingMovies.setVisibility(View.GONE);
                        mTextViewNowPlayingMovies.setVisibility(View.GONE);
                        mProgressBarNowPlayingMovies.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mRecyclerViewNowPlayingMovies.setVisibility(View.GONE);
                        mTextViewNowPlayingMovies.setVisibility(View.VISIBLE);
                        mProgressBarNowPlayingMovies.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    /**
     * Load Upcoming Movies By making an Api call to tmdb.org.
     */
    void loadUpComingMovies() {
        mainActivityViewModel.getUpComingMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mRecyclerViewUpComingMovies.setVisibility(View.VISIBLE);
                        mTextViewUpcomingMovies.setVisibility(View.GONE);
                        mProgressBarUpcomingMovies.setVisibility(View.GONE);

                        mMoviesAdapterUpComingMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewUpComingMovies.setAdapter(mMoviesAdapterUpComingMovies);
                        mRecyclerViewUpComingMovies.smoothScrollToPosition(0);
                        mRecyclerViewUpComingMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        break;
                    case LOADING:
                        mRecyclerViewUpComingMovies.setVisibility(View.GONE);
                        mTextViewUpcomingMovies.setVisibility(View.GONE);
                        mProgressBarUpcomingMovies.setVisibility(View.VISIBLE);
                        break;
                    case ERROR:
                        mRecyclerViewUpComingMovies.setVisibility(View.GONE);
                        mTextViewUpcomingMovies.setVisibility(View.VISIBLE);
                        mProgressBarUpcomingMovies.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Fragment fragment = new SearchFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, fragment, "Tag");
                transaction.addToBackStack("SearchFragment");
                mLinearLayout.setVisibility(View.GONE);
                item.setVisible(false);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        mLinearLayout.setVisibility(View.VISIBLE);
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
        super.onBackPressed();
    }
}


