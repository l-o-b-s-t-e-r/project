package com.android.project.activities.homewall;


import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.activities.detail.RecordDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeWallFragment extends Fragment implements HomeWallPresenter.View {

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.home_wall_recycler)
    RecyclerView recyclerView;

    private String mUsername;
    private HomeWallPresenter.ActionListener mActionListener;
    private HomeWallRecyclerViewAdapter mRecyclerViewAdapter;

    private boolean refreshing = true;

    public HomeWallFragment() {

    }

    public static Fragment newInstance(TabLayout tabLayout) {
        HomeWallFragment fragment = new HomeWallFragment();
        tabLayout.addTab(tabLayout.newTab());

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActionListener = new HomeWallPresenterImpl(this);
        mRecyclerViewAdapter = new HomeWallRecyclerViewAdapter(getContext(), mActionListener);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_wall, container, false);
        ButterKnife.bind(this, view);
        showSwipeLayoutProgress();
        swipeLayout.setOnRefreshListener(getRefreshListener());

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            mUsername = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.user_name), "");
        } else {
            mUsername = bundle.getString(getString(R.string.user_name_opened_page));
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mActionListener.loadLastRecords(mUsername);

        return view;
    }

    @Override
    public void updateRecords(List<Long> recordIds) {
        mRecyclerViewAdapter.updateData(recordIds);
    }

    @Override
    public void openRecordDetail(Long recordId) {
        Intent intent = new Intent(getContext(), RecordDetailActivity.class);
        intent.putExtra(RecordDetailActivity.RECORD_ID, recordId);
        startActivity(intent);
    }

    @Override
    public void clearHomeWall() {
        mRecyclerViewAdapter.cleanData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mActionListener.onStop();
    }

    @Override
    public void showProgress() {
        if (!refreshing) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (refreshing) {
            refreshing = false;
        }

        swipeLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

    public void showSwipeLayoutProgress() {
        swipeLayout.setRefreshing(true);
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshing = true;
                clearHomeWall();
                mActionListener.loadLastRecords(mUsername);
            }
        };
    }
}