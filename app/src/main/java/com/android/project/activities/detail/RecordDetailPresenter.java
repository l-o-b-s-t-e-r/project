package com.android.project.activities.detail;

import com.android.project.model.Option;
import com.android.project.model.Record;
import com.android.project.util.CommonPresenter;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Lobster on 22.06.16.
 */
public interface RecordDetailPresenter {

    interface View extends CommonPresenter.View {

        void showRecord(Record record);

    }

    interface ActionListener extends CommonPresenter.ActionListener {

        void loadRecord(Long recordId);

        void sendVote(Long recordId, Option option, String userName, List<Subscriber<Void>> subscribers);

    }

}