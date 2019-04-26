package com.exe.mehmood.moviebrowse.data.ViewModels;

import android.app.Application;

import com.exe.mehmood.moviebrowse.data.Repositories.MainActivityRepository;
import com.exe.mehmood.moviebrowse.model.Movie;
import com.exe.mehmood.moviebrowse.model.NetworkResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/***
 * View Model of Main Activity.
 */
public class MainActivityViewModel extends AndroidViewModel {
    private MainActivityRepository mainActivityRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mainActivityRepository = new MainActivityRepository(application);
    }

    public LiveData<List<Movie>> getAllFavMovies() {
        return mainActivityRepository.getMovies();
    }

    public void deleteMovie(Movie movie) {
        mainActivityRepository.deleteMovie(movie);
    }

    public void insert(Movie movie) {
        mainActivityRepository.insertMovie(movie);
    }

    public LiveData<Movie> getMovie(Integer id) {
        return mainActivityRepository.getMovie(id);
    }

    public LiveData<NetworkResponse<List<Movie>>> getUpComingMovies(){
        return mainActivityRepository.loadUpComingMovies();
    }

    public LiveData<NetworkResponse<List<Movie>>> getNowPlayingMovies(){
        return mainActivityRepository.loadNowPlayingMovies();
    }

    public LiveData<NetworkResponse<List<Movie>>> getTopRatedMovies(){
        return mainActivityRepository.loadTopRatedMovies();
    }

    public LiveData<NetworkResponse<List<Movie>>> getPopularMovies(){
        return mainActivityRepository.loadPopularMovies();
    }

    public LiveData<NetworkResponse<List<Movie>>> searchMovie(String query){
        return mainActivityRepository.loadMovieSearch(query);
    }
}
