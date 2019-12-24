package kdo.one.mymoviewithpaginlibrary.ui.movies_details

import androidx.lifecycle.LiveData
import io.reactivex.disposables.CompositeDisposable
import kdo.one.mymoviewithpaginlibrary.data.api.TheMovieDBInterface
import kdo.one.mymoviewithpaginlibrary.data.repository.MovieNetworkDataSource
import kdo.one.mymoviewithpaginlibrary.data.repository.NetworkState
import kdo.one.mymoviewithpaginlibrary.data.vo.MovieDetails

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {
    lateinit var movieNetworkDataSource: MovieNetworkDataSource

    fun fetchSingleMovieDetails(disposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieNetworkDataSource = MovieNetworkDataSource(apiService, disposable)
        movieNetworkDataSource.fetchMovieDetails(movieId)
        return movieNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieNetworkState(): LiveData<NetworkState> {
        return movieNetworkDataSource.networkState
    }
}