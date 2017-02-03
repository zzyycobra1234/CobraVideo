package org.cobra.data.net;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Administrator on 2016/8/15 0015.
 */

//HttpStatus.java
public class HttpStatus {
    @SerializedName("state")
    private int mCode;
    @SerializedName("msg")
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        if (mCode == ApiException.REQUEST_HAD_MONTH_PLAN)
            return true;
        if (mCode == ApiException.REQUEST_HAD_WEEK_PLAN)
            return true;
        if (mCode == ApiException.REQUEST_NEW_VERSION)
            return true;

        return mCode != ApiException.REQUEST_SUCCEED;

    }

    public boolean isNewVersion() {
        return mCode != ApiException.REQUEST_NEW_VERSION;
    }


}