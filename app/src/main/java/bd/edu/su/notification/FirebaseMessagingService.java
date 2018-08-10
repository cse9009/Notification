package bd.edu.su.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
//
//        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String messageTitle = remoteMessage.getNotification().getTitle();
            String messageBody = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();
            String dataMessage = remoteMessage.getData().get("message");
            String dataFrom  = remoteMessage.getData().get("from_id");

            Intent intent = new Intent(click_action);
            intent.putExtra("message",dataMessage);
            intent.putExtra("from_id",dataFrom);


            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);



            Notification.Builder mBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setContentIntent(pendingIntent)
                    ;

            int notificationId = (int) System.currentTimeMillis();

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(notificationId,mBuilder.build());
            //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
