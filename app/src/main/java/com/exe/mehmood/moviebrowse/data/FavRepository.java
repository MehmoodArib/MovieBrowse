package com.exe.mehmood.moviebrowse.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.exe.mehmood.moviebrowse.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

class FavRepository {
    private String DB_NAME = "db_task";
    private LiveData<List<Movie>> movies;

    private FavDataBase favDataBase;

    FavRepository(Context context) {
        favDataBase = Room.databaseBuilder(context, FavDataBase.class, DB_NAME).build();
        movies = favDataBase.myFavDao().fetchAllFavMovie();
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