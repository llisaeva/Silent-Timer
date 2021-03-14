package com.lisaeva.silenttimer;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.lisaeva.silenttimer.databinding.FragmentSilentIntervalBindingImpl;
import com.lisaeva.silenttimer.databinding.ListItemSilentIntervalBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_FRAGMENTSILENTINTERVAL = 1;

  private static final int LAYOUT_LISTITEMSILENTINTERVAL = 2;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(2);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lisaeva.silenttimer.R.layout.fragment_silent_interval, LAYOUT_FRAGMENTSILENTINTERVAL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lisaeva.silenttimer.R.layout.list_item_silent_interval, LAYOUT_LISTITEMSILENTINTERVAL);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_FRAGMENTSILENTINTERVAL: {
          if ("layout/fragment_silent_interval_0".equals(tag)) {
            return new FragmentSilentIntervalBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_silent_interval is invalid. Received: " + tag);
        }
        case  LAYOUT_LISTITEMSILENTINTERVAL: {
          if ("layout/list_item_silent_interval_0".equals(tag)) {
            return new ListItemSilentIntervalBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for list_item_silent_interval is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(19);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "active");
      sKeys.put(2, "description");
      sKeys.put(3, "duration");
      sKeys.put(4, "endDate");
      sKeys.put(5, "friday");
      sKeys.put(6, "handler");
      sKeys.put(7, "listItemDisplayDate");
      sKeys.put(8, "monday");
      sKeys.put(9, "repeat");
      sKeys.put(10, "saturday");
      sKeys.put(11, "showDescription");
      sKeys.put(12, "startDate");
      sKeys.put(13, "sunday");
      sKeys.put(14, "thursday");
      sKeys.put(15, "title");
      sKeys.put(16, "tuesday");
      sKeys.put(17, "viewModel");
      sKeys.put(18, "wednesday");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(2);

    static {
      sKeys.put("layout/fragment_silent_interval_0", com.lisaeva.silenttimer.R.layout.fragment_silent_interval);
      sKeys.put("layout/list_item_silent_interval_0", com.lisaeva.silenttimer.R.layout.list_item_silent_interval);
    }
  }
}
