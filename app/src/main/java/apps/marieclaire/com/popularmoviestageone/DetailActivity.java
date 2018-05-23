package apps.marieclaire.com.popularmoviestageone;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import apps.marieclaire.com.popularmoviestageone.Model.Movie;
import apps.marieclaire.com.popularmoviestageone.databinding.ActivityDetailBinding;
import com.squareup.picasso.Picasso;

import static apps.marieclaire.com.popularmoviestageone.Util.NetworkUtils.getImageUrl;

public class DetailActivity extends AppCompatActivity {

    public static final String INTENT_EXTRA_MOVIE = "INTENT_EXTRA_MOVIE";
    ActivityDetailBinding detailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        // setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        } else if (intent.hasExtra(INTENT_EXTRA_MOVIE)) {
            Movie movie = intent.getParcelableExtra(INTENT_EXTRA_MOVIE);
            populateUI(movie);
        }
    }

    private void populateUI(Movie movie) {
        String posterUrl = movie.getPosterPath();
        if (posterUrl != null && posterUrl.trim().length() > 0) {
            ImageView detailPosterView = findViewById(R.id.detail_poster);
            Picasso.with(this)
                    .load(getImageUrl(posterUrl))
                    .into(detailPosterView);
        }
        String title = movie.getOriginalTitle();
        detailBinding.detailTitle.setText(title);
        //TextView detailTitleView = findViewById(R.id.detail_title);
        //detailTitleView.setText(movie.getOriginalTitle());
        String synopsisView=movie.getOverview();
        // TextView synopsisView = findViewById(R.id.detail_synopsis);
        detailBinding.detailSynopsis.setText(synopsisView);
        //synopsisView.setText(movie.getOverview());
        // TextView ratingView = findViewById(R.id.detail_rating);
        double ratingView=movie.getVoteAverage();
        detailBinding.detailRating.setText(String.valueOf(ratingView) );
        //ratingView.setText(String.format(getString(R.string.rating_in_10), String.valueOf(movie.getVoteAverage())));
        String releaseDateView=movie.getReleaseDate();
        detailBinding.detailReleaseDate.setText(releaseDateView);
        //TextView releaseDateView = findViewById(R.id.detail_release_date);
        // releaseDateView.setText(movie.getReleaseDate());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}