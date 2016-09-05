package com.android.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.model.Image;
import com.android.project.util.RecordServiceImpl;
import com.android.project.wall.WallPresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 21.05.16.
 */

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getName();
    private Context mContext;
    private List<Image> mImages;
    private WallPresenter.ActionListener mActionListener;

    public RecordRecyclerViewAdapter(Context context, List<Image> images, WallPresenter.ActionListener actionListener) {
        mImages = images;
        mContext = context;
        mActionListener = actionListener;
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
            mActionListener.openRecordDetail(mImages.get(0).getRecordId());
        }

        public void setContent(final Image image) {
            Picasso.with(mContext)
                    .load(RecordServiceImpl.BASE_URL + image.getImage())
                    .error(R.drawable.ic_error)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            spinner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }
}
