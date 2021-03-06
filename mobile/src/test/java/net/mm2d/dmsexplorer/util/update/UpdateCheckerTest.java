/*
 * Copyright (c) 2017 大前良介 (OHMAE Ryosuke)
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/MIT
 */

package net.mm2d.dmsexplorer.util.update;

import android.content.Context;

import net.mm2d.dmsexplorer.settings.Settings;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:ryo@mm2d.net">大前良介 (OHMAE Ryosuke)</a>
 */
@RunWith(RobolectricTestRunner.class)
public class UpdateCheckerTest {
    @Test
    public void isUpdateAvailable_異常な入力ならfalse() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 715;
        final String json1 = "{\"mobile\":{\"versionCode\":716,\"targetInclude\":[0],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json1), is(false));
        final String json2 = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[0],\"targetExclude\":[]}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json2), is(false));
    }

    @Test
    public void isUpdateAvailable_targetが1つで対象_バージョンが低ければtrue() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 715;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[0],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_targetが1つで対象_バージョンが等しければfalse() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 716;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[0],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_targetが1つで対象_バージョンが高ければfalse() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 717;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[0],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_targetに記載がなければfalse() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 715;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_1つで対象ならtrue() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 710;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[710],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_1つで対象外ならfalse() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 710;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[711],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_2つで対象ならtrue1() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 710;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[710,714],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_2つで対象ならtrue2() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 714;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[710,714],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_2つで対象ならtrue3() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 712;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[710,714],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_2つで対象外ならfalse1() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 709;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[710,714],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_2つで対象外ならfalse2() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 715;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[710,714],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_3つで対象ならtrue1() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 700;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_3つで対象ならtrue2() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 710;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_3つで対象ならtrue3() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 712;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_3つで対象ならtrue4() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 705;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_3つで対象ならtrue5() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 714;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_3つで対象外ならfalse1() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 600;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_3つで対象外ならfalse2() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 711;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,710,712],\"targetExclude\":[]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_対象で除外リストに該当しないならtrue1() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 711;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[0,714],\"targetExclude\":[710]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }


    @Test
    public void isUpdateAvailable_対象で除外リストに該当しないならtrue2() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 711;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[0,714],\"targetExclude\":[710,712]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(true));
    }

    @Test
    public void isUpdateAvailable_対象だが除外ならfalse1() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 711;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,714],\"targetExclude\":[711]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_対象だが除外ならfalse2() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 711;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,714],\"targetExclude\":[700,711]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }

    @Test
    public void isUpdateAvailable_対象だが除外ならfalse3() throws Exception {
        final Context context = RuntimeEnvironment.application;
        final Settings settings = mock(Settings.class);
        final int version = 711;
        final String json = "{\"mobile\":{\"versionName\":\"0.7.16\",\"versionCode\":716,\"targetInclude\":[700,714],\"targetExclude\":[711,712]}}";
        assertThat(new UpdateChecker(context, settings, version).isUpdateAvailable(json), is(false));
    }
}
