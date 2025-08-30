package com.java.pattern.program.app;

import android.app.Application;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Subscribe to a topic for Firebase Cloud Messaging
        FirebaseMessaging.getInstance().subscribeToTopic("guideappnotification");

        // TODO: You can now use the new method to get the FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String refreshedToken = task.getResult();
                        // TODO: You can use the 'refreshedToken' for further actions
                    }
                });
    }
}