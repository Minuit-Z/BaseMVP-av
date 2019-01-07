package ziye.utils.app;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import ziye.ziye_mvp_master.R;


/**
 * Created by Administrator on 2017/5/15.
 * 通知栏工具类
 */

public class NotificationUtils {

    public static void sendNotification(String content, Context context, String extra,Class claz) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("新消息");// 设置通知栏标题
        mBuilder.setContentText(content);
        Intent intent = new Intent(context, claz);
        intent.putExtra("bean", extra);
        notificationSet(mBuilder, 0, context, intent);

    }

    public static void notificationSet(NotificationCompat.Builder mBuilder, int id, Context context, Intent mIntent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);// 设置通知小ICON（5.0必须采用白色透明图片）
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX); // 设置该通知优先级
        mBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);//闪灯
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();//API 16
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(id, notification);
    }
}
