package com.project.proiectsamt

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")

//create the channel id, channel name:

const val channelId = "notification_channel"
const val channelName = "com.project.proiectsamt"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {



    //primire notificare:
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.getNotification() != null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        }
    }

    //atasarea notificarii la custom layout

     fun getRemoteView(title: String): RemoteViews {
        val remoteView = RemoteViews("com.project.proiectsamt", R.layout.notification)

         remoteView.setTextViewText(R.id.title,title)
         remoteView.setImageViewResource(R.id.app_logo,R.drawable.pisica)

         return remoteView

    }
    // generarea notificarii  //notification.xml
    fun generateNotification(title: String, body: String) {

        val intent = Intent(this, MainActivity::class.java) // intent face ca utilizatorul sa fie redirectionat catre aplicatie
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // pune activitatea/notificarea prima in sirul de notificari

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE  //folosim pendingIntent pentru o actiune care urmeaza sa se intample; flagul face ca notificarea sa dispara odata ce este accesata
        )

        //creeare notificare
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.pisica)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title))

        //creating the notification manager:
         val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

    }
}