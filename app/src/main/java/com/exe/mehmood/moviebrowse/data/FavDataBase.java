package com.exe.mehmood.moviebrowse.data;

import com.exe.mehmood.moviebrowse.model.Movie;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavDataBase extends RoomDatabase {
    public abstract MyFavDao myFavDao();
}