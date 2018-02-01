package com.maple.smaple.fixdemo.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by gaogu on 2018/2/1.
 */

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.maple.smaple.fixdemo.app.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}