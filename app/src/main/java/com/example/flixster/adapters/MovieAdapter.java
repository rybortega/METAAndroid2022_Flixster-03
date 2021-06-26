package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;

    ItemMovieBinding binding;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    //Inflates layout and returns inside of viewholder.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = ItemMovieBinding.inflate(LayoutInflater.from(context), parent, false);

        View movieView = binding.getRoot();

        //View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    //Populate view data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Get the movie at the passed position
        Movie movie = movies.get(position);
        // Bind movie data into ViewHolder
        holder.bind(movie);
    }

    //Return total number of movies
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            binding.tvTitle.setText(movie.getTitle());
            binding.tvOverview.setText(movie.getOverview());
            String imageURL;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                Glide.with(context).load(imageURL).transform(new RoundedCornersTransformation(30, 20)).placeholder(R.drawable.placeholder).into(binding.ivPoster);
            } else {
                imageURL = movie.getPosterPath();
                Glide.with(context).load(imageURL).transform(new RoundedCornersTransformation(30, 20)).placeholder(R.drawable.placeholder_land).into(binding.ivPoster);
            }

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}

