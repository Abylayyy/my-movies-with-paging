package kdo.one.mymoviewithpaginlibrary.ui.movies_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kdo.one.mymoviewithpaginlibrary.data.repository.NetworkState
import kdo.one.mymoviewithpaginlibrary.data.vo.MovieDetails

class SingleMovieViewModel(
    private val repository: MovieDetailsRepository,
    movieId: Int
): ViewModel() {
    private val disposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        repository.fetchSingleMovieDetails(disposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getMovieNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}