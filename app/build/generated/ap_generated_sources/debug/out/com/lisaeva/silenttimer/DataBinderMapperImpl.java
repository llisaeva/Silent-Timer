package com.lisaeva.silenttimer;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.lisaeva.silenttimer.databinding.FragmentAlarmBindingImpl;
import com.lisaeva.silenttimer.databinding.ListItemAlarmBindingImpl;
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
  private static final int LAYOUT_FRAGMENTALARM = 1;

  private static final int LAYOUT_LISTITEMALARM = 2;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(2);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lisaeva.silenttimer.R.layout.fragment_alarm, LAYOUT_FRAGMENTALARM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.lisaeva.silenttimer.R.layout.list_item_alarm, LAYOUT_LISTITEMALARM);
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
        case  LAYOUT_FRAGMENTALARM: {
          if ("layout/fragment_alarm_0".equals(tag)) {
            return new FragmentAlarmBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_alarm is invalid. Received: " + tag);
        }
        case  LAYOUT_LISTITEMALARM: {
          if ("layout/list_item_alarm_0".equals(tag)) {
            return new ListItemAlarmBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for list_item_alarm is invalid. Received: " + tag);
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
    static final SparseArray<String> sKeys = new SparseArray<String>(18);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "description");
      sKeys.put(2, "duration");
      sKeys.put(3, "endDate");
      sKeys.put(4, "friday");
      sKeys.put(5, "handler");
      sKeys.put(6, "listItemDisplayDate");
      sKeys.put(7, "monday");
      sKeys.put(8, "repeat");
      sKeys.put(9, "saturday");
      sKeys.put(10, "showDescription");
      sKeys.put(11, "startDate");
      sKeys.put(12, "sunday");
      sKeys.put(13, "thursday");
      sKeys.put(14, "title");
      sKeys.put(15, "tuesday");
      sKeys.put(16, "viewModel");
      sKeys.put(17, "wednesday");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(2);

    static {
      sKeys.put("layout/fragment_alarm_0", com.lisaeva.silenttimer.R.layout.fragment_alarm);
      sKeys.put("layout/list_item_alarm_0", com.lisaeva.silenttimer.R.layout.list_item_alarm);
    }
  }
}
