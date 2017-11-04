package org.nashorn.prototype;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("Service", "onMessageReceived");

        String from = remoteMessage.getFrom();
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.i("Service", "from:"+from+",title:"+notification.getTitle());
        Log.i("Service", "from:"+from+",body:"+notification.getBody());
        Map<String,String> data = remoteMessage.getData();
        String data1 = data.get("data1");
        Log.i("Service", "from:"+from+",data1:"+data1);
        String data2 = data.get("data2");
        Log.i("Service", "from:"+from+",data2:"+data2);
    }
}
