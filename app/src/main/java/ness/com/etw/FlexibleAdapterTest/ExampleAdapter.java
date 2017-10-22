package ness.com.etw.FlexibleAdapterTest;


import android.support.annotation.Nullable;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class ExampleAdapter extends FlexibleAdapter<AbstractFlexibleItem> {


    public ExampleAdapter(@Nullable List<AbstractFlexibleItem> items, @Nullable Object listeners) {
        super(items, listeners);
    }


    public boolean isEmpty() {
        return super.isEmpty();
    }

    public void showLayoutInfo(boolean scrollToPosition) {

    }
}
