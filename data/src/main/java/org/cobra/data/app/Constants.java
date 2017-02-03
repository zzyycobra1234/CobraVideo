package org.cobra.data.app;

import java.io.File;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class Constants {
    //================= LOGIN ====================
    public static final String AUTO_LOGIN = "auto_login";

    //================= PATH ====================

    public static final String PATH_DATA = BaseApp.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
}
