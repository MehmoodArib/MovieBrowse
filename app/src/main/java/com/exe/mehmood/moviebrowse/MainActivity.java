package com.exe.mehmood.moviebrowse;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.exe.mehmood.moviebrowse.adapter.MoviesAdapter;
import com.exe.mehmood.moviebrowse.data.MovieViewModel;
import com.exe.mehmood.moviebrowse.data.NetworkResponse;
import com.exe.mehmood.moviebrowse.model.Movie;

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
 * This is the Main Activity consisting of RecyclerView.
 * We store the sort-order in shared preference and according to the sort-order we load the values
 * in recycler view.
 */

public class MainActivity extends AppCompatActivity {
    MovieViewModel movieViewModel;
    LinearLayout mLinearLayout;
    private Menu menu;
    private RecyclerView mRecyclerViewPopularMovies, mRecyclerViewTopRatedMovies, mRecyclerViewNowPlayingMovies,
            mRecyclerViewFavouriteMovies, mRecyclerViewUpComingMovies;
    private MoviesAdapter mMoviesAdapterPopularMovies, mMoviesAdapterTopRatedMovies, mMoviesAdapterNowPlayingMovies,
            mMoviesAdapterFavouriteMovies, mMoviesAdapterUpComingMovies;

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
        mRecyclerViewFavouriteMovies = findViewById(R.id.recycler_view_favouriteMovies);
        mRecyclerViewUpComingMovies = findViewById(R.id.recycler_view_upComingMovies);
        mRecyclerViewNowPlayingMovies = findViewById(R.id.recycler_view_nowPlayingMovies);
        mRecyclerViewTopRatedMovies = findViewById(R.id.recycler_view_topRatedMovies);
        mRecyclerViewPopularMovies = findViewById(R.id.recycler_view_popularMovies);
        mLinearLayout = findViewById(R.id.main_content);
        loadUpComingMovies();
        loadFavouriteMovies();
        loadNowPlayingMovies();
        loadPopularMovies();
        loadTopRatedMovies();
    }

    /**
     * Load saved favourite movies from the local database
     */

    private void loadFavouriteMovies() {
        movieViewModel.getAllFavMovies().observe(this, new Observer<List<Movie>>() {
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
        movieViewModel.getPopularMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mMoviesAdapterPopularMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewPopularMovies.setAdapter(mMoviesAdapterPopularMovies);
                        mRecyclerViewPopularMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        mRecyclerViewPopularMovies.smoothScrollToPosition(0);
                        break;
                    case LOADING:
                        Toast.makeText(MainActivity.this, "Loading Favorite", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(MainActivity.this, listNetworkResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * Load TopRated Movies By making an Api call to tmdb.org.
     */
    private void loadTopRatedMovies() {
        movieViewModel.getTopRatedMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mMoviesAdapterTopRatedMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewTopRatedMovies.setAdapter(mMoviesAdapterTopRatedMovies);
                        mRecyclerViewTopRatedMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        mRecyclerViewTopRatedMovies.smoothScrollToPosition(0);
                        break;
                    case LOADING:
                        Toast.makeText(MainActivity.this, "Loading TopRated", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(MainActivity.this, listNetworkResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * Load NowPlaying Movies By making an Api call to tmdb.org.
     */
    private void loadNowPlayingMovies() {
        movieViewModel.getNowPlayingMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mMoviesAdapterNowPlayingMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewNowPlayingMovies.setAdapter(mMoviesAdapterNowPlayingMovies);
                        mRecyclerViewNowPlayingMovies.smoothScrollToPosition(0);
                        mRecyclerViewNowPlayingMovies.setLayoutManager(new GridLayoutManager(MainActivity.this, 1, RecyclerView.HORIZONTAL, false));

                        break;
                    case LOADING:
                        Toast.makeText(MainActivity.this, "Loading NowPlaying", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(MainActivity.this, listNetworkResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * Load Upcoming Movies By making an Api call to tmdb.org.
     */
    void loadUpComingMovies() {
        movieViewModel.getUpComingMovies().observe(this, new Observer<NetworkResponse<List<Movie>>>() {
            @Override
            public void onChanged(NetworkResponse<List<Movie>> listNetworkResponse) {
                switch (listNetworkResponse.getStatus()) {
                    case SUCCESS:
                        mMoviesAdapterUpComingMovies = new MoviesAdapter(MainActivity.this, listNetworkResponse.getData());
                        mRecyclerViewUpComingMovies.setAdapter(mMoviesAdapterUpComingMovies);
                        mRecyclerViewUpComingMovies.smoothScrollToPosition(0);
                        mRecyclerViewUpComingMovies.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        break;
                    case LOADING:
                        Toast.makeText(MainActivity.this, "Upcoming Favorite", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR:
                        Toast.makeText(MainActivity.this, listNetworkResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                transaction.addToBackStack("true");
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


