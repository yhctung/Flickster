package com.ck.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ck.flickster.DetailActivity;
import com.ck.flickster.R;
import com.ck.flickster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    // needed for rendering
    Context context;

    // List of movies
    List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // creates view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // for debugging
        // Log.d("smile", "onCreateViewHolder");

        // use layout to create view
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        // return new viewholder
        return new ViewHolder(view);
    }

    // binds view to item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // for debugging
        // Log.d("smile", "onBindViewHolder" + position);

        // get data at specific position
        Movie movie = movies.get(position);

        // Bind the movie data into the view holder
        holder.bind(movie);
    }

    // get number of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // objects in the view
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        RelativeLayout container;

        public ViewHolder(View itemView){
            super(itemView);

            // link views and ids
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Movie movie){

            // add title and overview to view
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageUrl;
            int placeholderID;
            ImageView imageView;

            // Go to the backdrop path if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
                // placeholderID = R.drawable
                // imageView = ivBackdrop;
            }
            else {  // default portrait : poster
                imageUrl = movie.getPosterPath();
                // placeholderID = R.drawable
                // imageView = ivPoster;
            }


            // load it
            Glide.with(context).load(imageUrl).into(ivPoster);

            /* Not working for some reason
            // load image using Glide
            GlideApp.with(context)
                    .load(imageUrl)
                    .transform(new RoundedCornersTransformation(30, 0))
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .into(imageView);
            */

            // Add click listener on the whole row
            container.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    // go to detail activity
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("title", movie.getTitle());
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                }
            });

        }

    }
}
