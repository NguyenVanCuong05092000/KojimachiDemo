package jp.co.kojimachi.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import jp.co.kojimachi.R;
import jp.co.kojimachi.activities.ActMain;
import jp.co.kojimachi.constant.AppConstants;
import jp.co.kojimachi.constant.IntentActionConstant;
import jp.co.kojimachi.constant.NotificationConstants;
import jp.co.kojimachi.data.PrefManager;
import me.leolin.shortcutbadger.ShortcutBadger;

import static jp.co.kojimachi.constant.NotificationConstants.NOTIFICATION_BODY;
import static jp.co.kojimachi.constant.NotificationConstants.NOTIFICATION_DATA_TRANSFER;
import static jp.co.kojimachi.constant.NotificationConstants.NOTIFICATION_TITLE;


/**
 * Created by Atula9286 on 17/06/2020.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "FirebaseMessaging";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        try {
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "onNewToken.Token : " + s);
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "onNewToken.Token_2 : " + refreshedToken);
            // register with server if available
        } catch (Exception e) {
            if (AppConstants.LOG_DEBUG)
                Log.e(TAG, "onNewToken.Failed to complete token refresh", e);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
//            if (TextUtils.isEmpty(ActMain.currentToken)) { // not logged in
//                return;
//            }
//            Log.d("FirebaseMessaging", "Data : " + remoteMessage.getData());
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "MessageIncoming");
            Bundle data = new Bundle();
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                if (AppConstants.LOG_DEBUG)
                    Log.e(TAG, "EntryData : " + entry.getKey() + ", Val : " + entry.getValue() + ", raw : " + entry.toString());
                data.putString(entry.getKey(), entry.getValue());
            }
            String title = data.getString(NOTIFICATION_TITLE);
            String body = data.getString(NOTIFICATION_BODY);
            boolean isProduct = data.containsKey(NotificationConstants.NOTIFICATION_PRODUCT_ID);
            if (AppConstants.LOG_DEBUG)
                Log.e(TAG, "MessageIncoming >>> Title : " + title + ", Body : " + body);
            if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(body)) {
                Context context = getApplicationContext();
                Intent defaultAction = new Intent(context, ActMain.class)
                        .setAction(Intent.ACTION_DEFAULT)
                        .putExtra(NOTIFICATION_DATA_TRANSFER, data);
//                defaultAction.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                NotificationCompat.Builder mBuilder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String NOTIFICATION_CHANNEL_ID = context.getResources().getString(R.string.default_notification_channel_id);
                    //
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, context.getResources().getString(R.string.default_notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT);
                    // Configure the notification channel.
                    notificationChannel.setDescription(context.getResources().getString(R.string.default_notification_channel_description));
                    notificationChannel.enableLights(true);
//                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                    notificationChannel.enableVibration(true);
                    notificationManager.createNotificationChannel(notificationChannel);
                    //
                    mBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
                } else {
                    mBuilder = new NotificationCompat.Builder(context);
                }
                mBuilder.setContentTitle(title == null ? "" : title)
                        .setContentText(body == null ? "" : body)
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(
                                context,
                                0,
                                defaultAction,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        ));

                int currentId = PrefManager.getInstance(context).getInt(PrefManager.NOTIFICATION_COUNT);
                currentId++;
                if (AppConstants.LOG_DEBUG) Log.e(TAG, "notification.currentId:" + currentId);
                PrefManager.getInstance(context).writeInt(PrefManager.NOTIFICATION_COUNT, currentId);
                Notification notification = mBuilder.build();
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(
//                        isProduct? NotificationConstants.ID_VIEW_PRODUCT_INFO
//                                : NotificationConstants.ID_VIEW_NEWS
                        currentId
                        , notification);
                ShortcutBadger.applyCount(context, currentId);
                ShortcutBadger.applyNotification(context, notification, currentId);

                if (!isProduct)
                    sendBroadcast(new Intent().setAction(IntentActionConstant.ACTION_PUSH_NEWS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
