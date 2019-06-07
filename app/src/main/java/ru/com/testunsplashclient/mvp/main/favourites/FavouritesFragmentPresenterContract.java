package ru.com.testunsplashclient.mvp.main.favourites;

import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.BasePresenter;

public interface FavouritesFragmentPresenterContract extends BasePresenter {

    void loadFavourites();

    void refreshDb();

    void insertOrDeleteFavouriteInDb(Photo photo);

    void goToFavouriteInfo(String id);

}
