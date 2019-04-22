package com.exe.mehmood.moviebrowse.data;

import com.exe.mehmood.moviebrowse.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MyFavDao {
    @Insert
    void insertFavMovie(Movie movie);

    @Query("SELECT * FROM Movie")
    LiveData<List<Movie>> fetchAllFavMovie();

    @Query("SELECT * FROM Movie WHERE id =:id")
    LiveData<Movie> getMovie(int id);

    @Delete
    void deleteFav(Movie movie);
}
