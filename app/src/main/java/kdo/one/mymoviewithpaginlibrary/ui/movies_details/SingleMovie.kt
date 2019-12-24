package kdo.one.mymoviewithpaginlibrary.ui.movies_details

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kdo.one.mymoviewithpaginlibrary.R
import kdo.one.mymoviewithpaginlibrary.data.api.POSTER_BASE_URL
import kdo.one.mymoviewithpaginlibrary.data.api.TheMovieDBClient
import kdo.one.mymoviewithpaginlibrary.data.repository.NetworkState
import kdo.one.mymoviewithpaginlibrary.data.vo.MovieDetails
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

@Suppress("UNCHECKED_CAST")
class SingleMovie : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var repository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService = TheMovieDBClient.getClient()
        repository = MovieDetailsRepository(apiService)
        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer { binUi(it) })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("SetTextI18n")
    private fun binUi(it: MovieDetails?) {
        movie_title.text = it?.title
        movie_tagline.text = it?.tagline
        movie_release_date.text = it?.releaseDate
        movie_rating.text = it?.rating.toString()
        movie_runtime.text = it?.runtime.toString() + " minutes"
        movie_overview.text = it?.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it?.budget)
        movie_revenue.text = formatCurrency.format(it?.revenue)

        val moviePosterUrl = POSTER_BASE_URL + it?.posterPath
        Log.d("DETAIL_IMAGE", moviePosterUrl)
        Glide.with(this).load(moviePosterUrl).into(iv_movie_poster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(repository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
