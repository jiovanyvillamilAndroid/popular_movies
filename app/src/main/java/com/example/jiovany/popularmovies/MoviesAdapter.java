package com.example.jiovany.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


/**
 * Created by jiovany on 1/17/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    Movie[] moviesData;

    public MoviesAdapter() {
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
        holder.bindData(moviesData[position]);
    }

    @Override
    public int getItemCount() {
        return moviesData != null ? moviesData.length : 0;
    }

    public void setMoviesData(Movie[] moviesData) {
        this.moviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePosterImage;
        private TextView title;
        private TextView voteAverage;
        private TextView releaseDate;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            moviePosterImage = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            title = (TextView) itemView.findViewById(R.id.tv_movie_title);
            voteAverage = (TextView) itemView.findViewById(R.id.tv_vote_average);
            releaseDate = (TextView) itemView.findViewById(R.id.tv_release_date);
        }

        public void bindData(Movie movie) {
            Glide.with(moviePosterImage.getContext().getApplicationContext())
                    .load(movie.getPosterCompleteUrl(false))
                    .centerCrop()
                    .crossFade()
                    .into(moviePosterImage);
            title.setText(movie.getTitle());
            voteAverage.setText(String.valueOf(movie.getVoteAverage()));
            releaseDate.setText(movie.getReleaseDate());
        }

    }
}