package org.cobra.data.rx;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.cobra.data.R;
import org.cobra.data.net.ApiException;
import org.cobra.data.untils.ToastUtil;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class BaseSubscriber<T> extends Subscriber<T> {
    protected Context mContext;

    public BaseSubscriber(Context context) {
        this.mContext = context;
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(final Throwable e) {
        Log.w("Subscriber onError  ", e);
        if (e instanceof HttpException) {
            // We had non-2XX http error
            showToastMessage(mContext.getString(R.string.server_internal_error));
        } else if (e instanceof SocketTimeoutException) {
            showToastMessage(mContext.getString(R.string.cannot_connected_server_out_time));
        } else if (e instanceof ConnectException) {
            showToastMessage(mContext.getString(R.string.cannot_no_connected));
        } else if (e instanceof IOException) {
            // A network or conversion error happened
            showToastMessage(mContext.getString(R.string.cannot_connected_server));
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                ) {  //均视为解析错误
            showToastMessage("json解析出错");
        } else if (e instanceof ApiException) {
            ApiException exception = (ApiException) e;
            if (exception.isTokenExpried()) {
                //处理token失效对应的逻辑
            } else {
                handleApiException(exception);
            }
        }
    }

    @Override
    public void onNext(T t) {

    }


    private void handleApiException(ApiException exception) {
//        Logger.e("handleApiException");
        switch (exception.getErrorCode()) {
            case ApiException.REQUEST_FAIL:
                showToastMessage(exception.getMessage());
                break;
            case ApiException.REQUEST_ABNORMAL:
                showToastMessage(exception.getMessage());
                break;
            case ApiException.REQUEST_HAD_MONTH_PLAN:
                //                showToastMessage(exception.getMessage());
                sendReleasePlanFailMessage(exception.getMessage());
                break;
            case ApiException.REQUEST_HAD_WEEK_PLAN:
                //                showToastMessage(exception.getMessage());
                sendReleasePlanFailMessage(exception.getMessage());
                break;
            case ApiException.REQUEST_DATA_ABNORMAL:
                showToastMessage(exception.getMessage());
            default:
                showToastMessage(exception.getMessage());
                break;
            //            case Constant.REQUEST_NEW_VERSION:
            //                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
            //                break;
        }

    }


    public void sendReleasePlanFailMessage(String msg) {
//        WorkPlanEvent messageEvent = new WorkPlanEvent();
//        messageEvent.eventCode = (WorkPlanEvent.RELEASE_PLAN_EVENT_FAILED);
//        messageEvent.msg = msg;
//        RxBus.getDefault().send(messageEvent);
    }


    private void showToastMessage(String title) {
        if (mContext != null)
            ToastUtil.show(title);
    }


}