package ru.com.testunsplashclient.mvp.main.photo_details;

import com.arellomobile.mvp.MvpView;

import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.BaseView;

public interface PhotoDetailsFragmentView extends MvpView, BaseView {

    void showPhoto(Photo photo);

    void showServerError(Throwable throwable);

    void showDataBaseError(Throwable throwable);

}
