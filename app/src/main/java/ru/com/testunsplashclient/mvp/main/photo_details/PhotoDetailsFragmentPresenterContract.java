package ru.com.testunsplashclient.mvp.main.photo_details;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.BasePresenter;

public interface PhotoDetailsFragmentPresenterContract extends BasePresenter {

    void loadPhoto(String id);

    void insertOrDeleteFavouriteInDb(Photo photo);

}
