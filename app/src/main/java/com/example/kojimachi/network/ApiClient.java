package jp.co.kojimachi.network;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.co.kojimachi.constant.ApiConstants;
import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.entity.ApiResult;
import jp.co.kojimachi.listener.CallbackApi;
import jp.co.kojimachi.utils.AppUtils;

public class ApiClient implements ApiConstants {

    public static void requestApi(Context context, String url, boolean isPost, RequestParams params, CallbackApi callback) {
        try {
            if (AppConstants.LOG_DEBUG)
                Log.e("ApiClient", "requestApi->" + url + "\n->params:" + (params != null ? params.toString() : "null"));
            if (!AppUtils.isNetworkAvailable(context)) {
                callback.onFinished(new ApiResult(STATUS_ERROR_INTERNET));
                return;
            }
            AsyncHttpClient client = new AsyncHttpClient();
            TextHttpResponseHandler responseHandler = new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    try {
                        if (AppConstants.LOG_DEBUG)
                            Log.e("ApiClient", "onFailure->statusCode:" + statusCode
                                    + "\n-->response:" + responseString);
                        if (callback != null)
                            callback.onFinished(new ApiResult(STATUS_ERROR));
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                        if (AppConstants.LOG_DEBUG)
                            Log.e("ApiClient", "onSuccess->statusCode" + statusCode
                                    + "\n-->urlRequest:" + url
                                    + "\n-->response:" + responseString);
                        JSONObject json = new JSONObject(responseString);
                        if (callback != null)
                            callback.onFinished(new ApiResult(
                                    json.optInt(PARAM_STATUS),
                                    json.optString(PARAM_MESSAGE),
                                    json.optString(PARAM_TOKEN),
                                    json.opt(PARAM_RESPONSE),
                                    json.optInt(PARAM_TOTAL)));
                        return;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (callback != null)
                        callback.onFinished(new ApiResult(STATUS_ERROR));
                }
            };

            if (isPost)
                client.post(url, params, responseHandler);
            else
                client.get(url, params, responseHandler);
        } catch (Throwable e) {
            e.printStackTrace();
            callback.onFinished(new ApiResult(STATUS_ERROR));
        }
    }
}
