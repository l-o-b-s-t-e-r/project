package com.android.project.activities.detail;

import android.util.Log;

import com.android.project.WhichOneApp;
import com.android.project.api.RequestService;
import com.android.project.database.DatabaseManager;
import com.android.project.model.Option;
import com.android.project.model.Record;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
        mDetailView.showRecord(databaseManager.getRecordById(recordId));
    }

    @Override
    public void sendVote(final Record record, final Option option, final String username) {
        Log.i(TAG, String.format("sendVote: recordId - %d, option - %s, username - %s", record.getRecordId(), option.getOptionName(), username));

        Subscription subscription =
                requestService.sendVote(record.getRecordId(), option.getOptionName(), username)
                        .flatMap(new Func1<Void, Observable<Option>>() {
                            @Override
                            public Observable<Option> call(Void aVoid) {
                                return Observable.just(databaseManager.addVote(record.getRecordId(), option, username));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Option>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "sendVote: " + e.getMessage());
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Option option) {
                                Log.i(TAG, "sendVote: SUCCESS");

                                addUserVote(option, username);
                                mDetailView.updateQuiz();
                            }
                        });

        compositeSubscription.add(subscription);
    }

    private void addUserVote(Option option, String username) {
        option.getVotes().add(username);
    }

    @Override
    public void onStop() {
        compositeSubscription.clear();
    }
}
