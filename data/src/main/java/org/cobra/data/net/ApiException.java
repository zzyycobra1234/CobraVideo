package org.cobra.data.net;

/**
 * Created by Administrator on 2016/8/15 0015.
 */

public class ApiException extends RuntimeException {
    private int mErrorCode;


    /**
     * 请求成功
     */
    public static final int REQUEST_SUCCEED = 1;
    /**
     * 请求失败
     */
    public static final int REQUEST_FAIL = -1;

    public static final int REQUEST_DATA_ABNORMAL = -201;
    /**
     * 请求异常
     */
    public static final int REQUEST_ABNORMAL = -2;

    /**
     * 有新版本
     */
    public static final int REQUEST_NEW_VERSION = -100;
    /**
     * 已有周计划
     */
    public static final int REQUEST_HAD_WEEK_PLAN = -101;
    /**
     * 已有月计划
     */
    public static final int REQUEST_HAD_MONTH_PLAN = -102;



    public ApiException(int errorCode, String errorMessage) {
        super(errorMessage);
        mErrorCode = errorCode;
    }

    /**
     * 判断是否是token失效
     *
     * @return 失效返回true, 否则返回false;
     */
    public boolean isTokenExpried() {
        //        return mErrorCode == Constants.TOKEN_EXPRIED;
        return false;
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
