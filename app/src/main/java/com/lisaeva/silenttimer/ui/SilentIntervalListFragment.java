package com.lisaeva.silenttimer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lisaeva.silenttimer.SilentIntervalListAccess;
import com.lisaeva.silenttimer.SilentIntervalList;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.SilentIntervalListListener;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;
import com.lisaeva.silenttimer.databinding.ListItemSilentIntervalBinding;

public class SilentIntervalListFragment extends Fragment {
    private static final boolean HAS_OPTIONS_MENU = true;

    private ActivityCallback mActivityCallback;
    private RecyclerView mSilentIntervalRecyclerView;
    private SilentIntervalAdapter mAdapter;
    private SilentIntervalList mSilentIntervalList;

    private static final int ID_FRAGMENT_LAYOUT = R.layout.fragment_silent_interval_list;
    private static final int ID_RECYCLER_VIEW = R.id.silent_interval_recycler_view;
    private static final int ID_LIST_ITEM = R.layout.list_item_silent_interval;
    private static final int ID_MENU = R.menu.fragment_silent_interval_list;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        mSilentIntervalList = new SilentIntervalListAccess().getInstance(activity.getApplicationContext());

        if (activity instanceof ActivityCallback) {
            mActivityCallback = (ActivityCallback) activity;
        } else {
            Toast.makeText(this.getContext(), R.string.error_main_activity_missing, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(ID_FRAGMENT_LAYOUT,container, false);
        mSilentIntervalRecyclerView = view.findViewById(ID_RECYCLER_VIEW);
        mSilentIntervalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(HAS_OPTIONS_MENU);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Menu ----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        inflater.inflate(ID_MENU, menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (mActivityCallback.checkPermissions()) {
            switch(item.getItemId()) {
                case R.id.new_silent_interval:
                    openAlarmFragment(null);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            mActivityCallback.requestPermissions();
        }
        return true;
    }

    // Utilities -----------------------------------------------------------------------------------

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new SilentIntervalAdapter();
        }
        mSilentIntervalRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    // Main Activity Callback ----------------------------------------------------------------------

    private void openAlarmFragment(SilentInterval interval) {
        if (mActivityCallback != null) {
            mSilentIntervalList.clearTempSilentInterval();
            if (interval != null) {
                mSilentIntervalList.setTempSilentInterval(interval);
            }
            mActivityCallback.openSilentIntervalFragment();
        } else {
            Toast.makeText(this.getContext(), R.string.error_main_activity_missing, Toast.LENGTH_LONG).show();
        }
    }

    // ViewHolder class ----------------------------------------------------------------------------

    private class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SilentInterval interval;
        private ListItemSilentIntervalBinding binding;
        private Switch activeSwitch;

        public AlarmHolder(ListItemSilentIntervalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
            activeSwitch = itemView.findViewById(R.id.active_switch);
        }

        public void bind(SilentInterval interval) {
            this.interval = interval;
            SilentIntervalViewModel viewModel = new SilentIntervalViewModel(interval);
            binding.setViewModel(viewModel);
            activeSwitch.setOnCheckedChangeListener((view, isChecked) -> {
                if (viewModel.getActive() != isChecked) {
                    if (isChecked) {
                        mSilentIntervalList.activate(interval);
                    } else {
                        mSilentIntervalList.terminate(interval);
                    }
                    viewModel.notifyPropertyChanged(BR.active);
                }
            });
        }

        @Override
        public void onClick(View view) {
            openAlarmFragment(interval);
        }
    }

    // Adapter class -------------------------------------------------------------------------------

    private class SilentIntervalAdapter extends RecyclerView.Adapter<AlarmHolder> implements SilentIntervalListListener {

        public SilentIntervalAdapter() {
            mSilentIntervalList.registerListListener(this);
        }

        @NonNull
        @Override
        public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ListItemSilentIntervalBinding binding = DataBindingUtil.inflate(layoutInflater, ID_LIST_ITEM, parent,false);
            return new AlarmHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
            if (getItemCount() > position) {
                SilentInterval alarm = mSilentIntervalList.get(position);
                holder.bind(alarm);
            }
        }

        @Override
        public int getItemCount() {
            return mSilentIntervalList.size();
        }

        @Override
        public void onListChanged() {
            this.notifyDataSetChanged();
        }
    }
}