package kdo.one.mymoviewithpaginlibrary.ui.popular_movies

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kdo.one.mymoviewithpaginlibrary.R
import kdo.one.mymoviewithpaginlibrary.data.api.POSTER_BASE_URL
import kdo.one.mymoviewithpaginlibrary.data.repository.NetworkState
import kdo.one.mymoviewithpaginlibrary.data.vo.Movie
import kdo.one.mymoviewithpaginlibrary.ui.movies_details.SingleMovie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class MoviePagedListAdapter(val context: Context): PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallBack()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == MOVIE_VIEW_TYPE) {
            view = inflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.network_state_item, parent, false)
            return NetWorkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetWorkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0

    override fun getItemViewType(position: Int) = if (hasExtraRow() && position == itemCount - 1) NETWORK_VIEW_TYPE else MOVIE_VIEW_TYPE

    class MovieDiffCallBack: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie?, context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context).load(moviePosterURL).into(itemView.cv_iv_movie_poster)

            itemView.setOnClickListener{
                Intent(context, SingleMovie::class.java).also {
                    it.putExtra("id", movie?.id)
                    context.startActivity(it)
                }
            }
        }
    }

    class NetWorkStateItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(state: NetworkState?) {
            when (state!!) {
                NetworkState.LOADING -> itemView.progress_bar_item.visibility = View.VISIBLE
                NetworkState.ERROR -> {
                    itemView.error_msg_item.visibility = View.VISIBLE
                    itemView.error_msg_item.text = state.msg
                }
                NetworkState.ENDOFLIST -> {
                    itemView.error_msg_item.visibility = View.VISIBLE
                    itemView.error_msg_item.text = state.msg
                }
                else -> {
                    itemView.progress_bar_item.visibility = View.GONE
                    itemView.error_msg_item.visibility = View.GONE
                }
            }
        }
    }

    fun setNetworkState(state: NetworkState) {
        val previous = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = state
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previous != state) {
            notifyItemChanged(itemCount - 1)
        }
    }
}


























