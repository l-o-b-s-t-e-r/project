package com.android.project.view.wall;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Image;
import com.android.project.model.Record;
import com.android.project.model.User;
import com.android.project.util.ImageManager;
import com.android.project.util.QuizViewBuilder;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Lobster on 12.05.16.
 */

public class WallRecyclerViewAdapter extends RecyclerView.Adapter<WallRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = WallRecyclerViewAdapter.class.getSimpleName();

    private boolean allRecordsLoaded;
    private List<Record> mRecords;
    private WallPresenter.ActionListener mPresenter;
    private String mUsername;
    private QuizViewBuilder mQuizViewBuilder;
    private RequestManager mGlide;

    @Inject
    public WallRecyclerViewAdapter(WallPresenter.ActionListener presenter, User user, QuizViewBuilder quizViewBuilder, RequestManager glide) {
        mPresenter = presenter;
        mUsername = user.getUsername();
        mQuizViewBuilder = quizViewBuilder;
        mGlide = glide;
        mRecords = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setContent(mRecords.get(position), position);

        if (!allRecordsLoaded && position == mRecords.size() - 2) {
            mPresenter.loadNextRecords(mRecords.get(mRecords.size() - 1).getRecordId(), mUsername);
        }
    }

    public void updateData(List<Record> records) {
        if (!records.isEmpty()) {
            mRecords.addAll(records);
            notifyItemChanged(mRecords.size() - records.size());
        } else {
            allRecordsLoaded = true;
        }
    }

    public void refreshRecords(Set<String> recordIds) {
        Log.i(TAG, "updateRecords: record IDs - " + recordIds.toString());

        Record record = new Record();
        Integer index;
        for (String recordId : recordIds) {
            record.setRecordId(Long.valueOf(recordId));
            index = mRecords.indexOf(record);
            if (index != -1) {
                mPresenter.updateRecord(record.getRecordId(), index);
            }
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder.mQuizSubscriber != null) {
            holder.mQuizSubscriber.unsubscribe();
        }
    }

    public void updateRecord(Integer index, Record record, Boolean notify) {
        mRecords.set(index, record);

        if (notify) {
            notifyItemChanged(index);
        }
    }

    public void cleanData() {
        mRecords.clear();
        allRecordsLoaded = false;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.record_recycler)
        RecyclerView recyclerView;
        @BindView(R.id.radio_group)
        RadioGroup radioGroup;
        @BindView(R.id.avatar)
        ImageView avatar;

        private Record mRecord;
        private Integer mPosition;
        private Subscriber<Record> mQuizSubscriber;
        private RecordRecyclerViewAdapter mRecordRecyclerViewAdapter;

        public ViewHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
            createIncludedRecyclerView();

            radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    radioGroup.removeAllViews();

                    mQuizSubscriber = createQuizSubscriber(
                            mQuizViewBuilder.createProgressOption(radioGroup, mRecord)
                    );

                mPresenter.sendVote(
                            mRecord,
                            mRecord.getOptions().get(checkedId),
                            mUsername,
                            mQuizSubscriber,
                            mPosition
                    );
            });
        }

        @OnClick({R.id.avatar, R.id.username})
        public void onUserClick() {
            mPresenter.openUserPage(mRecord.getUsername());
        }

        public void setContent(Record record, Integer position) {
            Log.i(TAG, "setContent: record - " + record.toString());

            mRecord = record;
            mPosition = position;
            username.setText(record.getUsername());
            description.setText(record.getDescription());

            mGlide.load(ImageManager.IMAGE_URL + record.getAvatar())
                    .asBitmap()
                    .into(ImageManager.getInstance().createTarget(
                            ImageManager.SMALL_AVATAR_SIZE, ImageManager.SMALL_AVATAR_SIZE, avatar
                    ));

            buildOptions();
            setImages(record.getRecordId(), record.getImages());
        }

        private void buildOptions() {
            radioGroup.removeAllViews();

            if (mRecord.getSelectedOption() != null) {
                mQuizViewBuilder.createVotedOptions(radioGroup, mRecord);
            } else {
                mQuizViewBuilder.createRadioOptions(radioGroup, mRecord);
            }
        }

        private void createIncludedRecyclerView() {
            recyclerView.setLayoutManager(new LinearLayoutManager(WhichOneApp.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);

            mRecordRecyclerViewAdapter = new RecordRecyclerViewAdapter(mPresenter, mGlide);
            recyclerView.setAdapter(mRecordRecyclerViewAdapter);
        }

        private void setImages(Long recordId, List<Image> images) {
            mRecordRecyclerViewAdapter.setContent(recordId, images);
        }

        private Subscriber<Record> createQuizSubscriber(final List<QuizViewBuilder.ViewHolder> viewHolderOptions) {
            return new Subscriber<Record>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(Record record) {
                    Integer allVotesCount = record.getVoteCount();
                    for (QuizViewBuilder.ViewHolder viewHolder : viewHolderOptions) {
                        viewHolder.setContent(record, allVotesCount);
                    }
                }
            };
        }
    }
}
