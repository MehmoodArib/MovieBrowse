package com.exe.mehmood.moviebrowse.data.Repositories;

import com.exe.mehmood.moviebrowse.BuildConfig;
import com.exe.mehmood.moviebrowse.api.Client;
import com.exe.mehmood.moviebrowse.api.Service;
import com.exe.mehmood.moviebrowse.model.NetworkResponse;
import com.exe.mehmood.moviebrowse.model.Trailer;
import com.exe.mehmood.moviebrowse.model.TrailerResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivityRepository {
    public LiveData<NetworkResponse<List<Trailer>>> loadMovieTrailer(int movieId) {
        final MutableLiveData<NetworkResponse<List<Trailer>>> movieTrailers = new MutableLiveData<>();
        movieTrailers.setValue(new NetworkResponse<List<Trailer>>(NetworkResponse.Status.LOADING, null, null));
        try {
            Service apiService =
                    Client.getClient().create(Service.class);
            Call<TrailerResponse> call = apiService.getMovieTrailer(movieId, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                    assert response.body() != null;
                    movieTrailers.setValue(new NetworkResponse<>(NetworkResponse.Status.SUCCESS, response.body().getResults(), null));
                }

                @Override
                public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                    movieTrailers.setValue(new NetworkResponse<List<Trailer>>(NetworkResponse.Status.ERROR, null, "Error fetching data from server"));
                }
            });
        } catch (Exception e) {
            movieTrailers.setValue(new NetworkResponse<List<Trailer>>(NetworkResponse.Status.ERROR, null, "Unable to make a network request!"));
        }
        return movieTrailers;
    }
}
