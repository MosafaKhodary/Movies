package com.example.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity implements PostersAdapter.ListItemClickListener {
    private static String REQUESTED_URL =
            "http://api.themoviedb.org/3/movie/popular?api_key=";

    RecyclerView mPostersRecyclerView;
    PostersAdapter mPostersAdapter;
    Toast mToast;

    static final String[] posters = new String[20];
    static final String[] originalTitle = new String[20];
    static final String[] overView = new String[20];
    static final String[] releaseDate = new String[20];
    static final String[] voteAverage = new String[20];


    MoviesAsyncTask moviesAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPostersRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mPostersAdapter = new PostersAdapter(this, posters, this);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mPostersRecyclerView.setLayoutManager(layoutManager);
        mPostersRecyclerView.setHasFixedSize(true);
        mPostersRecyclerView.setAdapter(mPostersAdapter);

        moviesAsyncTask = new MoviesAsyncTask();
        moviesAsyncTask.execute();

    }

    @Override
    public void onItemClick(int itemIndex) {
        Intent i = new Intent(this, DetailsActivity.class);
        DetailsActivity.getItems(originalTitle[itemIndex], voteAverage[itemIndex]
                , posters[itemIndex], releaseDate[itemIndex], overView[itemIndex]);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.top_rated_movies:
                REQUESTED_URL =
                        "http://api.themoviedb.org/3/movie/top_rated?api_key=";
                mPostersAdapter = new PostersAdapter(this, posters, this);
                mPostersRecyclerView.setAdapter(mPostersAdapter);
                moviesAsyncTask = new MoviesAsyncTask();
                moviesAsyncTask.execute();
                return true;

            case R.id.popular_movies:
                REQUESTED_URL =
                        "http://api.themoviedb.org/3/movie/popular?api_key=";
                mPostersAdapter = new PostersAdapter(this, posters, this);
                mPostersRecyclerView.setAdapter(mPostersAdapter);
                moviesAsyncTask = new MoviesAsyncTask();
                moviesAsyncTask.execute();
                return true;
            default:
                mToast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
                mToast.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private class MoviesAsyncTask extends AsyncTask<URL, Void, Movie> {

        @Override
        protected Movie doInBackground(URL... urls) {
            URL url = createUrl(REQUESTED_URL);
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.v(MainActivity.class.getSimpleName(), "doInBackground error");
            }
            Movie movie = extractJSON(jsonResponse);
            return movie;
        }

        @Override
        protected void onPostExecute(Movie movie) {
            if (movie == null) {
                return;
            }
        }

        private URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(MainActivity.class.getSimpleName(), "Error with creating URL", exception);
                return null;
            }
            return url;
        }


        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";
            if (url == null)
                return jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else
                    Log.v(MainActivity.class.getSimpleName(), "else errorr");
            } catch (IOException e) {
                Log.v(MainActivity.class.getSimpleName(), "makeHttpRequest errorr");

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    output.append(line);
                    line = bufferedReader.readLine();
                }
            }
            return output.toString();
        }

        private Movie extractJSON(String jsonResponse) {
            if (TextUtils.isEmpty(jsonResponse)) {
                return null;
            }
            try {
                JSONObject root = new JSONObject(jsonResponse);
                JSONArray resultsArray = root.getJSONArray("results");

                if (resultsArray.length() > 0) {
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject x = resultsArray.getJSONObject(i);
                        posters[i] = x.getString("poster_path");
                        originalTitle[i] = x.getString("original_title");
                        overView[i] = x.getString("overview");
                        releaseDate[i] = x.getString("release_date");
                        voteAverage[i] = x.getString("vote_average");

                    }
                    return new Movie(voteAverage[0], posters[0], originalTitle[0], overView[0], releaseDate[0]);
                }
            } catch (JSONException e) {
                Log.e(MainActivity.class.getSimpleName(), "Error parsing JSON", e);
            }
            return null;
        }
    }
}
