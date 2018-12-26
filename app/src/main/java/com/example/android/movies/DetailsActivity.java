package com.example.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {


    TextView originalTitle;
    TextView releaseDate;
    TextView userRating;
    TextView overView;
    ImageView imageView;

    static String mtitle;
    static String mrating;
    static String mimage;
    static String mdate;
    static String mover_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        originalTitle = (TextView) findViewById(R.id.tv_original_title);
        releaseDate = (TextView) findViewById(R.id.release_date);
        userRating = (TextView) findViewById(R.id.user_rating);
        overView = (TextView) findViewById(R.id.overview);
        imageView = (ImageView) findViewById(R.id.iv_movie_poster);

        Intent i = getIntent();

        originalTitle.setText(mtitle);
        releaseDate.setText(mdate);
        userRating.setText(mrating);
        overView.setText(mover_view);

        String imageUrl = "https://image.tmdb.org/t/p/w600_and_h900_bestv2";
        Picasso.with(DetailsActivity.this)
                .load(imageUrl + mimage)
                .into(imageView);
    }

    public static void getItems(String title, String rating, String image, String date, String over_view) {
        mtitle = title;
        mover_view = over_view;
        mrating = rating;
        mimage = image;
        mdate = date;
    }
}
