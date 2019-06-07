package ru.com.testunsplashclient.ui.screens.main;

import android.os.Bundle;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.app.TheApplication;
import ru.com.testunsplashclient.mvp.main.info.InfoFragmentPresenter;
import ru.com.testunsplashclient.mvp.main.info.InfoFragmentView;
import ru.com.testunsplashclient.ui.BaseMainFragment;
import ru.com.testunsplashclient.ui.common.BackButtonListener;
import ru.com.testunsplashclient.ui.common.RouterProvider;
import ru.terrakok.cicerone.Router;

@EFragment(R.layout.fragment_info)
public class InfoFragment extends BaseMainFragment implements
        InfoFragmentView,
        BackButtonListener {

    public static final String TAG = "InfoFragment";

//Set screen views

    @ViewById
    TextView txtAuthorName;

    @ViewById
    TextView txtAuthorPhone;

    @ViewById
    TextView txtAuthorEmail;

    @ViewById
    TextView txtLibraries;

//Set app navigation injections and it's management

    @Inject
    Router router;

//Set Bundles and Beans

    @InjectPresenter
    InfoFragmentPresenter presenter;

    @App
    TheApplication application;

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

        txtAuthorName.setText(getString(R.string.author_name));
        txtAuthorPhone.setText(String.format(getString(R.string.phone_), getString(R.string.author_phone)));
        txtAuthorEmail.setText(String.format(getString(R.string.email_), getString(R.string.author_email)));
        txtLibraries.setText(String.format(getString(R.string.stack_), getString(R.string.stack)));

    }

    @Override
    public boolean onBackPressed() {
        presenter.onBackPressed();
        return true;
    }
}
