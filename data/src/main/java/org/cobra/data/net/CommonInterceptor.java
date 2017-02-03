package org.cobra.data.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/8/17 0017.
 */

public class CommonInterceptor implements Interceptor {
    private long mOrgId;
    private long mUserId;

    public CommonInterceptor(long mOrgId, long mUserId) {
        this.mOrgId = mOrgId;
        this.mUserId = mUserId;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();

        if (!oldRequest.url().toString().contains("loginUser") && !oldRequest.url().toString().contains("checkVersion") && !oldRequest.url().toString().contains("sendSms")
                && !oldRequest.url().toString().contains("registerUser") && !oldRequest.url().toString().contains("addOrgUser") && !oldRequest.url().toString().contains("findOrgbyStr")
                && !oldRequest.url().toString().contains("addOrg") && !oldRequest.url().toString().contains("findExamineUser") &&
                !oldRequest.url().toString().contains("updatePwdBySms") && !oldRequest.url().toString().contains("addProgressUser")

                ) {
            if (mOrgId == 0 || mUserId == 0) {
                throw new ApiException(ApiException.REQUEST_DATA_ABNORMAL, "您的提交数据异常");
            }
        }

        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("orgId", mOrgId + "")
                .addQueryParameter("userId", mUserId + "");

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        return chain.proceed(newRequest);
    }
}
