package jp.co.kojimachi.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.constant.EnumUrl;
import jp.co.kojimachi.entity.ApiResult;

import static jp.co.kojimachi.constant.ApiConstants.PARAM_MESSAGE;
import static jp.co.kojimachi.constant.ApiConstants.PARAM_RESPONSE;
import static jp.co.kojimachi.constant.ApiConstants.PARAM_STATUS;
import static jp.co.kojimachi.constant.ApiConstants.PARAM_TOKEN;
import static jp.co.kojimachi.constant.ApiConstants.PARAM_TOTAL;
import static jp.co.kojimachi.constant.ApiConstants.STATUS_ERROR;


public class APIHelper {

    private static final String TAG = "NETWORK";
    private static APIHelper _instance;
    private LinkedHashMap<Integer, ArrayList<APIConnect>> _hmAPIConnect = null;

    private APIHelper() {
        _hmAPIConnect = new LinkedHashMap<>();
    }

    public static APIHelper getInstance() {
        if (_instance == null) {
            _instance = new APIHelper();
        }
        return _instance;
    }

    /**
     * generate request parameter for request type is x-www-form-urlencoded
     *
     * @param params map of param name and value of param in request api
     * @return
     */
    private static String makeRequestParameters(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        try {
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(entry.getKey());
                result.append("=");
                result.append(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * clear api with same type and uiid
     *
     * @param typeAPI            type of api
     * @param apiUIID            for identified api-connect
     * @param isCancelCurrentAPI true : cancel current API, otherwise do nothing
     */
    private void clearAPI(int typeAPI, long apiUIID, boolean isCancelCurrentAPI) {
        try {
            ArrayList<APIConnect> arlAPI = _hmAPIConnect.get(typeAPI);
            if (arlAPI != null) {
                for (int i = arlAPI.size() - 1; i >= 0; i--) {
                    APIConnect apiConnect = arlAPI.get(i);
                    if (apiConnect.apiUIID == apiUIID) {
                        if (isCancelCurrentAPI) {
                            try {
                                apiConnect.cancel(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } catch (Error e) {
                                e.printStackTrace();
                            }
                        }
                        arlAPI.remove(i);
                        break;
                    }
                }
                _hmAPIConnect.put(typeAPI, arlAPI);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * clear all api with same type
     *
     * @param typeAPI
     */
    public void clearAPI(int typeAPI) {
        try {
            ArrayList<APIConnect> arlAPI = _hmAPIConnect.get(typeAPI);
            if (arlAPI != null) {
                for (APIConnect apiConnect : arlAPI) {
                    try {
                        apiConnect.cancel(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                }
                arlAPI.clear();
                arlAPI = new ArrayList<>();
                _hmAPIConnect.put(typeAPI, arlAPI);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * clear all api
     */
    public void clearAPI() {
        try {
            for (Integer typeAPI : _hmAPIConnect.keySet()) {
                ArrayList<APIConnect> arlAPI = _hmAPIConnect.get(typeAPI);
                if (arlAPI != null) {
                    for (APIConnect apiConnect : arlAPI) {
                        try {
                            apiConnect.cancel(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                    }
                    arlAPI.clear();
                }
            }
            _hmAPIConnect.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Call api with request in format as x-www-form-urlencoded
     *
     * @param enumUrl                 using as index api to collect any api/url api
     *                                //* @param maxRetry                total of retry call api when occur timeout (min is 1 - at least 1 call)
     * @param params                  map of key-value of params
     * @param onStartCallAPIListener  action when start call api
     * @param onFinishCallAPIListener action when receive response
     */
    public void callAPI(EnumUrl enumUrl, HashMap<String, String> params, OnStartCallAPIListener onStartCallAPIListener, OnFinishCallAPIListener onFinishCallAPIListener) {
        try {
            if (AppConstants.LOG_DEBUG)
                Log.e("ApiClient", "requestApi->" + enumUrl + "\n->params:" + (params != null ? params.toString() : "null"));
            ArrayList<APIConnect> arlAPI = _hmAPIConnect.get(enumUrl.getType());
            if (arlAPI == null) {
                arlAPI = new ArrayList<>();
            }
            APIConnect apiConnect = new APIConnect(enumUrl.getType(), System.currentTimeMillis(), onStartCallAPIListener, onFinishCallAPIListener);
            apiConnect.setOnCompleteRequestListener((typeAPI1, apiUIID, apiUrl, requestParameters, response) -> clearAPI(typeAPI1, apiUIID, false));
            arlAPI.add(apiConnect);
            _hmAPIConnect.put(enumUrl.getType(), arlAPI);
            apiConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, enumUrl.getUrl(), makeRequestParameters(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAPI(int retry, EnumUrl enumUrl, HashMap<String, String> params, OnStartCallAPIListener onStartCallAPIListener, OnFinishCallAPIListener onFinishCallAPIListener) {
        try {
            if (AppConstants.LOG_DEBUG)
                Log.e("ApiClient", "requestApi->" + enumUrl + "\n->params:" + (params != null ? params.toString() : "null"));
            ArrayList<APIConnect> arlAPI = _hmAPIConnect.get(enumUrl.getType());
            if (arlAPI == null) {
                arlAPI = new ArrayList<>();
            }
            APIConnect apiConnect = new APIConnect(enumUrl.getType(), System.currentTimeMillis(), onStartCallAPIListener, onFinishCallAPIListener);
            apiConnect.setMaxRetry(retry);
            apiConnect.setOnCompleteRequestListener((typeAPI1, apiUIID, apiUrl, requestParameters, response) -> clearAPI(typeAPI1, apiUIID, false));
            arlAPI.add(apiConnect);
            _hmAPIConnect.put(enumUrl.getType(), arlAPI);
            apiConnect.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, enumUrl.getUrl(), makeRequestParameters(params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * handle base-connect api
     */
    private static class APIConnect extends AsyncTask<String, Integer, String> {
        private final String TAG = "APIConnect";
        private final int TIMEOUT = 10000;
        private int maxRetry = 1; // default is 1 (at least 1 call API)
        private int typeAPI;
        private long apiUIID;

        private OnStartCallAPIListener onStartCallAPIListener;
        private OnFinishCallAPIListener onFinishCallAPIListener;
        private OnCompleteRequestListener onCompleteRequestListener;

        public APIConnect(int typeAPI, long apiUIID, OnStartCallAPIListener onStartCallAPIListener, OnFinishCallAPIListener onFinishCallAPIListener) {
            this.typeAPI = typeAPI;
            this.apiUIID = apiUIID;
            this.onStartCallAPIListener = onStartCallAPIListener;
            this.onFinishCallAPIListener = onFinishCallAPIListener;
        }

        public void setOnCompleteRequestListener(OnCompleteRequestListener onCompleteRequestListener) {
            this.onCompleteRequestListener = onCompleteRequestListener;
        }

        public void setMaxRetry(int maxRetry) {
            this.maxRetry = maxRetry;
        }

        private void noticeRemoveAPI(String url, String urlParameters, String response) {
            Log.d(TAG, "noticeRemoveAPI: " + response);
            if (onCompleteRequestListener != null) {
                onCompleteRequestListener.onComplete(typeAPI, apiUIID, url, urlParameters, response);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (onStartCallAPIListener != null) {
                onStartCallAPIListener.onStart();
            }
        }

        @Override
        protected String doInBackground(String... args) {
            if (args != null && args.length >= 2) {
                String urlServer = args[0];
                Log.d(TAG, "CallAPI >>> urlServer : " + urlServer);
                if (!TextUtils.isEmpty(urlServer)) {
                    String urlParameters = args[1];

                    int retryCount = 0;
                    String response = null;
                    while (retryCount < maxRetry) {
                        try {
                            URL url = new URL(urlServer);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("charset", "UTF-8");

                            conn.setConnectTimeout(TIMEOUT);
                            conn.setReadTimeout(TIMEOUT);

                            conn.setDoOutput(true);
                            conn.setDoInput(true);
                            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                            int postDataLength = postData.length;
                            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                            conn.setUseCaches(false);
                            try {
                                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                                os.write(postData);
                                os.flush();
                                os.close();
                            } catch (SocketTimeoutException e) {
                                retryCount++;
                                e.printStackTrace();
                                if (retryCount >= maxRetry) {
                                    break;
                                }
                                continue;
                            }
                            // Log.d(TAG, "StatusResponse : " + conn.getResponseCode());
                            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                                StringBuilder sbResponse = new StringBuilder();
                                String responseLine;
                                while ((responseLine = br.readLine()) != null) {
                                    sbResponse.append(responseLine.trim());
                                }
                                response = sbResponse.toString();
                            }
                            conn.disconnect();
                            noticeRemoveAPI(urlServer, urlParameters, response);
                            break;
                        } catch (Exception e) {
                            retryCount++;
                            e.printStackTrace();
                            if (retryCount >= maxRetry) {
                                break;
                            }
                        }
                    }
                    return response;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Log.d(TAG, "onPostExecute:onSuccess " + result);
                try {
                    JSONObject json = new JSONObject(result);
                    if (onFinishCallAPIListener != null) {
                        onFinishCallAPIListener.onFinished(new ApiResult(
                                json.optInt(PARAM_STATUS),
                                json.optString(PARAM_MESSAGE),
                                json.optString(PARAM_TOKEN),
                                json.opt(PARAM_RESPONSE),
                                json.optInt(PARAM_TOTAL)));
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "onPostExecute:onFailure " + result);
            if (onFinishCallAPIListener != null) {
                onFinishCallAPIListener.onFinished(new ApiResult(STATUS_ERROR));
            }
        }
    }

    /**
     * for purpose do anything when start call api (show loading, etc...)
     */
    public interface OnStartCallAPIListener {
        void onStart();
    }

    /**
     * for purpose receive response api and handle it
     */
    public interface OnFinishCallAPIListener {
        void onFinished(ApiResult response);
    }

    /**
     * for purpose handle api-connect when complete request
     */
    public interface OnCompleteRequestListener {
        void onComplete(int typeAPI, long apiUIID, String apiUrl, String requestParameters, String response);
    }
}
