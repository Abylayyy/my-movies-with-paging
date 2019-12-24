package kdo.one.mymoviewithpaginlibrary.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import kdo.one.mymoviewithpaginlibrary.data.api.POST_PER_PAGE
import kdo.one.mymoviewithpaginlibrary.data.api.TheMovieDBInterface
import kdo.one.mymoviewithpaginlibrary.data.repository.MovieDataSource
import kdo.one.mymoviewithpaginlibrary.data.repository.MovieDataSourceFactory
import kdo.one.mymoviewithpaginlibrary.data.repository.NetworkState
import kdo.one.mymoviewithpaginlibrary.data.vo.Movie

class MoviePagedListRepository(private val apiService: TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(disposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, disposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()
        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }
}