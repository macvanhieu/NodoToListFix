package com.dell.noteapp.reminder;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.dell.noteapp.R;
import com.dell.noteapp.database.AlarmReminderConstants;
import com.dell.noteapp.entity.Note;
import com.dell.noteapp.view.MainActivity;

import java.util.List;

// class Khởi tạo service bắn notifi
public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;
    private List<Note> noteList;
    private String description ="";

    Cursor cursor;
    //This is a deep link intent, and needs the task stack
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri uri = intent.getData();

        //Display a notification to view the task details
       // Intent action = new Intent(this, NoteActivity.class);
        // Nghiệp vụ xử lý khi tab vào notifi sẽ truyền đến màn MainActicity
        Intent action = new Intent(this, MainActivity.class);
        action.setData(uri);
        // truyền theo dữ liệu
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        // Truy vấn vào DB lấy ra dữ liệu
        if(uri != null){
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        // Lấy ra nội dung hiển thị trên thanh notifi
        String description = "";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                description = AlarmReminderConstants.getColumnString(cursor, AlarmReminderConstants.AlarmReminderEntry.KEY_TITLE);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


//        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//        Notification note = new NotificationCompat.Builder(getApplication(), NOTIFICATION_CHANNEL_ID)
//                .setContentTitle(getString(R.string.reminder_title))
//                .setContentText(description)
//                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
//                .setContentIntent(operation)
//                .setAutoCancel(true)
//                .build();
//
//        manager.notify(NOTIFICATION_ID, note);
        // Khởi tạo thông báo
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        // Cấp quyền cho máy có api >= 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",
                            NotificationManager.IMPORTANCE_MAX);

            // Configure the notification channel.
            //Set các thuộc tính cho dialog, màu sắc, nội dung, kích cỡ..
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        // set các giá trị hiển thị trên thanh thông báo
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
              //  .setTicker("Hearty365")
                //     .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(getString(R.string.app_name))
               // .setContentText(operation)
                .setContentText(description)
                .setContentIntent(operation)
                .setContentInfo("Info");

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

}