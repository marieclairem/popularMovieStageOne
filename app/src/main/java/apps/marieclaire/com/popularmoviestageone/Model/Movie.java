package apps.marieclaire.com.popularmoviestageone.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private final int mId;
    private  String mOriginalTitle;
    private  String mPosterPath;
    private  String mOverview;
    private  double mVoteAverage;
    private  String mReleaseDate;

    public Movie(int id, String originalTitle, String posterPath, String overview,
                 double voteAverage, String releaseDate) {
        mId = id;
        mOriginalTitle = originalTitle;
        mPosterPath = posterPath;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        mId = in.readInt();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readDouble();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getOriginalTitle() {
        return mOriginalTitle;
    }
    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }
    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }
    public String getOverview() {
        return mOverview;
    }
    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }
    public void setmVoteAverage(double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }



    public String getReleaseDate() {
        return mReleaseDate;
    }
    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }
    @Override
    public String toString() {
        return "Movie{" +
                "mId=" + mId +
                ", mOriginalTitle='" + mOriginalTitle + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mVoteAverage=" + mVoteAverage +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeDouble(mVoteAverage);
        dest.writeString(mReleaseDate);
    }
}
