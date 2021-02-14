package com.lisaeva.silenttimer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lisaeva.silenttimer.ActivityCallback;
import com.lisaeva.silenttimer.AlarmList;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.databinding.ListItemAlarmBinding;
import com.lisaeva.silenttimer.model.SilentAlarm;
import com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel;


public class AlarmListFragment extends Fragment {
    private static final boolean HAS_OPTIONS_MENU = true;

    private ActivityCallback activityCallback;
    private RecyclerView alarmRecyclerView;
    private AlarmAdapter adapter;
    private AlarmList alarmList;

    private static final int ID_FRAGMENT_LAYOUT = R.layout.fragment_alarm_list;
    private static final int ID_RECYCLER_VIEW = R.id.alarm_recycler_view;
    private static final int ID_LIST_ITEM = R.layout.list_item_alarm;
    private static final int ID_MENU = R.menu.fragment_alarm_list;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        alarmList = AlarmList.get(activity.getApplicationContext());

        if (activity instanceof ActivityCallback) {
            activityCallback = (ActivityCallback) activity;
        } else {
            Toast.makeText(this.getContext(), R.string.error_main_activity_missing, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(ID_FRAGMENT_LAYOUT,container, false);
        alarmRecyclerView = view.findViewById(ID_RECYCLER_VIEW);
        alarmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        switch(item.getItemId()) {
            case R.id.new_silent_alarm:
                openAlarmFragment(null);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Utilities -----------------------------------------------------------------------------------

    private void updateUI() {
        if (adapter == null) {
            adapter = new AlarmAdapter();
        }
        alarmRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void openAlarmFragment(SilentAlarm alarm) {
        if (activityCallback != null) {
            Bundle bundle = new Bundle();
            if (alarm != null) {
                bundle.putInt(AlarmFragment.SILENT_ALARM_INDEX_TAG, alarmList.getPosition(alarm));
                alarmList.setTempAlarm(alarm);
            } else {
                bundle.putInt(AlarmFragment.SILENT_ALARM_INDEX_TAG, -1);
                alarmList.clearTempAlarm();
            }
            activityCallback.callback(ActivityCallback.CallbackReason.OPEN_ALARM_FRAGMENT, bundle);
        } else {
            Toast.makeText(this.getContext(), R.string.error_main_activity_missing, Toast.LENGTH_LONG).show();
        }
    }

    // ViewHolder class ----------------------------------------------------------------------------

    private class AlarmHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SilentAlarm alarm;
        private ListItemAlarmBinding binding;

        public AlarmHolder(ListItemAlarmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(SilentAlarm alarm) {
            this.alarm = alarm;
            binding.setViewModel(new SilentAlarmViewModel(alarm));
        }

        @Override
        public void onClick(View view) {
            openAlarmFragment(alarm);
        }
    }

    // Adapter class -------------------------------------------------------------------------------

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {

        public AlarmAdapter() {}

        @NonNull
        @Override
        public AlarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ListItemAlarmBinding binding = DataBindingUtil.inflate(layoutInflater, ID_LIST_ITEM, parent,false);
            return new AlarmHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull AlarmHolder holder, int position) {
            if (getItemCount() > position) {
                SilentAlarm alarm = alarmList.get(position);
                holder.bind(alarm);
            }
        }

        @Override
        public int getItemCount() {
            return alarmList.size();
        }
    }
}