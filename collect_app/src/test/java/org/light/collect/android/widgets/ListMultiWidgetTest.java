package org.light.collect.android.widgets;

import android.support.annotation.NonNull;

import org.light.collect.android.widgets.base.GeneralSelectMultiWidgetTest;
import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */

public class ListMultiWidgetTest extends GeneralSelectMultiWidgetTest<ListMultiWidget> {
    @NonNull
    @Override
    public ListMultiWidget createWidget() {
        return new ListMultiWidget(RuntimeEnvironment.application, formEntryPrompt, true);
    }
}
