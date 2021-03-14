package com.lisaeva.silenttimer.databinding;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentSilentIntervalBindingImpl extends FragmentSilentIntervalBinding implements com.lisaeva.silenttimer.generated.callback.OnClickListener.Listener {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = null;
    }
    // views
    @NonNull
    private final androidx.core.widget.NestedScrollView mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView5;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener checkboxFridayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.friday
            //         is viewModel.setFriday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxFriday.isChecked();
            // localize variables for thread safety
            // viewModel.friday
            boolean viewModelFriday = false;
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setFriday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxMondayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.monday
            //         is viewModel.setMonday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxMonday.isChecked();
            // localize variables for thread safety
            // viewModel.monday
            boolean viewModelMonday = false;
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setMonday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxRepeatandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.repeat
            //         is viewModel.setRepeat((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxRepeat.isChecked();
            // localize variables for thread safety
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;
            // viewModel.repeat
            boolean viewModelRepeat = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setRepeat(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxSaturdayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.saturday
            //         is viewModel.setSaturday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxSaturday.isChecked();
            // localize variables for thread safety
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;
            // viewModel.saturday
            boolean viewModelSaturday = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setSaturday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxShowDiscriptionandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.showDescription
            //         is viewModel.setShowDescription((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxShowDiscription.isChecked();
            // localize variables for thread safety
            // viewModel.showDescription
            boolean viewModelShowDescription = false;
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setShowDescription(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxSundayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.sunday
            //         is viewModel.setSunday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxSunday.isChecked();
            // localize variables for thread safety
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;
            // viewModel.sunday
            boolean viewModelSunday = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setSunday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxThursdayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.thursday
            //         is viewModel.setThursday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxThursday.isChecked();
            // localize variables for thread safety
            // viewModel.thursday
            boolean viewModelThursday = false;
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setThursday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxTuesdayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.tuesday
            //         is viewModel.setTuesday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxTuesday.isChecked();
            // localize variables for thread safety
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;
            // viewModel.tuesday
            boolean viewModelTuesday = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setTuesday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener checkboxWednesdayandroidCheckedAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.wednesday
            //         is viewModel.setWednesday((boolean) callbackArg_0)
            boolean callbackArg_0 = checkboxWednesday.isChecked();
            // localize variables for thread safety
            // viewModel.wednesday
            boolean viewModelWednesday = false;
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setWednesday(((boolean) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener labelDescriptionandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.description
            //         is viewModel.setDescription((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(labelDescription);
            // localize variables for thread safety
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel.description
            java.lang.String viewModelDescription = null;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setDescription(((java.lang.String) (callbackArg_0)));
            }
        }
    };
    private androidx.databinding.InverseBindingListener labelTitleandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of viewModel.title
            //         is viewModel.setTitle((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(labelTitle);
            // localize variables for thread safety
            // viewModel
            com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;
            // viewModel.title
            java.lang.String viewModelTitle = null;
            // viewModel != null
            boolean viewModelJavaLangObjectNull = false;



            viewModelJavaLangObjectNull = (viewModel) != (null);
            if (viewModelJavaLangObjectNull) {




                viewModel.setTitle(((java.lang.String) (callbackArg_0)));
            }
        }
    };

    public FragmentSilentIntervalBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 16, sIncludes, sViewsWithIds));
    }
    private FragmentSilentIntervalBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.CheckBox) bindings[11]
            , (android.widget.CheckBox) bindings[7]
            , (android.widget.CheckBox) bindings[4]
            , (android.widget.CheckBox) bindings[12]
            , (android.widget.CheckBox) bindings[13]
            , (android.widget.CheckBox) bindings[6]
            , (android.widget.CheckBox) bindings[10]
            , (android.widget.CheckBox) bindings[8]
            , (android.widget.CheckBox) bindings[9]
            , (android.widget.TextView) bindings[3]
            , (android.widget.EditText) bindings[14]
            , (android.widget.TextView) bindings[15]
            , (android.widget.EditText) bindings[1]
            , (android.widget.TextView) bindings[2]
            );
        this.checkboxFriday.setTag(null);
        this.checkboxMonday.setTag(null);
        this.checkboxRepeat.setTag(null);
        this.checkboxSaturday.setTag(null);
        this.checkboxShowDiscription.setTag(null);
        this.checkboxSunday.setTag(null);
        this.checkboxThursday.setTag(null);
        this.checkboxTuesday.setTag(null);
        this.checkboxWednesday.setTag(null);
        this.endDateLabel.setTag(null);
        this.labelDescription.setTag(null);
        this.labelDuration.setTag(null);
        this.labelTitle.setTag(null);
        this.mboundView0 = (androidx.core.widget.NestedScrollView) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView5 = (android.widget.LinearLayout) bindings[5];
        this.mboundView5.setTag(null);
        this.startDateLabel.setTag(null);
        setRootTag(root);
        // listeners
        mCallback2 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 2);
        mCallback1 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x20000L;
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
        if (BR.repeat == variableId) {
            setRepeat((java.lang.Boolean) variable);
        }
        else if (BR.handler == variableId) {
            setHandler((com.lisaeva.silenttimer.ui.SilentIntervalFragment) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setRepeat(@Nullable java.lang.Boolean Repeat) {
        this.mRepeat = Repeat;
    }
    public void setHandler(@Nullable com.lisaeva.silenttimer.ui.SilentIntervalFragment Handler) {
        this.mHandler = Handler;
        synchronized(this) {
            mDirtyFlags |= 0x4L;
        }
        notifyPropertyChanged(BR.handler);
        super.requestRebind();
    }
    public void setViewModel(@Nullable com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel ViewModel) {
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
                return onChangeViewModel((com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModel(com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel ViewModel, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        else if (fieldId == BR.title) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        else if (fieldId == BR.startDate) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        else if (fieldId == BR.endDate) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        else if (fieldId == BR.repeat) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        else if (fieldId == BR.sunday) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        else if (fieldId == BR.monday) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        else if (fieldId == BR.tuesday) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        else if (fieldId == BR.wednesday) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        else if (fieldId == BR.thursday) {
            synchronized(this) {
                    mDirtyFlags |= 0x800L;
            }
            return true;
        }
        else if (fieldId == BR.friday) {
            synchronized(this) {
                    mDirtyFlags |= 0x1000L;
            }
            return true;
        }
        else if (fieldId == BR.saturday) {
            synchronized(this) {
                    mDirtyFlags |= 0x2000L;
            }
            return true;
        }
        else if (fieldId == BR.showDescription) {
            synchronized(this) {
                    mDirtyFlags |= 0x4000L;
            }
            return true;
        }
        else if (fieldId == BR.description) {
            synchronized(this) {
                    mDirtyFlags |= 0x8000L;
            }
            return true;
        }
        else if (fieldId == BR.duration) {
            synchronized(this) {
                    mDirtyFlags |= 0x10000L;
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
        java.lang.String viewModelEndDate = null;
        java.lang.String viewModelDuration = null;
        boolean viewModelWednesday = false;
        boolean viewModelShowDescription = false;
        java.lang.String viewModelStartDate = null;
        boolean viewModelThursday = false;
        java.lang.String viewModelTitle = null;
        java.lang.String viewModelDescription = null;
        int viewModelShowDescriptionViewVISIBLEViewGONE = 0;
        boolean viewModelRepeat = false;
        boolean viewModelSaturday = false;
        boolean viewModelMonday = false;
        boolean viewModelFriday = false;
        boolean viewModelTuesday = false;
        com.lisaeva.silenttimer.ui.SilentIntervalFragment handler = mHandler;
        boolean viewModelSunday = false;
        int viewModelRepeatViewVISIBLEViewGONE = 0;
        com.lisaeva.silenttimer.viewmodel.SilentIntervalViewModel viewModel = mViewModel;

        if ((dirtyFlags & 0x3fff9L) != 0) {


            if ((dirtyFlags & 0x20021L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.endDate
                        viewModelEndDate = viewModel.getEndDate();
                    }
            }
            if ((dirtyFlags & 0x30001L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.duration
                        viewModelDuration = viewModel.getDuration();
                    }
            }
            if ((dirtyFlags & 0x20401L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.wednesday
                        viewModelWednesday = viewModel.getWednesday();
                    }
            }
            if ((dirtyFlags & 0x24001L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.showDescription
                        viewModelShowDescription = viewModel.getShowDescription();
                    }
                if((dirtyFlags & 0x24001L) != 0) {
                    if(viewModelShowDescription) {
                            dirtyFlags |= 0x80000L;
                    }
                    else {
                            dirtyFlags |= 0x40000L;
                    }
                }


                    // read viewModel.showDescription ? View.VISIBLE : View.GONE
                    viewModelShowDescriptionViewVISIBLEViewGONE = ((viewModelShowDescription) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x20011L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.startDate
                        viewModelStartDate = viewModel.getStartDate();
                    }
            }
            if ((dirtyFlags & 0x20801L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.thursday
                        viewModelThursday = viewModel.getThursday();
                    }
            }
            if ((dirtyFlags & 0x20009L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.title
                        viewModelTitle = viewModel.getTitle();
                    }
            }
            if ((dirtyFlags & 0x28001L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.description
                        viewModelDescription = viewModel.getDescription();
                    }
            }
            if ((dirtyFlags & 0x20041L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.repeat
                        viewModelRepeat = viewModel.getRepeat();
                    }
                if((dirtyFlags & 0x20041L) != 0) {
                    if(viewModelRepeat) {
                            dirtyFlags |= 0x200000L;
                    }
                    else {
                            dirtyFlags |= 0x100000L;
                    }
                }


                    // read viewModel.repeat ? View.VISIBLE : View.GONE
                    viewModelRepeatViewVISIBLEViewGONE = ((viewModelRepeat) ? (android.view.View.VISIBLE) : (android.view.View.GONE));
            }
            if ((dirtyFlags & 0x22001L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.saturday
                        viewModelSaturday = viewModel.getSaturday();
                    }
            }
            if ((dirtyFlags & 0x20101L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.monday
                        viewModelMonday = viewModel.getMonday();
                    }
            }
            if ((dirtyFlags & 0x21001L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.friday
                        viewModelFriday = viewModel.getFriday();
                    }
            }
            if ((dirtyFlags & 0x20201L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.tuesday
                        viewModelTuesday = viewModel.getTuesday();
                    }
            }
            if ((dirtyFlags & 0x20081L) != 0) {

                    if (viewModel != null) {
                        // read viewModel.sunday
                        viewModelSunday = viewModel.getSunday();
                    }
            }
        }
        // batch finished
        if ((dirtyFlags & 0x21001L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxFriday, viewModelFriday);
        }
        if ((dirtyFlags & 0x20000L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxFriday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxFridayandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxMonday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxMondayandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxRepeat, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxRepeatandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxSaturday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxSaturdayandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxShowDiscription, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxShowDiscriptionandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxSunday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxSundayandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxThursday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxThursdayandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxTuesday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxTuesdayandroidCheckedAttrChanged);
            androidx.databinding.adapters.CompoundButtonBindingAdapter.setListeners(this.checkboxWednesday, (android.widget.CompoundButton.OnCheckedChangeListener)null, checkboxWednesdayandroidCheckedAttrChanged);
            this.endDateLabel.setOnClickListener(mCallback2);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.labelDescription, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, labelDescriptionandroidTextAttrChanged);
            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.labelTitle, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, labelTitleandroidTextAttrChanged);
            this.startDateLabel.setOnClickListener(mCallback1);
        }
        if ((dirtyFlags & 0x20101L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxMonday, viewModelMonday);
        }
        if ((dirtyFlags & 0x20041L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxRepeat, viewModelRepeat);
            this.mboundView5.setVisibility(viewModelRepeatViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x22001L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxSaturday, viewModelSaturday);
        }
        if ((dirtyFlags & 0x24001L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxShowDiscription, viewModelShowDescription);
            this.labelDescription.setVisibility(viewModelShowDescriptionViewVISIBLEViewGONE);
        }
        if ((dirtyFlags & 0x20081L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxSunday, viewModelSunday);
        }
        if ((dirtyFlags & 0x20801L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxThursday, viewModelThursday);
        }
        if ((dirtyFlags & 0x20201L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxTuesday, viewModelTuesday);
        }
        if ((dirtyFlags & 0x20401L) != 0) {
            // api target 1

            androidx.databinding.adapters.CompoundButtonBindingAdapter.setChecked(this.checkboxWednesday, viewModelWednesday);
        }
        if ((dirtyFlags & 0x20021L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.endDateLabel, viewModelEndDate);
        }
        if ((dirtyFlags & 0x28001L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.labelDescription, viewModelDescription);
        }
        if ((dirtyFlags & 0x30001L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.labelDuration, viewModelDuration);
        }
        if ((dirtyFlags & 0x20009L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.labelTitle, viewModelTitle);
        }
        if ((dirtyFlags & 0x20011L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.startDateLabel, viewModelStartDate);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 2: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.SilentIntervalFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {


                    handler.onClickEndDate();
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.SilentIntervalFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {


                    handler.onClickStartDate();
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel
        flag 1 (0x2L): repeat
        flag 2 (0x3L): handler
        flag 3 (0x4L): viewModel.title
        flag 4 (0x5L): viewModel.startDate
        flag 5 (0x6L): viewModel.endDate
        flag 6 (0x7L): viewModel.repeat
        flag 7 (0x8L): viewModel.sunday
        flag 8 (0x9L): viewModel.monday
        flag 9 (0xaL): viewModel.tuesday
        flag 10 (0xbL): viewModel.wednesday
        flag 11 (0xcL): viewModel.thursday
        flag 12 (0xdL): viewModel.friday
        flag 13 (0xeL): viewModel.saturday
        flag 14 (0xfL): viewModel.showDescription
        flag 15 (0x10L): viewModel.description
        flag 16 (0x11L): viewModel.duration
        flag 17 (0x12L): null
        flag 18 (0x13L): viewModel.showDescription ? View.VISIBLE : View.GONE
        flag 19 (0x14L): viewModel.showDescription ? View.VISIBLE : View.GONE
        flag 20 (0x15L): viewModel.repeat ? View.VISIBLE : View.GONE
        flag 21 (0x16L): viewModel.repeat ? View.VISIBLE : View.GONE
    flag mapping end*/
    //end
}