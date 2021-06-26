package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    String ytKey;

    public static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=5f23ae969ce265a0f28e1c99ad2b4c78";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        Context context = this.getApplicationContext();


        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());
        binding.rvDate.setText(movie.getReleaseDate());

        Glide.with(this.getApplicationContext()).load(movie.getBackdropPath()).placeholder(R.drawable.placeholder_land).into(binding.tvPic);

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage / 2.0f);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(MOVIE_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0)
                        return;
                    ytKey = results.getJSONObject(0).getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
            }

        });

        binding.tvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DNF","Please");

                // make sure the position is valid, i.e. actually exists in the view
                // get the movie at the position, this won't work if the class is static
                // create intent for the new activity
                Intent intent = new Intent(context, MovieTrailerActivity.class);
                intent.putExtra("TrailerKey", ytKey);
                // serialize the movie using parceler, use its short name as a ke
                // show the activity
                startActivity(intent);
            }
        });
    }


    }

