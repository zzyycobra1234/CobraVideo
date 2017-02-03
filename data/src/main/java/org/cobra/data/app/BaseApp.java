package org.cobra.data.app;

import android.support.multidex.MultiDexApplication;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class BaseApp extends MultiDexApplication {
    private static BaseApp instance;
    public static long sOrgId;
    public static long sUserId;

    public static synchronized BaseApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
