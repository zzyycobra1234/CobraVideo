package org.cobra.data.net;

import android.support.annotation.NonNull;

import org.cobra.data.app.BaseApp;
import org.cobra.data.app.Constants;
import org.cobra.data.untils.BaseSystemUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by Administrator on 2017/1/3 0003.
 */

public class RetrofitHelper {
    private static OkHttpClient sOkHttpClient = null;
    private static ClipServiceApi sClipServiceApi;
//    private static NetworkService sNetworkService;

    public static final String API_BASE_URL = "http://www.iclip365.com:9090/clip_basic1/"; // 正式服
//        public static final String API_BASE_URL = "http://172.16.0.200:8080/clip_basic/"; //测试服
    //    public static final String API_BASE_URL = "http://121.40.77.4:8080/clip_basic/"; //测试服
    //    public static final String API_BASE_URL = "http://a67819477.eicp.net:23817/clip_basic/";
    //    public static final String API_BASE_URL = "http://10.0.1.8:8080/clip_basic/";

    private void init() {
        initOkHttp();
        sClipServiceApi = getApiService(API_BASE_URL, ClipServiceApi.class);
//        sNetworkService = getApiService(API_BASE_URL, NetworkService.class);

    }


    public RetrofitHelper() {
        init();
    }

    /**
     * Okhttp3 配置
     *
     * @return
     */
    @NonNull
    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        if (BuildConfig.DEBUG) {
            // 添加网路日志打印
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            initHttpLogger(builder);
//        }

        // 添加公共参数
        CommonInterceptor commonInterceptor = new CommonInterceptor(
                BaseApp.sOrgId, BaseApp.sUserId);
        builder.addInterceptor(commonInterceptor);

        // 网络连接缓存
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!BaseSystemUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (BaseSystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };

        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        //        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);


        sOkHttpClient = builder.build();
    }

    private void initHttpLogger(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        builder.addInterceptor(loggingInterceptor);
    }


    private <T> T getApiService(String baseUrl, Class<T> clz) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(sOkHttpClient)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                // file 转换成上传文件
                // .addConverterFactory(new FileRequestBodyConverterFactory())
                .build();
        return retrofit.create(clz);
    }

//
//    //进入我的页面
//    public Observable<IntoMeEntity> requestIntoMe(long meUserId, long checkUserId) {
//        return sClipServiceApi.requestIntoMe(meUserId, checkUserId);
//    }
//
//    //    登录
//    public Observable<LoginBean> requestLoginUser(Map<String, String> userLogin) {
//        return sClipServiceApi.requestLoginUser(userLogin);
//    }



//    public void initTest(){
//        ToastUtil.shortShow("填充数据");
//    }

}
