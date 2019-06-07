package ru.com.testunsplashclient.core.data;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ru.com.testunsplashclient.app.Api;
import ru.com.testunsplashclient.app.NoNetworkException;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.core.utils.Constants;
import ru.com.testunsplashclient.core.utils.NetworkUtils;
import ru.com.testunsplashclient.core.utils.RxUtils;

public class DataProvider {

    private Api api;

    public DataProvider(Api api) {
        this.api = api;
        TheApplication.INSTANCE.getAppComponent().inject(this);

    }

    @Inject
    NetworkUtils networkUtils;

    @Inject
    PhotosDb photosDb;

//HTTP methods

    public Disposable getPhotos(int page, Consumer<List<Photo>> onComplete, Consumer<Throwable> onError) {
        if (!hasNetwork()) return createNoNetworkSubscription(onComplete, onError);
        return api.getPhotos(Constants.ACSESS_KEY, page)
                .compose(RxUtils.applySchedulers())
                .subscribe(onComplete, onError);
    }

    public Disposable getPhoto(String id, Consumer<Photo> onComplete, Consumer<Throwable> onError) {
        if (!hasNetwork()) return createNoNetworkSubscription(onComplete, onError);
        return api.getPhotoById(id, Constants.ACSESS_KEY)
                .compose(RxUtils.applySchedulers())
                .subscribe(onComplete, onError);
    }

//DB methods

    public Disposable getPhotosFromDb(Consumer<List<Photo>> onComplete, Consumer<Throwable> onError) {
        return photosDb.getPhotos()
                .compose(RxUtils.applySchedulerSingle())
                .subscribe(onComplete, onError);
    }

    public Disposable getFavouritesFromDb(Consumer<List<Photo>> onComplete, Consumer<Throwable> onError) {
        return photosDb.getFavourites()
                .compose(RxUtils.applySchedulerSingle())
                .subscribe(onComplete, onError);
    }

    public Disposable getPhotoFromDb(String id, Consumer<Photo> onComplete, Consumer<Throwable> onError) {
        return photosDb.getPhoto(id)
                .compose(RxUtils.applySchedulerSingle())
                .subscribe(onComplete, onError);
    }

    public Disposable getFavouritePhotoFromDb(String id, Consumer<Photo> onComplete, Consumer<Throwable> onError) {
        return photosDb.getFavouritePhoto(id)
                .compose(RxUtils.applySchedulerSingle())
                .subscribe(onComplete, onError);
    }

    public Disposable updatePhotosInDb(List<Photo> photos, Consumer<Throwable> onError) {
        return photosDb.updatePhotos(photos)
                .doOnError(onError)
                .subscribe();
    }

    public Disposable updateFavouritesInDb(List<Photo> photos, Consumer<Throwable> onError) {
        return photosDb.updateFavourites(photos)
                .doOnError(onError)
                .subscribe();
    }

    public Disposable updatePhotoInDb(Photo photo, Consumer<Throwable> onError) {
        return photosDb.updatePhoto(photo)
                .doOnError(onError)
                .subscribe();
    }

    public Disposable insertOrDeleteFavouriteInDb(Photo photo, Consumer<Throwable> onError) {
        return photosDb.insertOrDeleteFavourite(photo)
                .doOnError(onError)
                .subscribe();
    }

    public Disposable clearPhotosInDb(Consumer<Throwable> onError) {
        return photosDb.clearAllPhotos()
                .doOnError(onError)
                .subscribe();
    }

    public Disposable clearFavouritesInDb(Consumer<Throwable> onError) {
        return photosDb.clearAllFavourites()
                .doOnError(onError)
                .subscribe();
    }

    public boolean isEmptyPhotos() {
        return photosDb.isEmptyPhotos();
    }

    public boolean isEmptyFavourites() {
        return isEmptyFavourites();
    }

    public boolean containsFavourite(String id) {
        return photosDb.containsFavourite(id);
    }


//Network methods

    private boolean hasNetwork() {
        return true;
    }

    private <T> Disposable createNoNetworkSubscription(Consumer<T> onComplete, Consumer<Throwable> onError) {
        return RxUtils.makeObservable((Callable<T>) () -> {
            throw new NoNetworkException();
        }).subscribe(onComplete, onError);
    }
}
