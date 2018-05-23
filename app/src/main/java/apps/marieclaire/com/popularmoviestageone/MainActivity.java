package apps.marieclaire.com.popularmoviestageone;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import java.net.URL;
import apps.marieclaire.com.popularmoviestageone.Model.Movie;
import apps.marieclaire.com.popularmoviestageone.Model.MovieAdapter;
import apps.marieclaire.com.popularmoviestageone.Util.JsonUtils;
import apps.marieclaire.com.popularmoviestageone.Util.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.List;

import static apps.marieclaire.com.popularmoviestageone.DetailActivity.INTENT_EXTRA_MOVIE;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>, MovieAdapter.ItemClickListener {

    private MovieAdapter mMovieAdapter;

    private static final int MOVIE_LOADER_ID = 0;
    private RecyclerView mRecyclerView;
    private View mErrorView;
    private View mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.movies_recycler_view);
        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mMovieAdapter);

        View view = this.findViewById(android.R.id.content);
        view.post(() -> setColumns(view, view.getContext()));
        mErrorView = findViewById(R.id.error_message_view);
        mLoadingView = findViewById(R.id.loading_view);
        Bundle bundle=new Bundle();
        bundle.putString(getString(R.string.pref_order_by_key),getString(R.string.pref_order_by_popular_value));
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, bundle, this);
    }

    private void showMovies() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id==R.id.action_popular)
        {
            Bundle bundle=new Bundle();
            bundle.putString(getString(R.string.pref_order_by_key),getString(R.string.pref_order_by_popular_value));
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,bundle, MainActivity.this);
        }
        else if(id==R.id.action_top_rated)
        {
            Bundle bundle=new Bundle();
            bundle.putString(getString(R.string.pref_order_by_key),getString(R.string.pref_order_by_highest_rated_value));
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,bundle, MainActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }


    private void setColumns(View view, Context context) {
        int columnCount = getViewColumnCount(view,R.dimen.movie_width);
        columnCount = Math.max(2, columnCount);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @NonNull
    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            private String mData;

            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    // Use cached data
                    deliverResult(mData);
                } else {
                    showLoading();
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                String orderBy=args.getString(getString(R.string.pref_order_by_key),getString(R.string.pref_order_by_popular_value));
                URL moviesRequestUrl = NetworkUtils.buildUrl(MainActivity.this, orderBy);
                try {
                    return NetworkUtils.getJsonResponseFromUrl(moviesRequestUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (data != null) {
            List<Movie> results = null;
            try {
                results = JsonUtils.parseMovieResultsJson(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            showMovies();
            mMovieAdapter.setMovieList(results);
        } else {
            showError();
            mMovieAdapter.setMovieList(null);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(INTENT_EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    private int getViewColumnCount(View view, int preferredWidthResource) {
        int containerWidth = view.getWidth();
        int preferredWidth = view.getContext().getResources().
                getDimensionPixelSize(preferredWidthResource);
        return containerWidth / preferredWidth;
    }
}