package com.android.project.activities.detail;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Option;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Lobster on 22.06.16.
 */

public class RecordDetailPresenterImpl implements RecordDetailPresenter.ActionListener {

    private static final String TAG = RecordDetailPresenterImpl.class.getSimpleName();
    @Inject
    public RequestService requestService;
    @Inject
    public DatabaseManager databaseManager;
    @Inject
    public CompositeSubscription compositeSubscription;

    private RecordDetailPresenter.View mDetailView;

    public RecordDetailPresenterImpl(RecordDetailPresenter.View detailView) {
        mDetailView = detailView;
        WhichOneApp.getMainComponent().inject(this);
    }

    @Override
    public void loadRecord(Long recordId) {
        Log.i(TAG, "loadRecord: recordId - " + recordId);
        mDetailView.showRecord(databaseManager.getById(recordId));
    }

    @Override
    public void sendVote(final Long recordId, final Option option, final String userName, final List<Subscriber<Void>> subscribers) {
        Log.i(TAG, String.format("sendVote: recordId - %d, option - %s, username - %s", recordId, option.getOptionName(), userName));

        Subscription subscription =
                requestService
                        .sendVote(recordId, option.getOptionName(), userName)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "sendVote: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Void aVoid) {
                                Log.i(TAG, "sendVote: SUCCESS");
                                databaseManager.addVote(recordId, option, userName);

                                for (Subscriber<Void> subscriber : subscribers) {
                                    subscriber.onNext(null);
                                }
                            }
                        });

        compositeSubscription.add(subscription);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}