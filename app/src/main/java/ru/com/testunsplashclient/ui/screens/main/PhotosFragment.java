package ru.com.testunsplashclient.ui.screens.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.events.UpdateFavourites;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.core.utils.Constants;
import ru.com.testunsplashclient.mvp.main.photos.PhotosFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.photos.PhotosFragmentView;
import ru.com.testunsplashclient.ui.BaseMainFragment;
import ru.com.testunsplashclient.ui.adapters.PhotosAdapter;
import ru.com.testunsplashclient.ui.common.BackButtonListener;
import ru.com.testunsplashclient.ui.common.IOnStarClick;
import ru.com.testunsplashclient.ui.common.RouterProvider;
import ru.terrakok.cicerone.Router;

@EFragment(R.layout.fragment_photo_list)
public class PhotosFragment extends BaseMainFragment implements
        PhotosFragmentView,
        BackButtonListener,
        IOnStarClick<Photo> {

    public static final String TAG = "PhotosFragment";

//Set screen views

    @ViewById
    SwipeRefreshLayout srlRefresh;

    @ViewById
    View clError;

    @ViewById
    ProgressBar progressBar;

    @ViewById
    SmoothProgressBar progressBottom;

    @ViewById
    RecyclerView rvPhotos;

//Set app navigation injections and it's management

    @Inject
    Router router;

//Set Bundles and Beans

    @InjectPresenter
    PhotosFragmentPresenter presenter;

    @App
    TheApplication application;

//Set Local variables

    private boolean isLoadingData;

    private PhotosAdapter adapter;
    private int lastVisibleElement;
    private long mLastClickTime = 0;

    private int currentPage;

//Main methods

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
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
        setRefresh();

        currentPage = presenter.getCurrentPage();

        if(currentPage == -2) loadPhotos(true, false);
    }

    @Override
    public void showLoading() {
        isLoadingData = true;
        hideErrorView();
        if (adapter == null || adapter.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        isLoadingData = false;
        progressBar.setVisibility(View.GONE);
        progressBottom.setVisibility(View.GONE);
    }

    @Override
    public void showServerError(Throwable throwable) {
        showErrorViewIfEmpty();
        getMainActivity().showServerError(throwable);
    }

    @Override
    public void showDataBaseError(Throwable throwable) {
        showErrorViewIfEmpty();
        getMainActivity().showDbError(throwable);
    }

    @Override
    public void showPhotosFromDb(List<Photo> photos) {
        if (photos.isEmpty() && adapter.getItemCount() == 0) {
            showErrorViewIfEmpty();
        } else {
            rvPhotos.setVisibility(View.VISIBLE);
            adapter.addData(photos);
        }
    }

    @Override
    public void showPhotosFormNet(int page, List<Photo> photos) {
        if(currentPage == page) {
            if (currentPage < Constants.PAGE_COUNT)
                setScrollListener();
            currentPage = -2;
        }
        if (photos.isEmpty() && adapter.getItemCount() > 0) {
            removeScrollListener();
        } else if (page == Constants.PAGE_COUNT) {
            removeScrollListener();
            adapter.addData(checkForRepeats(photos));
        } else {
            if (photos.isEmpty() && adapter.getItemCount() == 0) {
                removeScrollListener();
                showErrorViewIfEmpty();
            } else {
                rvPhotos.setVisibility(View.VISIBLE);
                adapter.addData(checkForRepeats(photos));
            }
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
            presenter.goToPhotoInfo(model.getId());
        });

    }

    private void setRefresh() {
        srlRefresh.setColorSchemeResources(R.color.colorPrimary);

        srlRefresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (srlRefresh != null) {
                        srlRefresh.setRefreshing(false);
                        adapter.clear();
                        presenter.refreshDb();
                        setScrollListener();
                        loadPhotos(false, true);
                    }
                }
            }, 500);
        });
    }

    private void setScrollListener() {
        rvPhotos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoadingData) {
                    loadPhotos(false, false);
                }
            }
        });
    }

    private void removeScrollListener() {
        rvPhotos.clearOnScrollListeners();
    }

    private void loadPhotos(boolean loadFromDb, boolean refreshData) {
        presenter.loadPhotos(loadFromDb, refreshData);
    }

    private void showErrorViewIfEmpty() {
        if (adapter != null && adapter.isEmpty()) {
            rvPhotos.setVisibility(View.GONE);
            clError.setVisibility(View.VISIBLE);
        }
    }

    private void hideErrorView() {
        clError.setVisibility(View.GONE);
    }

    private void changeItemInAdapter(Photo photoItem) {
        for(Photo photo: adapter.getData()) {
            if(photo.getId().equals(photoItem.getId())) {
                photo.setFavourite(photoItem.isFavourite());
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private List<Photo> checkForRepeats(List<Photo> photos) {
        List<Photo> selectedPhotos = new ArrayList<>(photos);
        List<Photo> photosInAdapter = adapter.getData();
        for (Photo photo : photosInAdapter) {
            for (Photo photo1 : photos) {
                if (photo.getId().equals(photo1.getId())) {
                    selectedPhotos.remove(photo1);
                }
            }
        }
        return selectedPhotos;
    }

}
