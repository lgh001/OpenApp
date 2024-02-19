package cn.lgh.openapp.test;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.app.NotificationCompat;

import cn.lgh.openapp.R;

public class PhoneCallReceiver {

    private static final String CHANNEL_ID = "voice_call_channel";
    private static final int CALL_NOTIFICATION_ID = 1;

    private Context context;
    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;
    private NotificationManager notificationManager;

    public PhoneCallReceiver(Context context) {
        this.context = context;
        this.telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        this.phoneStateListener = new CustomPhoneStateListener();
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 创建通知渠道（适用于Android 8.0及更高版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Voice Call Channel";
            String channelDescription = "Channel for voice call notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void start(){
        // 创建通知渠道（适用于Android 8.0及更高版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Voice Call Channel";
            String channelDescription = "Channel for voice call notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void startListening() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public void stopListening() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
    }

    private class CustomPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    // 电话响铃状态
                    showCallNotification("有电话呼入", "正在等待接听");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    // 通话状态，用户接听或拨打电话
                    showCallNotification("正在通话中", "通话时间：00:00");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    // 电话空闲状态，没有任何活动
                    dismissCallNotification();
                    break;
            }
        }
    }

    private void showCallNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true);

        notificationManager.notify(CALL_NOTIFICATION_ID, builder.build());
    }

    private void dismissCallNotification() {
        notificationManager.cancel(CALL_NOTIFICATION_ID);
    }
}
