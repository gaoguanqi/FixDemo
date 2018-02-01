package com.maple.smaple.fixdemo.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.maple.smaple.fixdemo.BuildConfig;
import com.maple.smaple.fixdemo.MainActivity;
import com.maple.smaple.fixdemo.R;
import com.meituan.android.walle.WalleChannelReader;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.BuglyStrategy;
import com.tencent.bugly.beta.Beta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


public class    AppController {

    private static AppController sInstance = null;
    private Application mApplication = null;
    private Context mContext = null;

    public void init(Application application, Context context) {
        this.mApplication = application;
        this.mContext = context;

        initBugly(mApplication);
    }
    private void initBugly(Application application) {
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.autoDownloadOnWifi = true;
        Beta.largeIconId = R.mipmap.ic_launcher;
        Beta.smallIconId = R.mipmap.ic_launcher;
        Beta.defaultBannerId = R.drawable.icon_default_loading;
        Beta.storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //已经确认过的弹窗在APP下次启动自动检查更新时会再次显示;
        Beta.showInterruptedStrategy = true;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        //Beta.canShowUpgradeActs.add(AboutUsActivity.class);
        BuglyStrategy strategy = new BuglyStrategy();
        // 获取当前进程名
        String processName = AppController.getProcessName(android.os.Process.myPid());
        strategy.setUploadProcess(processName == null || processName.equals(application.getPackageName()));
        //设置app渠道号
        String channel = WalleChannelReader.getChannel(application);
        strategy.setAppChannel(channel);
        Bugly.init(application, BuildConfig.BUGLY_APPID, BuildConfig.DEBUG, strategy);
    }
    /**
     * 获取实例
     *
     * @return AppController的实例
     */
    public static AppController getInstance() {
        if (sInstance == null) {
            synchronized (AppController.class) {
                if (sInstance == null) {
                    sInstance = new AppController();
                }
            }
        }
        return sInstance;
    }

    public Context getContext() {
        return mContext;
    }

    public Application getApplication() {
        return mApplication;
    }



    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 去往任意页面
     *
     * @param context       上下文
     * @param activityClass 目标类
     * @param map           参数
     */
    public static void forwardPage(Context context, Class activityClass, Map<String, Object> map, int flags) {
        Intent intent = new Intent(context, activityClass);
        if (map != null) {
            for (Map.Entry item : map.entrySet()) {
                intent.putExtra(item.getKey().toString(), item.getValue() == null ? "" : item.getValue().toString());
            }
        }
        intent.setFlags(flags);
        context.startActivity(intent);
    }
}