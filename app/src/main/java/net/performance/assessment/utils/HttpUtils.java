package net.performance.assessment.utils;

import net.performance.assessment.interfaces.HttpCallback;
import net.performance.assessment.network.http.SimpleHttpCallback;

import org.apache.http.conn.ConnectTimeoutException;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * HTTP网络请求辅助类
 */
public class HttpUtils {
    private final static String TAG = "HttpUtils";

    //json请求
    private final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // 超时时间
    public static final int TIMEOUT = 60;

    public static final int DEFAULT_ERROR = -1;

    private static volatile HttpUtils instance;

    private OkHttpClient mClient;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    //请求标识
    private AtomicLong mHttpCount = new AtomicLong(0);

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public HttpUtils() {
        mClient = new OkHttpClient.Builder().connectTimeout(TIMEOUT,
                TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS).readTimeout(TIMEOUT,
                TimeUnit.SECONDS).build();
    }

    public long asyncJsonPost(String url, String json, final SimpleHttpCallback callback) {
        LogUtils.e(url);
        LogUtils.e(json);
        return asyncJsonPost(url, json, callback.getRequestTag(), callback);
    }

    public long asyncJsonPost(String url, String json, Object requestTag,
                              final HttpCallback callback) {
        final long flag = mHttpCount.incrementAndGet();

        LogUtils.v("request url---" + url);
        LogUtils.v("json body---" + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).tag(requestTag).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (e instanceof SocketException) {
                    //如主动取消则不回调
                    return;
                }
                if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
                    onResponseTimeout(flag, callback);
                } else {
                    onResponseFailure(DEFAULT_ERROR, flag, callback);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.v(TAG, "statusCode---" + response.code());
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    LogUtils.v(TAG, result);
                    LogUtils.e(result);
                    onResponseSuccess(result, flag, callback);
                } else {
                    onResponseFailure(DEFAULT_ERROR, flag, callback);
                }
            }
        });

        return flag;
    }

    public long asyncJsonGet(String url, final HttpCallback callback) {
        final long flag = mHttpCount.incrementAndGet();

        Request request = new Request.Builder().url(url).get().build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (e instanceof SocketTimeoutException || e instanceof ConnectTimeoutException) {
                    onResponseTimeout(flag, callback);
                } else {
                    onResponseFailure(DEFAULT_ERROR, flag, callback);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LogUtils.v(TAG, "statusCode---" + response.code());
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    LogUtils.v(TAG, result);
                    onResponseSuccess(result, flag, callback);
                } else {
                    onResponseFailure(DEFAULT_ERROR, flag, callback);
                }
            }
        });

        return flag;
    }

    private void onResponseSuccess(final String result, final long flag,
                                   final HttpCallback callback) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onHttpResponseSuccess(result, flag);
                }
            });

        }
    }

    private void onResponseFailure(final int error, final long flag, final HttpCallback callback) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onHttpResponseFailure(error, flag);
                }
            });

        }
    }

    private void onResponseTimeout(final long flag, final HttpCallback callback) {
        if (callback != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onHttpResponseTimeout(flag);
                }
            });
        }
    }

    public void cancelRequest(Object tag) {
        //mClient.dispatcher( ).cancelAll( );
        for (Call call : mClient.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag)) {
                LogUtils.v("queuedCalls found");
                call.cancel();
            }
        }
        for (Call call : mClient.dispatcher().runningCalls()) {
            if (call.request().tag().equals(tag)) {
                LogUtils.v("runningCalls found");
                call.cancel();
            }
        }
    }
}
