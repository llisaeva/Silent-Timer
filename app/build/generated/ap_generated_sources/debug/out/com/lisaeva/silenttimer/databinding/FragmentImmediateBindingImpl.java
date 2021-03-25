package com.lisaeva.silenttimer.databinding;
import com.lisaeva.silenttimer.R;
import com.lisaeva.silenttimer.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentImmediateBindingImpl extends FragmentImmediateBinding implements com.lisaeva.silenttimer.generated.callback.OnClickListener.Listener {

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
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.LinearLayout mboundView1;
    @NonNull
    private final android.widget.LinearLayout mboundView10;
    @NonNull
    private final android.widget.LinearLayout mboundView4;
    @NonNull
    private final android.widget.LinearLayout mboundView7;
    // variables
    @Nullable
    private final android.view.View.OnClickListener mCallback4;
    @Nullable
    private final android.view.View.OnClickListener mCallback2;
    @Nullable
    private final android.view.View.OnClickListener mCallback5;
    @Nullable
    private final android.view.View.OnClickListener mCallback3;
    @Nullable
    private final android.view.View.OnClickListener mCallback1;
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentImmediateBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 14, sIncludes, sViewsWithIds));
    }
    private FragmentImmediateBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.Button) bindings[13]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[12]
            );
        this.chooseClockTime.setTag(null);
        this.immediateTime1.setTag(null);
        this.immediateTime2.setTag(null);
        this.immediateTime3.setTag(null);
        this.immediateTime4.setTag(null);
        this.immediateUnit1.setTag(null);
        this.immediateUnit2.setTag(null);
        this.immediateUnit3.setTag(null);
        this.immediateUnit4.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.LinearLayout) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView10 = (android.widget.LinearLayout) bindings[10];
        this.mboundView10.setTag(null);
        this.mboundView4 = (android.widget.LinearLayout) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView7 = (android.widget.LinearLayout) bindings[7];
        this.mboundView7.setTag(null);
        setRootTag(root);
        // listeners
        mCallback4 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 4);
        mCallback2 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 2);
        mCallback5 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 5);
        mCallback3 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 3);
        mCallback1 = new com.lisaeva.silenttimer.generated.callback.OnClickListener(this, 1);
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
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
        if (BR.handler == variableId) {
            setHandler((com.lisaeva.silenttimer.ui.ImmediateFragment) variable);
        }
        else if (BR.viewModel == variableId) {
            setViewModel((com.lisaeva.silenttimer.viewmodel.ImmediateViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setHandler(@Nullable com.lisaeva.silenttimer.ui.ImmediateFragment Handler) {
        this.mHandler = Handler;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.handler);
        super.requestRebind();
    }
    public void setViewModel(@Nullable com.lisaeva.silenttimer.viewmodel.ImmediateViewModel ViewModel) {
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
                return onChangeViewModel((com.lisaeva.silenttimer.viewmodel.ImmediateViewModel) object, fieldId);
        }
        return false;
    }
    private boolean onChangeViewModel(com.lisaeva.silenttimer.viewmodel.ImmediateViewModel ViewModel, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
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
        java.lang.String viewModelGetUnitInt3 = null;
        java.lang.String viewModelGetTimeInt4 = null;
        java.lang.String viewModelGetUnitInt4 = null;
        java.lang.String viewModelGetTimeInt1 = null;
        com.lisaeva.silenttimer.ui.ImmediateFragment handler = mHandler;
        java.lang.String viewModelGetUnitInt1 = null;
        java.lang.String viewModelGetTimeInt2 = null;
        java.lang.String viewModelGetUnitInt2 = null;
        com.lisaeva.silenttimer.viewmodel.ImmediateViewModel viewModel = mViewModel;
        java.lang.String viewModelGetTimeInt3 = null;

        if ((dirtyFlags & 0x5L) != 0) {



                if (viewModel != null) {
                    // read viewModel.getUnit(3)
                    viewModelGetUnitInt3 = viewModel.getUnit(3);
                    // read viewModel.getTime(4)
                    viewModelGetTimeInt4 = viewModel.getTime(4);
                    // read viewModel.getUnit(4)
                    viewModelGetUnitInt4 = viewModel.getUnit(4);
                    // read viewModel.getTime(1)
                    viewModelGetTimeInt1 = viewModel.getTime(1);
                    // read viewModel.getUnit(1)
                    viewModelGetUnitInt1 = viewModel.getUnit(1);
                    // read viewModel.getTime(2)
                    viewModelGetTimeInt2 = viewModel.getTime(2);
                    // read viewModel.getUnit(2)
                    viewModelGetUnitInt2 = viewModel.getUnit(2);
                    // read viewModel.getTime(3)
                    viewModelGetTimeInt3 = viewModel.getTime(3);
                }
        }
        // batch finished
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            this.chooseClockTime.setOnClickListener(mCallback5);
            this.mboundView1.setOnClickListener(mCallback1);
            this.mboundView10.setOnClickListener(mCallback4);
            this.mboundView4.setOnClickListener(mCallback2);
            this.mboundView7.setOnClickListener(mCallback3);
        }
        if ((dirtyFlags & 0x5L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateTime1, viewModelGetTimeInt1);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateTime2, viewModelGetTimeInt2);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateTime3, viewModelGetTimeInt3);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateTime4, viewModelGetTimeInt4);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateUnit1, viewModelGetUnitInt1);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateUnit2, viewModelGetUnitInt2);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateUnit3, viewModelGetUnitInt3);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.immediateUnit4, viewModelGetUnitInt4);
        }
    }
    // Listener Stub Implementations
    // callback impls
    public final void _internalCallbackOnClick(int sourceId , android.view.View callbackArg_0) {
        switch(sourceId) {
            case 4: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.ImmediateFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {



                    handler.onClickTime(4);
                }
                break;
            }
            case 2: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.ImmediateFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {



                    handler.onClickTime(2);
                }
                break;
            }
            case 5: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.ImmediateFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {


                    handler.chooseClockTime();
                }
                break;
            }
            case 3: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.ImmediateFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {



                    handler.onClickTime(3);
                }
                break;
            }
            case 1: {
                // localize variables for thread safety
                // handler
                com.lisaeva.silenttimer.ui.ImmediateFragment handler = mHandler;
                // handler != null
                boolean handlerJavaLangObjectNull = false;



                handlerJavaLangObjectNull = (handler) != (null);
                if (handlerJavaLangObjectNull) {



                    handler.onClickTime(1);
                }
                break;
            }
        }
    }
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): viewModel
        flag 1 (0x2L): handler
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}