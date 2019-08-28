package org.light.collect.android.widgets;

import android.support.annotation.NonNull;

import org.light.collect.android.widgets.base.GeneralSelectOneWidgetTest;
import org.robolectric.RuntimeEnvironment;

/**
 * @author James Knight
 */

public class SelectOneWidgetTest extends GeneralSelectOneWidgetTest<AbstractSelectOneWidget> {

    @NonNull
    @Override
    public SelectOneWidget createWidget() {
        return new SelectOneWidget(RuntimeEnvironment.application, formEntryPrompt, false);
    }
}
