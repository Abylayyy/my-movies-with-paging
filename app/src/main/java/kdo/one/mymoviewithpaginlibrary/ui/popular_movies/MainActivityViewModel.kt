package kdo.one.mymoviewithpaginlibrary.ui.popular_movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import io.reactivex.disposables.CompositeDisposable
import kdo.one.mymoviewithpaginlibrary.data.repository.NetworkState
import kdo.one.mymoviewithpaginlibrary.data.vo.Movie

class MainActivityViewModel(private val repository: MoviePagedListRepository): ViewModel() {

    private val disposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        repository.fetchLiveMoviePagedList(disposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        repository.getNetworkState()
    }

    fun listIsEmpty() = moviePagedList.value?.isEmpty() ?: true

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}