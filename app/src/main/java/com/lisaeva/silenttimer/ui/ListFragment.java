package com.lisaeva.silenttimer.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lisaeva.silenttimer.databinding.ListItemBinding;
import com.lisaeva.silenttimer.localdata.SilentIntervalListAccess;
import com.lisaeva.silenttimer.localdata.SilentIntervalList;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;

/**
 * Controller for the Main screen, which displays the list of silent intervals.
 */
public class ListFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment_list;
    private static final int LAYOUT_MENU = R.menu.fragment_list;
    private static final int LAYOUT_LIST = R.id.recycler_view;
    private static final int LAYOUT_EMPTY = R.id.empty_view;
    private static final boolean HAS_OPTIONS_MENU = true;

    private MainActivityCallback mMainActivityCallback;
    private SilentIntervalList mSilentIntervalList;
    private SharedPreferenceUtil mSharedPreferenceUtil;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private TextView mEmptyView;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityCallback = (MainActivityCallback) getActivity();
        mSharedPreferenceUtil = new SharedPreferenceUtil(mMainActivityCallback.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        mEmptyView = view.findViewById(LAYOUT_EMPTY);
        mRecyclerView = view.findViewById(LAYOUT_LIST);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(HAS_OPTIONS_MENU);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSilentIntervalList != null)
            mSilentIntervalList.removeListListener(mAdapter);
    }

    // Menu ----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(LAYOUT_MENU, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (mMainActivityCallback.checkPermissions()) {
            switch(item.getItemId()) {
                case R.id.new_silent_interval:
                    openAlarmFragment(null);
                    break;
                case R.id.new_immediate_silent_interval:
                    mMainActivityCallback.openImmediateFragment();
                    break;
                case R.id.edit_list:
                    mMainActivityCallback.openEditListFragment();
                    break;
                case R.id.settings:
                    mMainActivityCallback.openSettings();
                    break;
                default: break;
            }
        } else {
            mMainActivityCallback.requestPermissions();
        }
        return true;
    }

    // Utilities -----------------------------------------------------------------------------------

    private void updateUI() {
        mSilentIntervalList = SilentIntervalListAccess.getInstance(mMainActivityCallback.getContext());
        mSilentIntervalList.loadList();
        if (mAdapter == null) {
            mAdapter = new Adapter();
        }
        mSilentIntervalList.registerListListener(mAdapter);
        mRecyclerView.setAdapter(mAdapter);
        showEmptyView(mSilentIntervalList.isEmpty());
    }

    private void showEmptyView(boolean b) {
        if (b) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            Log.d("showEmptyView()", "empty");
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            Log.d("showEmptyView()", "nonempty");
        }
    }

    // Main Activity Callback ----------------------------------------------------------------------

    private void openAlarmFragment(SilentInterval interval) {
        mSilentIntervalList.clearTempSilentInterval();
        if (interval != null)mSilentIntervalList.setTempSilentInterval(interval);
        mMainActivityCallback.openScheduleFragment();
    }

    // ViewHolder class ----------------------------------------------------------------------------

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
        private SilentInterval mInterval;
        private ListItemBinding mBinding;
        private SilentIntervalViewModel mViewModel;
        private SwitchCompat mActiveSwitch;

        public ViewHolder(ListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            itemView.setOnClickListener(this);
            mActiveSwitch = itemView.findViewById(R.id.active_switch);
            mSharedPreferenceUtil.registerActivePendingIntentListener(this);
        }

        public void bind(SilentInterval interval) {
            this.mInterval = interval;
            mViewModel = new SilentIntervalViewModel(interval);
            if (mSharedPreferenceUtil.isActivePendingIntent(interval.getUuid())) mViewModel.setActive(true);
            mBinding.setViewModel(mViewModel);
            mActiveSwitch.setOnCheckedChangeListener((view, isChecked) -> {
                if (mActiveSwitch.equals(view)) {
                    if (mViewModel.getActive() != isChecked) {
                        if (isChecked) {
                            mSilentIntervalList.activate(interval);
                            mViewModel.setActive(true);
                        } else {
                            mSilentIntervalList.deactivate(interval);
                            mViewModel.setActive(false);
                        }
                    }
                }
            });
        }

        @Override public void onClick(View view) { openAlarmFragment(mInterval); }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            boolean active = mSharedPreferenceUtil.isActivePendingIntent(key);
            if (mViewModel.getUUID().equals(key) &&  mViewModel.getActive() != active) {
                mViewModel.setActive(active);
            }
        }
    }

    // Adapter class -------------------------------------------------------------------------------

    private class Adapter extends RecyclerView.Adapter<ViewHolder> implements SilentIntervalList.ChangeListener {
        private static final int LAYOUT = R.layout.list_item;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, LAYOUT, parent,false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (getItemCount() > position) {
                SilentInterval interval = mSilentIntervalList.get(position);
                holder.bind(interval);
            }
        }

        @Override public int getItemCount() { return mSilentIntervalList.size(); }

        @Override
        public void onListChanged() {
            Log.d("onListChanged()", "received");
            showEmptyView(mSilentIntervalList.isEmpty());
            this.notifyDataSetChanged();
        }

        @Override
        public void onItemRemoved(int position) {
            if (mSilentIntervalList.isEmpty())showEmptyView(true);
            this.notifyItemRemoved(position);
        }

        @Override
        public void onRangeChanged(int positionStart, int itemCount) {
            if (mSilentIntervalList.isEmpty())showEmptyView(true);
            this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemInserted(int position) {
            if (mSilentIntervalList.size() == 1)showEmptyView(false);
            this.notifyItemInserted(position);
        }

        @Override public void onItemChanged(int position) {
            this.notifyItemRemoved(position);
        }
        @Override public void onItemMoved(int fromPosition, int toPosition) {
            this.notifyItemMoved(fromPosition, toPosition);
        }
    }
}