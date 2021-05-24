package com.t3h.basemvvm.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.AudioBook;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.ui.main.MainActivity;
import com.t3h.basemvvm.ui.main.MainActivityJava;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class AudioPlayerService extends Service implements EXOPlayManager.ExoCallBack, NotificationReceiver.OnClickButtonNotification {

    private static final String CHANNEL_ID = "AUDIO_BOOK_CHANNAL";
//    private List<AudioBook> listPlaying = new ArrayList<>();
//    private List<AudioBook> listSavePosition;
//    private int currentPositionAudio = -1;
    private Book book;
    private EXOPlayManager managerPlayer;
    private AudioBinder myBinder = new AudioBinder();

    public int indexQuality = 0;
    private Notification notification;
    private RemoteViews notificationLayout;

    private boolean isReplayOneAudio = false;


    @Override
    public void onCreate() {
        super.onCreate();
        managerPlayer = new EXOPlayManager(this, this);

        notificationLayout = new RemoteViews(getPackageName(), R.layout.layout_notification);
        createNotificationChannel();
        NotificationReceiver.onClickButtonNotification = this;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "My Background Service Chanel";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    name, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private PendingIntent onButtonNotificationClick(@IdRes int id) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("EXTRA_BUTTON_CLICKED", id);
        return PendingIntent.getBroadcast(this, id, intent, 0);
    }

    private PendingIntent onSeekNotification(@IdRes int id, int progress) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("EXTRA_BUTTON_CLICKED", id);
        intent.putExtra("progress_notification", progress);
        return PendingIntent.getBroadcast(this, id, intent, 0);
    }

    public void showNotification() {
        if (book == null) {
            return;
        }
        Book book = this.book;
        String title = book.getTitle();
        String owner = book.getAuthor();
        String urlImage = book.getCoverImage();

        notificationLayout.setTextViewText(R.id.tv_title_video, title);
        notificationLayout.setTextViewText(R.id.tv_owner_video, owner);

        notificationLayout.setOnClickPendingIntent(R.id.btn_back_notification,
                onButtonNotificationClick(R.id.btn_back_notification));
        notificationLayout.setOnClickPendingIntent(R.id.btn_play_notification,
                onButtonNotificationClick(R.id.btn_play_notification));
        notificationLayout.setOnClickPendingIntent(R.id.btn_next_notification,
                onButtonNotificationClick(R.id.btn_next_notification));
        notificationLayout.setOnClickPendingIntent(R.id.btn_close_notification,
                onButtonNotificationClick(R.id.btn_close_notification));
        notificationLayout.setOnClickPendingIntent(R.id.imv_video_notification,
                onButtonNotificationClick(R.id.imv_video_notification));


        if (getManagerPlayer().isPlaying) {
            notificationLayout.setImageViewResource(R.id.btn_play_notification, R.drawable.exo_controls_pause);
        } else {
            notificationLayout.setImageViewResource(R.id.btn_play_notification, R.drawable.exo_controls_play);
        }

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo)
                .setCustomBigContentView(notificationLayout)
                .setDefaults(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();

        NotificationTarget notificationTarget = new NotificationTarget(
                getApplicationContext(),
                R.id.imv_video_notification, notificationLayout,
                notification,
                10);

        Glide.with(this).asBitmap()
                .load(urlImage)
                .error(R.drawable.baseline_home_white_24dp)
                .into(notificationTarget);

        startForeground(10, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

//    public int getCurrentPositionAudio() {
//        return currentPositionAudio;
//    }

//    public void setCurrentPositionAudio(int currentPositionAudio) {
//        this.currentPositionAudio = currentPositionAudio;
//    }

//    public boolean isReplayOneVideo() {
//        return isReplayOneVideo;
//    }
//
//    public void setReplayOneVideo(boolean isReplayOneVideo) {
//        this.isReplayOneVideo = isReplayOneVideo;
//    }

    public Book getListPlaying() {
        return book;
    }

    public void setListPlaying(Book listPlaying) {
        this.book = listPlaying;
    }

    public EXOPlayManager getManagerPlayer() {
        return managerPlayer;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        try {
            if (!hasFocus) {
                @SuppressLint("WrongConstant") Object service = getSystemService("statusbar");
                Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                Method collapse = statusbarManager.getMethod(Build.VERSION.SDK_INT > 16 ? "collapsePanels" : "collapse");
                collapse.setAccessible(true);
                collapse.invoke(service);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onPlayerError() {

    }

    @Override
    public void playNextWhenEnded() {
        playNext();
    }

    public void stopPlayer() {
        managerPlayer.stopEXO();
        onCloseButton();
    }

    public void upPriorityVideoInPlaying(int position) {
//        if (position >= 1) {
//            listPlaying.add(position - 1, listPlaying.get(position));
//            listPlaying.remove(position + 1);
//        }
//
//        if (position == currentPositionAudio & position > 0) {
//            currentPositionAudio--;
//        }
//
//        if (position == currentPositionAudio + 1) {
//            currentPositionAudio++;
//        }
    }

    public void changeQuality(int i) {
        if (i == indexQuality) {
            return;
        } else {
            if (book!=null) {
                long currentPosition = managerPlayer.getCurrentPosition();
                managerPlayer.pauseEXO();
                managerPlayer.initEXOMusic(book.getMp3());
                managerPlayer.seekTo(currentPosition);
                indexQuality = i;
            }
        }
    }


    public class AudioBinder extends Binder {
        public AudioPlayerService getService() {
            return AudioPlayerService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        managerPlayer.releaseEXO();
    }

    public void playNext() {
        getManagerPlayer().fastForwardEXO();
    }

    public void playPrevios() {
        getManagerPlayer().rewindEXO();

    }

    public void playEXOVideo(Book list) {
//        if ((listPlaying != list)) {
//            listSavePosition = list;
//            listPlaying.clear();
//            listPlaying.addAll(list);
//        }
        this.book=list;
//        currentPositionAudio = position;
        playExo();
    }

    public void playExo() {
        String url = book.getMp3();
        getManagerPlayer().initEXOMusic(url);
        //update View by broadcast
//        Intent intent = new Intent();
//        intent.setAction("action.UpdateUI.MainActivityJava");
//        intent.putExtra("youtubeVideo",(Serializable) book);
//        sendBroadcast(intent);
        putIntent();
        showNotification();
    }


// Callback Notification

    @Override
    public void onBackButton() {
        playPrevios();
    }

    @Override
    public void onPlayButton() {
        if (getManagerPlayer().isPlaying) {
            getManagerPlayer().pauseEXO();
        } else {
            getManagerPlayer().playEXO();
        }
        putIntent();
        showNotification();
    }

    @Override
    public void onNextButton() {
        playNext();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onCloseButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(2);
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(2);
            //  System.exit(0);
            managerPlayer.pauseEXO();
//            Intent intent = new Intent();
//            intent.setAction("action.UpdateUI.MainActivityJava");
////            intent.putExtra("youtubeVideo", listPlaying.get(currentPositionAudio));
//            sendBroadcast(intent);
            putIntent();
            stopSelf();
        }
    }

    private void putIntent(){
        Intent intent = new Intent();
        intent.setAction("action.UpdateUI.MainActivityJava");
        intent.putExtra("book", book);
        sendBroadcast(intent);
    }


    @Override
    public void onOpenButton() {
        Intent intent = new Intent(this, MainActivityJava.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        onWindowFocusChanged(false);
    }
}
