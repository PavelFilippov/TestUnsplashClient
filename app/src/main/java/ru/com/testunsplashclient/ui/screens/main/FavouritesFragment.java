package ru.com.testunsplashclient.ui.screens.main;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.events.UpdateFavourites;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.main.favourites.FavouritesFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.favourites.FavouritesFragmentView;
import ru.com.testunsplashclient.ui.BaseMainFragment;
import ru.com.testunsplashclient.ui.adapters.PhotosAdapter;
import ru.com.testunsplashclient.ui.common.BackButtonListener;
import ru.com.testunsplashclient.ui.common.IOnStarClick;
import ru.com.testunsplashclient.ui.common.RouterProvider;
import ru.terrakok.cicerone.Router;

@EFragment(R.layout.fragment_favourites_list)
public class FavouritesFragment extends BaseMainFragment implements
        FavouritesFragmentView,
        BackButtonListener,
        IOnStarClick<Photo> {

    public static final String TAG = "FavouritesFragment";

//Set screen views

    @ViewById
    View clError;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    RecyclerView rvPhotos;

//Set app navigation injections and it's management

    @Inject
    Router router;

//Set Bundles and Beans

    @InjectPresenter
    FavouritesFragmentPresenter presenter;

    @App
    TheApplication application;

//Set Local variables

    private PhotosAdapter adapter;
    private int lastVisibleElement;
    private long mLastClickTime = 0;

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

        setAdapter();
        loadFavourites();
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

    @Override
    public void showDataBaseError(Throwable throwable) {
        showViewIfEmpty();
        getMainActivity().showDbError(throwable);
    }

    @Override
    public void showFavourites(List<Photo> photos) {
        if(photos.isEmpty()) {
            showViewIfEmpty();
        } else {
            rvPhotos.setVisibility(View.VISIBLE);
            adapter.setData(photos);
        }
    }

    @Override
    public void onStarClick(Photo model, int position) {
        presenter.insertOrDeleteFavouriteInDb(model);
        EventBus.getDefault().post(new UpdateFavourites(model));
    }

    @Subscribe
    public void onFavoriteChanged(UpdateFavourites event) {
        changeItemInAdapter(event.getPhoto());
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

    private void setAdapter() {

        if (adapter != null && rvPhotos.getLayoutManager() != null) {
            rvPhotos.getLayoutManager().scrollToPosition(lastVisibleElement);
        } else {
            adapter = new PhotosAdapter(getMainActivity(), this);
            rvPhotos.setLayoutManager(new LinearLayoutManager(getMainActivity()));
            rvPhotos.setAdapter(adapter);
        }

        adapter.setRecyclerTouchListener((model, position) -> {
            if (isDoubleClick(mLastClickTime)) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            lastVisibleElement = position;
            presenter.goToFavouriteInfo(model.getId());
        });
    }

    private void loadFavourites() {
        presenter.loadFavourites();
    }

    private void showViewIfEmpty() {
        if(adapter != null && adapter.isEmpty()) {
            rvPhotos.setVisibility(View.GONE);
            clError.setVisibility(View.VISIBLE);
        }
    }

    private void showViewIfNotEmpty() {
        if(adapter != null && !adapter.isEmpty()) {
            rvPhotos.setVisibility(View.VISIBLE);
            clError.setVisibility(View.GONE);
        }
    }

    private void hideErrorView() {
        clError.setVisibility(View.GONE);
    }

    private void changeItemInAdapter(Photo photoItem) {
        if(photoItem.isFavourite()) {
            adapter.addData(photoItem);
            showViewIfNotEmpty();
        } else {
            adapter.remove(photoItem);
            showViewIfEmpty();
        }
    }

}
