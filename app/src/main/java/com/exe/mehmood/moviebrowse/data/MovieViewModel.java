package com.exe.mehmood.moviebrowse.data;

import android.app.Application;

import com.exe.mehmood.moviebrowse.model.Movie;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MovieViewModel extends AndroidViewModel {
    private FavRepository favRepository;
    public MovieViewModel(@NonNull Application application) {
        super(application);
        favRepository = new FavRepository(application);
    }

    public LiveData<List<Movie>> getAllFavMovies() {
        return favRepository.getMovies();
    }

    public void deleteMovie(Movie movie) {
        favRepository.deleteMovie(movie);
    }

    public void insert(Movie movie) {
        favRepository.insertMovie(movie);
    }

    public LiveData<Movie> getMovie(Integer id) {
        return favRepository.getMovie(id);
    }

    public LiveData<NetworkResponse<List<Movie>>> getUpComingMovies(){
        return favRepository.loadUpComingMovies();
    }
    public LiveData<NetworkResponse<List<Movie>>> getNowPlayingMovies(){
        return favRepository.loadNowPlayingMovies();
    }
    public LiveData<NetworkResponse<List<Movie>>> getTopRatedMovies(){
        return favRepository.loadTopRatedMovies();
    }
    public LiveData<NetworkResponse<List<Movie>>> getPopularMovies(){
        return favRepository.loadPopularMovies();
    }
    public LiveData<NetworkResponse<List<Movie>>> searchMovie(String query){
        return favRepository.loadMovieSearch(query);
    }
}
