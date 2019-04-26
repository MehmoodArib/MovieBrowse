package com.exe.mehmood.moviebrowse.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.exe.mehmood.moviebrowse.BuildConfig;
import com.exe.mehmood.moviebrowse.MainActivity;
import com.exe.mehmood.moviebrowse.api.Client;
import com.exe.mehmood.moviebrowse.api.Service;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.exe.mehmood.moviebrowse.model.MoviesResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/***
 * Repository Containing All methods of local database and Api.
 */
class MainActivityRepository {
    private String DB_NAME = "db_task";
    private LiveData<List<Movie>> movies;


    private FavDataBase favDataBase;
    MainActivityRepository(Context context) {
        favDataBase = Room.databaseBuilder(context, FavDataBase.class, DB_NAME).build();
        movies = favDataBase.myFavDao().fetchAllFavMovie();
    }


    /**
     * Load Upcoming Movies By making an Api call to tmdb.org.
     */
    public LiveData<NetworkResponse<List<Movie>>> loadUpComingMovies() {
        final MutableLiveData<NetworkResponse<List<Movie>>> upcomingMovies = new MutableLiveData<>();
        upcomingMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.LOADING, null, null));
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getUpComingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    upcomingMovies.setValue(new NetworkResponse<>(NetworkResponse.Status.SUCCESS, response.body().getResults(), null));
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    upcomingMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Error fetching data from server"));
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            upcomingMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Unable to make a network request!"));
        }
        return upcomingMovies;
    }

    /**
     * Load Popular Movies By making an Api call to tmdb.org.
     */
    public LiveData<NetworkResponse<List<Movie>>> loadPopularMovies() {
        final MutableLiveData<NetworkResponse<List<Movie>>> popularMovies = new MutableLiveData<>();
        popularMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.LOADING, null, null));
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getUpComingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    popularMovies.setValue(new NetworkResponse<>(NetworkResponse.Status.SUCCESS, response.body().getResults(), null));
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    popularMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Error fetching data from server"));
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            popularMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Unable to make a network request!"));
        }
        return popularMovies;
    }

    public LiveData<NetworkResponse<List<Movie>>> loadMovieSearch(String query) {
        final MutableLiveData<NetworkResponse<List<Movie>>> searchResult = new MutableLiveData<>();
        searchResult.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.LOADING, null, null));
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.findMovie(query, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    searchResult.setValue(new NetworkResponse<>(NetworkResponse.Status.SUCCESS, response.body().getResults(), null));
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    searchResult.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Error fetching data from server"));
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            searchResult.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Unable to make a network request!"));
        }
        return searchResult;
    }

    /**
     * Load Upcoming Movies By making an Api call to tmdb.org.
     */
    public LiveData<NetworkResponse<List<Movie>>> loadTopRatedMovies() {
        final MutableLiveData<NetworkResponse<List<Movie>>> topRatedMovies = new MutableLiveData<>();
        topRatedMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.LOADING, null, null));
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    topRatedMovies.setValue(new NetworkResponse<>(NetworkResponse.Status.SUCCESS, response.body().getResults(), null));
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    topRatedMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Error fetching data from server"));
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            topRatedMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Unable to make a network request!"));
        }
        return topRatedMovies;
    }

    /**
     * Load NowPlaying Movies By making an Api call to tmdb.org.
     */
    public LiveData<NetworkResponse<List<Movie>>> loadNowPlayingMovies() {
        final MutableLiveData<NetworkResponse<List<Movie>>> nowPlayingMovies = new MutableLiveData<>();
        nowPlayingMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.LOADING, null, null));
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<MoviesResponse> call = apiService.getNowPlayingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    assert response.body() != null;
                    nowPlayingMovies.setValue(new NetworkResponse<>(NetworkResponse.Status.SUCCESS, response.body().getResults(), null));
                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d("Error", t.getMessage());
                    //Toast.makeText(MainActivity.this, "Error Fetching Data!", Toast.LENGTH_SHORT).show();
                    nowPlayingMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Error fetching data from server"));
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
            nowPlayingMovies.setValue(new NetworkResponse<List<Movie>>(NetworkResponse.Status.ERROR, null, "Unable to make a network request!"));
        }
        return nowPlayingMovies;
    }

    void insertMovie(Movie movie) {

        insertMovieAsyncTask(movie);
    }

    void deleteMovie(Movie movie) {

        deleteMovieAsyncTask(movie);
    }

    @SuppressLint("StaticFieldLeak")
    private void insertMovieAsyncTask(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                favDataBase.myFavDao().insertFavMovie(movie);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void deleteMovieAsyncTask(final Movie movie) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                favDataBase.myFavDao().deleteFav(movie);
                return null;
            }
        }.execute();
    }

    LiveData<Movie> getMovie(int id) {
        return favDataBase.myFavDao().getMovie(id);
    }

    LiveData<List<Movie>> getMovies() {
        return movies;
    }
}