/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import net.mm2d.android.upnp.avt.MediaRenderer;
import net.mm2d.android.upnp.avt.MrControlPoint;
import net.mm2d.android.upnp.avt.MrControlPoint.MrDiscoveryListener;
import net.mm2d.android.util.AribUtils;
import net.mm2d.dmsexplorer.BR;
import net.mm2d.dmsexplorer.R;
import net.mm2d.dmsexplorer.Repository;
import net.mm2d.dmsexplorer.domain.entity.ContentEntity;
import net.mm2d.dmsexplorer.domain.model.MediaServerModel;
import net.mm2d.dmsexplorer.settings.Settings;
import net.mm2d.dmsexplorer.util.ItemSelectUtils;
import net.mm2d.dmsexplorer.util.ThemeUtils;
import net.mm2d.dmsexplorer.view.adapter.PropertyAdapter;
import net.mm2d.dmsexplorer.view.dialog.DeleteDialog;

/**
 * @author <a href="mailto:ryo@mm2d.net">大前良介 (OHMAE Ryosuke)</a>
 */
public class ContentDetailFragmentModel extends BaseObservable {
    public final int collapsedColor;
    public final int expandedColor;
    @NonNull
    public final String title;
    @NonNull
    public final PropertyAdapter propertyAdapter;
    public final boolean hasResource;
    public final boolean isProtected;

    private final boolean mCanDelete;
    private boolean mCanSend;
    private boolean mDeleteEnabled;

    @NonNull
    private final Settings mSettings;
    @NonNull
    private final FragmentActivity mActivity;
    @NonNull
    private final MrControlPoint mMrControlPoint;
    private final MrDiscoveryListener mMrDiscoveryListener = new MrDiscoveryListener() {
        @Override
        public void onDiscover(@NonNull final MediaRenderer server) {
            updateCanSend();
        }

        @Override
        public void onLost(@NonNull final MediaRenderer server) {
            updateCanSend();
        }
    };

    public ContentDetailFragmentModel(
            @NonNull final FragmentActivity activity,
            @NonNull final Repository repository) {
        final MediaServerModel model = repository.getMediaServerModel();
        if (model == null) {
            throw new IllegalStateException();
        }
        final ContentEntity entity = model.getSelectedEntity();
        if (entity == null) {
            throw new IllegalStateException();
        }
        mActivity = activity;
        final String rawTitle = entity.getName();
        title = AribUtils.toDisplayableString(rawTitle);
        propertyAdapter = PropertyAdapter.ofContent(activity, entity);
        if (activity.getResources().getBoolean(R.bool.two_pane)) {
            collapsedColor = ThemeUtils.getSlightColor(rawTitle);
            expandedColor = ThemeUtils.getSlightColor(rawTitle);
        } else {
            collapsedColor = ThemeUtils.getVividColor(rawTitle);
            expandedColor = ThemeUtils.getPastelColor(rawTitle);
        }
        hasResource = entity.hasResource();
        isProtected = entity.isProtected();
        mCanDelete = model.canDelete(entity);
        mSettings = new Settings(activity);
        setDeleteEnabled(mSettings.isDeleteFunctionEnabled() && mCanDelete);

        mMrControlPoint = repository.getControlPointModel().getMrControlPoint();
        updateCanSend();
        mMrControlPoint.addMrDiscoveryListener(mMrDiscoveryListener);
    }

    public void onResume() {
        setDeleteEnabled(mSettings.isDeleteFunctionEnabled() && mCanDelete);
    }

    @Bindable
    public boolean getCanSend() {
        return mCanSend;
    }

    @Bindable
    public boolean isDeleteEnabled() {
        return mDeleteEnabled;
    }

    private void setDeleteEnabled(final boolean enabled) {
        mDeleteEnabled = enabled;
        notifyPropertyChanged(BR.deleteEnabled);
    }

    private void updateCanSend() {
        mCanSend = mMrControlPoint.getDeviceListSize() > 0 && hasResource;
        notifyPropertyChanged(BR.canSend);
    }

    public void terminate() {
        mMrControlPoint.removeMrDiscoveryListener(mMrDiscoveryListener);
    }

    public void onClickPlay(@NonNull final View view) {
        if (isProtected) {
            showSnackbar(view);
        } else {
            ItemSelectUtils.play(mActivity, 0);
        }
    }

    public boolean onLongClickPlay(@NonNull final View view) {
        if (isProtected) {
            showSnackbar(view);
        } else {
            ItemSelectUtils.play(mActivity);
        }
        return true;
    }

    private static void showSnackbar(@NonNull final View view) {
        Snackbar.make(view, R.string.toast_not_support_drm, Snackbar.LENGTH_LONG).show();
    }

    public void onClickSend(@NonNull final View view) {
        ItemSelectUtils.send(mActivity);
    }

    public void onClickDelete(@NonNull final View view) {
        DeleteDialog.show(mActivity);
    }
}
