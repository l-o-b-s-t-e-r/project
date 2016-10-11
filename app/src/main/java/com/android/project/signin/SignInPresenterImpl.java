package com.android.project.signin;

import com.android.project.cofig.DatabaseManager;
import com.android.project.cofig.WhichOneApp;
import com.android.project.util.RequestService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 05.09.16.
 */
public class SignInPresenterImpl implements SignInPresenter.ActionListener {

    private static final String TAG = SignInPresenterImpl.class.getName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private SignInPresenter.View mSignInView;

    public SignInPresenterImpl(SignInPresenter.View signInView) {
        mSignInView = signInView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void signIn(String name, String password) {
        Subscription subscription =
                requestService
                        .signIn(name, password)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignInView.openUserPage(false);
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                mSignInView.openUserPage(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void remindInfo(String email) {
        Subscription subscription =
                requestService
                        .remindInfo(email)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                mSignInView.remindInfoResult(false);
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                mSignInView.remindInfoResult(true);
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
