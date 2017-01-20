package com.example.jiovany.popularmovies;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.jiovany.popularmovies.utils.Constants;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jiovany on 1/17/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private List<Movie> moviesData;
    private OnItemClick onItemClickListener;

    public MoviesAdapter(OnItemClick onItemClick) {
        onItemClickListener = onItemClick;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutItemId = R.layout.movie_item;
        View inflatedView = LayoutInflater.from(context).inflate(layoutItemId, parent, false);
        return new MoviesViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        holder.bindData(moviesData.get(position), onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return moviesData != null ? moviesData.size() : 0;
    }

    public void setMoviesData(ArrayList<Movie> moviesData) {
        this.moviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePosterImage;
        private TextView title;
        private TextView voteAverage;
        private TextView releaseDate;
        private View itemContainer;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            itemContainer = itemView;
            moviePosterImage = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            title = (TextView) itemView.findViewById(R.id.tv_movie_title);
            voteAverage = (TextView) itemView.findViewById(R.id.tv_vote_average);
            releaseDate = (TextView) itemView.findViewById(R.id.tv_release_date);
        }

        public void bindData(final Movie movie, final OnItemClick listener) {
            Glide.with(moviePosterImage.getContext().getApplicationContext())
                    .load(movie.getPosterCompleteUrl(false)).centerCrop().crossFade().into(moviePosterImage);
            title.setText(movie.getTitle());
            voteAverage.setText(String.valueOf(movie.getVoteAverage()).concat(Constants.BLACK_STAR_UNICODE));
            releaseDate.setText(movie.getReleaseDate());
            itemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });
        }

    }
}
