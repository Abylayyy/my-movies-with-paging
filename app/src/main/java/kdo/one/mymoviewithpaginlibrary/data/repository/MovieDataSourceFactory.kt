package kdo.one.mymoviewithpaginlibrary.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import io.reactivex.disposables.CompositeDisposable
import kdo.one.mymoviewithpaginlibrary.data.api.TheMovieDBInterface
import kdo.one.mymoviewithpaginlibrary.data.vo.Movie

class MovieDataSourceFactory(
    private val apiService: TheMovieDBInterface,
    private val disposable: CompositeDisposable

): DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, disposable)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}