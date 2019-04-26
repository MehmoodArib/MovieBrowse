package com.exe.mehmood.moviebrowse.data.ViewModels;

import android.app.Application;

import com.exe.mehmood.moviebrowse.data.Repositories.DetailActivityRepository;
import com.exe.mehmood.moviebrowse.model.NetworkResponse;
import com.exe.mehmood.moviebrowse.model.Trailer;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DetailActivityViewModel extends AndroidViewModel {
    private DetailActivityRepository detailActivityRepository;

    public DetailActivityViewModel(@NonNull Application application) {
        super(application);
        detailActivityRepository = new DetailActivityRepository();
    }

    public LiveData<NetworkResponse<List<Trailer>>> getMovieTrailer(int movieId) {
        return detailActivityRepository.loadMovieTrailer(movieId);
    }
}
