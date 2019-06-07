package ru.com.testunsplashclient.mvp.main.photos;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.BaseView;

public interface PhotosFragmentView extends MvpView, BaseView {

    @StateStrategyType(SkipStrategy.class)
    void showPhotosFromDb(List<Photo> photos);

    void showPhotosFormNet(int page, List<Photo> photos);

    void showServerError(Throwable throwable);

    void showDataBaseError(Throwable throwable);

}
