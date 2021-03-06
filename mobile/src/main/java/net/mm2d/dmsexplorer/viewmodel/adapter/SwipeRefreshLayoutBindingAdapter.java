/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.viewmodel.adapter;

import android.databinding.BindingAdapter;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * @author <a href="mailto:ryo@mm2d.net">大前良介 (OHMAE Ryosuke)</a>
 */
public class SwipeRefreshLayoutBindingAdapter {
    @BindingAdapter("colorSchemeResources")
    public static void setColorSchemeResources(
            @NonNull final SwipeRefreshLayout view,
            @ColorRes int... colorResIds) {
        if (colorResIds == null) {
            return;
        }
        view.setColorSchemeResources(colorResIds);
    }
}
