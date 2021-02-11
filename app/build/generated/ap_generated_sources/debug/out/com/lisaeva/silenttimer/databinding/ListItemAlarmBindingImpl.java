package com.lisaeva.silenttimer.databinding;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ListItemAlarmBindingImpl extends ListItemAlarmBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ListItemAlarmBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 3, sIncludes, sViewsWithIds));
    }
    private ListItemAlarmBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            );
        this.alarmDate.setTag(null);
        this.alarmTitle.setTag(null);
        this.linearLayout.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.viewModel == variableId) {
            setViewModel((com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setViewModel(@Nullable com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel ViewModel) {
        updateRegistration(0, ViewModel);
        this.mViewModel = ViewModel;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.viewModel);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeViewModel((com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModel(com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel ViewModel, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        else if (fieldId == BR.title) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        else if (fieldId == BR.showDescription) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        else if (fieldId == BR.description) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        else if (fieldId == BR.listItemDisplayDate) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        java.lang.String viewModelShowDescriptionViewModelDescriptionViewModelListItemDisplayDate = null;
        boolean viewModelShowDescription = false;
        java.lang.String viewModelListItemDisplayDate = null;
        com.lisaeva.silenttimer.viewmodel.SilentAlarmViewModel viewModel = mViewModel;
        java.lang.String viewModelTitle = null;
        java.lang.String viewModelDescription = null;

        if ((dirtyFlags & 0x3fL) != 0) {


            if ((dirtyFlags & 0x3dL) != 0) {

                    if (viewModel != null) {
                        // read viewModel.showDescription
                        viewModelShowDescription = viewModel.getShowDescription();
                    }
                if((dirtyFlags & 0x3dL) != 0) {
                    if(viewModelShowDescription) {
                            dirtyFlags |= 0x80L;
                    }
                    else {
                            dirtyFlags |= 0x40L;
                    }
                }
            }
            if ((dirtyFlags & 0x23L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.title
                        viewModelTitle = viewModel.getTitle();
                    }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x40L) != 0) {

                if (viewModel != null) {
                    // read viewModel.listItemDisplayDate
                    viewModelListItemDisplayDate = viewModel.getListItemDisplayDate();
                }
        }
        if ((dirtyFlags & 0x80L) != 0) {

                if (viewModel != null) {
                    // read viewModel.description
                    viewModelDescription = viewModel.getDescription();
                }
        }

        if ((dirtyFlags & 0x3dL) != 0) {

                // read viewModel.showDescription ? viewModel.description : viewModel.listItemDisplayDate
                viewModelShowDescriptionViewModelDescriptionViewModelListItemDisplayDate = ((viewModelShowDescription) ? (viewModelDescription) : (viewModelListItemDisplayDate));
        }
        // batch finished
        if ((dirtyFlags & 0x3dL) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.alarmDate, viewModelShowDescriptionViewModelDescriptionViewModelListItemDisplayDate);
        }
        if ((dirtyFlags & 0x23L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.alarmTitle, viewModelTitle);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel
        flag 1 (0x2L): viewModel.title
        flag 2 (0x3L): viewModel.showDescription
        flag 3 (0x4L): viewModel.description
        flag 4 (0x5L): viewModel.listItemDisplayDate
        flag 5 (0x6L): null
        flag 6 (0x7L): viewModel.showDescription ? viewModel.description : viewModel.listItemDisplayDate
        flag 7 (0x8L): viewModel.showDescription ? viewModel.description : viewModel.listItemDisplayDate
    flag mapping end*/
    //end
}