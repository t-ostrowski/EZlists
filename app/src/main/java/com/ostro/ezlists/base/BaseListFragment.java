package com.ostro.ezlists.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ostro.ezlists.R;
import com.ostro.ezlists.base.clickable_recycler.ClickableRecyclerAdapter;
import com.ostro.ezlists.base.clickable_recycler.OnItemClickListener;
import com.ostro.ezlists.base.clickable_recycler.SpaceItemDecoration;
import com.ostro.ezlists.rx.EndlessObserver;
import com.ostro.ezlists.util.NetworkUtils;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.List;

import butterknife.BindView;
import rx.Observable;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public abstract class BaseListFragment<O, VH extends RecyclerView.ViewHolder> extends BaseFragment {

    public static final String TAG = "BreakdownCategoriesFragment";

    private static final String LAST_POSITION = "LAST_POSITION";
    private static final String LAST_ITEM_COUNT = "LAST_ITEM_COUNT";

    @BindView(R.id.recycler)
    protected RecyclerView mRecyclerView;
    @Nullable
    @BindView(R.id.progress_bar)
    protected View mProgressBar;
    @Nullable
    @BindView(R.id.network_progress_bar)
    protected View mNetworkProgressBar;

    private ClickableRecyclerAdapter<O, VH> mAdapter;
    private int mSavedPosition;
    private int mLastItemCount;
    private boolean mItemDecorationEnabled = true;
    private LinearLayoutManager mLayoutManager;

    @Override
    public int getLayoutResources() {
        return R.layout.base_fragment_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = provideAdapter(getActivity());
        if (savedInstanceState != null) {
            mSavedPosition = savedInstanceState.getInt(LAST_POSITION);
            mLastItemCount = savedInstanceState.getInt(LAST_ITEM_COUNT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        Activity activity = getActivity();
        mLayoutManager = provideLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        itemDecoration(activity);
//        mRecyclerView.setHasFixedSize(true);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    protected void itemDecoration(Activity activity) {
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(
                getItemColumnCount(),
                activity.getResources().getDimensionPixelSize(R.dimen.global_widget_margin),
                true)
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        if(hasBeenStopped() && canReloadOnStart()) {
            // TODO: 20/11/2015 faut voir si on fait tjrs une requete coté WEB
            loadData();
        }
    }

    private void loadData() {
        subscribeAsync(getItems(),
                mObserver,
                FragmentEvent.STOP);
        progressBar(mLastItemCount == 0);
        progressBarNetwork(true);
    }

    protected boolean canReloadOnStart() {
        return true;
    }

    private EndlessObserver<List<O>> mObserver = new EndlessObserver<List<O>>() {
        @Override
        public void onNext(List<O> objects) {
            super.onNext(objects);
            success(objects);
            progressBarNetwork(false);
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            viewError(e);
            progressBar(false);
            progressBarNetwork(false);
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
            progressBar(false);
            progressBarNetwork(false);
        }
    };

    protected void success(List<O> list) {
        mAdapter.setItems(list);
        mAdapter.notifyDataSetChanged();
        if (!list.isEmpty()) {
            progressBar(false);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (mSavedPosition != 0) {
            mRecyclerView.scrollToPosition(mSavedPosition);
        }
    }

    @Nullable
    public O getItem(int position) {
        return mAdapter == null ? null : mAdapter.getItem(position);
    }

    protected void viewError(Throwable throwable) {
        int errorType = NetworkUtils.get().getErrorType(throwable);
        if (acceptOfflineMode() && errorType != NetworkUtils.ERROR_UNKNOWN) {
            // nothing, it's error from web
        } else {
            errorSnackBar(mRecyclerView, NetworkUtils.get().getErrorMessage(throwable));
        }
    }

    protected void progressBar(boolean visible) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    protected void progressBarNetwork(boolean visible) {
        if (mNetworkProgressBar != null) {
            mNetworkProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            onItemClicked(position);
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLayoutManager != null) {
            outState.putInt(LAST_POSITION, mLayoutManager.findFirstVisibleItemPosition());
        }
        if (mAdapter != null) {
            outState.putInt(LAST_ITEM_COUNT, mAdapter.getItemCount());
        }
    }

    public abstract boolean acceptOfflineMode();

    public abstract LinearLayoutManager provideLayoutManager(Activity activity);

    public abstract int getItemColumnCount();

    public abstract ClickableRecyclerAdapter<O, VH> provideAdapter(Activity activity);

    public abstract Observable<List<O>> getItems();

    public abstract void onItemClicked(int position);
}

