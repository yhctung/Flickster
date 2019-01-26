package com.ck.flickster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ck.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.google.android.youtube.player.YouTubePlayer.Provider;

public class DetailActivity extends YouTubeBaseActivity {

    private static final String YOUTUBE_API_KEY = "AIzaSyBVtQN9gvismCQhaCkW4m7Y5WJf7LXBS_8";
    private static final String TRAILERS_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youtubePlayerView;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // find all views
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        youtubePlayerView = findViewById(R.id.player);

        // get the data
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        // set title, overview, rating
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getVoteAverage());

        AsyncHttpClient client = new AsyncHttpClient();

        // get the video
        client.get(String.format(TRAILERS_API, movie.getmovieId()), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    if (results.length() == 0){
                        // display poster
                    }
                    else if (movie.getVoteAverage() > 5) {
                        JSONObject movieTrailer = results.getJSONObject(0);
                        String youtubeKey = movieTrailer.getString("key");
                        initializeYoutube(youtubeKey);
                    }
                    else if (movie.getVoteAverage() <= 5) {
                        JSONObject movieTrailer = results.getJSONObject(0);
                        final String youtubeKey = movieTrailer.getString("key");
                        /* // Image -> click to see video
                        // how to use onBindViewHolder() here?

                        ImageView ivPoster;
                        ivPoster = itemView.findViewById(R.id.ivPoster);

                        // Get image url
                        String imageUrl;
                        // Go to the backdrop path if phone is in landscape
                        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            imageUrl = movie.getBackdropPath();
                        }
                        else {  // default portrait : poster
                            imageUrl = movie.getPosterPath();
                        }

                        // Load poster image
                        Glide.with(context).load(imageUrl).into(ivPoster);

                        // Set on click listener
                        ivPoster.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view){
                            initializeYoutube(youtubeKey);
                        }
                        });
                        */
                        initializeYoutube(youtubeKey);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    // starting the actual video
    private void initializeYoutube(final String youtubeKey) {
        youtubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //Log.d("smile", "on init success");
                if (movie.getVoteAverage() > 5) {       // play automatically if rating > 5
                    youTubePlayer.loadVideo(youtubeKey);
                } else if (movie.getVoteAverage() <= 5) {   // wait if < 5
                    youTubePlayer.cueVideo(youtubeKey);
                }
            }

            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.e("smile", "failed to initialize youtube video");
            }
        });
    }
}