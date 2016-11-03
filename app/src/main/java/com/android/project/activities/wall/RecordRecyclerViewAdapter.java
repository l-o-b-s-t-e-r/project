package com.android.project.activities.wall;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.WhichOneApp;
import com.android.project.model.Image;
import com.android.project.util.ImageManager;
import com.squareup.picasso.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 21.05.16.
 */

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getSimpleName();
    private Long mRecordId;
    private List<Image> mImages;
    private WallPresenter.ActionListener mActionListener;

    public RecordRecyclerViewAdapter(WallPresenter.ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void setContent(Long recordId, List<Image> images) {
        mRecordId = recordId;
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_recycler_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.record_image)
        ImageView imageView;
        @BindView(R.id.progressBar)
        ProgressBar spinner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.record_image)
        public void onImageClick() {
            mActionListener.openRecordDetail(mRecordId);
        }

        public void setContent(final Image image) {
            spinner.setVisibility(View.VISIBLE);

            WhichOneApp.getPicasso()
                    .load(ImageManager.IMAGE_URL + image.getImage())
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "ERROR");
                        }
                    });
        }
    }
}
