package com.lisaeva.silenttimer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.databinding.ListItemEditBinding;
import com.lisaeva.silenttimer.localdata.SilentIntervalList;
import com.lisaeva.silenttimer.localdata.SilentIntervalListAccess;
import com.lisaeva.silenttimer.model.SilentInterval;
import com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller for the Edit List screen.
 */
public class EditListFragment extends Fragment {
    private static final int LAYOUT = R.layout.fragment_list;
    private static final int LAYOUT_MENU = R.menu.fragment_list_item_edit;
    private static final int LAYOUT_LIST = R.id.recycler_view;
    private static final boolean HAS_OPTIONS_MENU = true;

    private MainActivityCallback mMainActivityCallback;
    private SilentIntervalList mSilentIntervalList;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;

    // Life Cycle ----------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityCallback = (MainActivityCallback) getActivity();
        mSilentIntervalList = SilentIntervalListAccess.getInstance(mMainActivityCallback.getContext());
        mSilentIntervalList.loadList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        mRecyclerView = view.findViewById(LAYOUT_LIST);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(HAS_OPTIONS_MENU);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                mSilentIntervalList.swap(fromPosition, toPosition);
                mAdapter.notifyItemMoved(fromPosition, toPosition);
                return false;
            }

            @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    // Menu ----------------------------------------------------------------------------------------

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(LAYOUT_MENU, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.accept_changes:
                mMainActivityCallback.openListFragment();
                break;
            case R.id.check_all_items:
                mAdapter.checkAllItems();
                break;
            case R.id.delete_items:
                mAdapter.deleteCheckedItems();
            default: break;
        }
        return true;
    }

    // Utilities -----------------------------------------------------------------------------------

    private void updateUI() {
        mSilentIntervalList = SilentIntervalListAccess.getInstance(mMainActivityCallback.getContext());
        mSilentIntervalList.loadList();
        if (mAdapter == null)mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    // ViewHolder class ----------------------------------------------------------------------------

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemEditBinding mBinding;
        private CheckBox mCheckBox;

        public ViewHolder(ListItemEditBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bind(SilentInterval interval) {
            SilentIntervalViewModel viewModel = new SilentIntervalViewModel(interval);
            mBinding.setViewModel(viewModel);
            mCheckBox = itemView.findViewById(R.id.edit_list_checkbox);
            mCheckBox.setChecked(mAdapter.isChecked(interval));
            mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> mAdapter.checkItem(interval, isChecked));
        }
    }

    // Adapter class -------------------------------------------------------------------------------

    private class Adapter extends RecyclerView.Adapter<ViewHolder>  {
        private static final int LAYOUT = R.layout.list_item_edit;
        private boolean mAllItemsChecked = false;
        private Set<SilentInterval> mAllItems;
        private Set<SilentInterval> mCheckedItems;

        public Adapter() {
            mCheckedItems = new HashSet<>();
            mAllItems = new HashSet<>();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            ListItemEditBinding binding = DataBindingUtil.inflate(layoutInflater, LAYOUT, parent, false);
            ViewHolder viewHolder = new ViewHolder(binding);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (getItemCount() > position) {
                SilentInterval interval = mSilentIntervalList.get(position);
                holder.bind(interval);
                mAllItems.add(interval);
                if (mAllItemsChecked)mCheckedItems.add(interval);
            }
        }

        @Override public int getItemCount() { return mSilentIntervalList.size(); }

        public boolean isChecked(SilentInterval interval) { return mAllItemsChecked || mCheckedItems.contains(interval); }

        public void checkAllItems() {
            mAllItemsChecked = !mAllItemsChecked;
            if (mAllItemsChecked)mCheckedItems.addAll(mAllItems);
            else mCheckedItems.clear();
            notifyDataSetChanged();
        }

        public void checkItem(SilentInterval interval, boolean isChecked) {
            if (isChecked) mCheckedItems.add(interval);
            else mCheckedItems.remove(interval);
        }

        public void deleteCheckedItems() {
            for (SilentInterval interval : mCheckedItems)
                mSilentIntervalList.remove(interval);
            mCheckedItems.clear();
            notifyDataSetChanged();
        }
    }
}
