package ru.com.testunsplashclient.ui.screens.main;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.events.UpdateFavourites;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.main.photo_details.PhotoDetailsFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.photo_details.PhotoDetailsFragmentView;
import ru.com.testunsplashclient.ui.BaseMainFragment;
import ru.com.testunsplashclient.ui.common.BackButtonListener;
import ru.com.testunsplashclient.ui.common.RouterProvider;
import ru.terrakok.cicerone.Router;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

@EFragment(R.layout.fragment_photo_details)
public class PhotoDetailsFragment extends BaseMainFragment implements
        PhotoDetailsFragmentView,
        BackButtonListener {

    private static final String ID = "id";
    public static final String TAG = "PhotoDetailsFragment";

//Set screen views

    @ViewById
    View clError;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    View clInfoContainer;

    @ViewById
    AppCompatImageView imgPhoto;

    @ViewById
    TextView txtAuthorName;

    @ViewById
    TextView txtDescription;

    @ViewById
    TextView txtLikes;

    @ViewById
    AppCompatImageView imgFavourite;

//Set app navigation injections and it's management

    @Inject
    Router router;

//Set Bundles and Beans

    @InjectPresenter
    PhotoDetailsFragmentPresenter presenter;

    @App
    TheApplication application;

    @FragmentArg(ID)
    String id;

    public static Bundle getBundle(String code) {
        Bundle args = new Bundle();
        args.putString(ID, code);
        return args;
    }

//Main methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.getAppComponent().inject(this);
    }

    @AfterViews
    public void afterViews() {
        if ((getParentFragment()) != null) {
            presenter.setRouter(((RouterProvider) getParentFragment()).getRouter());
        } else {
            presenter.setRouter(router);
        }

        presenter.loadPhoto(id);
    }

    @Override
    public void showPhoto(Photo photo) {
        clInfoContainer.setVisibility(View.VISIBLE);
        setPhoto(photo);
        txtAuthorName.setText(photo.getUser().getName() != null ? photo.getUser().getName() : getString(R.string.no_name));
        txtDescription.setText(photo.getDescription() != null ? photo.getDescription() :
                photo.getAltDescription() != null ? photo.getAltDescription() : getString(R.string.no_description));
        txtLikes.setText(String.format(getString(R.string.likes_), photo.getLikes()));
        setStar(photo.isFavourite());
        setStarClickListener(photo);
    }

    @Override
    public void showServerError(Throwable throwable) {
        showViewOnError();
        getMainActivity().showServerError(throwable);
    }

    @Override
    public void showDataBaseError(Throwable throwable) {
        showViewOnError();
        getMainActivity().showDbError(throwable);
    }

    @Override
    public void showLoading() {
        hideErrorView();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Subscribe
    public void onFavoriteChanged(UpdateFavourites event) {
        setStar(event.getPhoto().isFavourite());
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }

//Internal methods

    private void showViewOnError() {
        clError.setVisibility(View.VISIBLE);
    }

    private void hideErrorView() {
        clError.setVisibility(View.GONE);
    }

    private void setPhoto(Photo photo) {
        ViewGroup.LayoutParams params = imgPhoto.getLayoutParams();
        params.height = getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT ?
                getMainActivity().getScreenHeight()/2 :
                getMainActivity().getScreenHeight();
        imgPhoto.setLayoutParams(params);

        String largeFile = "";
        String smallFile = "";

        if (photo.getUrls().getRegular() != null) {
            largeFile = photo.getUrls().getRegular();
        } else if (photo.getUrls().getSmall() != null) {
            largeFile = photo.getUrls().getSmall();
        } else if (photo.getUrls().getThumb() != null) {
            largeFile = photo.getUrls().getThumb();
        }

        smallFile = photo.getUrls().getThumb();

        Glide.with(getMainActivity())
                .asBitmap()
                .load(largeFile)
                .thumbnail(Glide.with(getMainActivity())
                        .asBitmap()
                        .load(smallFile)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transform(new CenterCrop(), new RoundedCorners(getMainActivity().getResources().getDimensionPixelSize(R.dimen.corner_radius)))
                        .placeholder(R.drawable.rounded_corners_empty_image)
                        .error(R.drawable.rounded_corners_empty_image))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new CenterCrop(), new RoundedCorners(getMainActivity().getResources().getDimensionPixelSize(R.dimen.corner_radius)))
                .placeholder(R.drawable.rounded_corners_empty_image)
                .error(R.drawable.rounded_corners_empty_image)
                .into(imgPhoto);
    }

    private void setStar(boolean isInFavourites) {
        imgFavourite.setImageDrawable(isInFavourites ?
                getResources().getDrawable(R.drawable.favourites_in) :
                getResources().getDrawable(R.drawable.favourites_out));
    }

    private void setStarClickListener(Photo photo) {
        imgFavourite.setOnClickListener(v -> {
            photo.setFavourite(!photo.isFavourite());
            presenter.insertOrDeleteFavouriteInDb(photo);
            EventBus.getDefault().post(new UpdateFavourites(photo));
            setStar(photo.isFavourite());
        });
    }
}
