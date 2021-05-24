package com.t3h.basemvvm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.t3h.basemvvm.R;

public class NotificationReceiver extends BroadcastReceiver {
    public static OnClickButtonNotification onClickButtonNotification;
    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("EXTRA_BUTTON_CLICKED", -1);
        switch (id) {
            case R.id.btn_back_notification:
                onClickButtonNotification.onBackButton();
                break;
            case R.id.btn_play_notification:
                onClickButtonNotification.onPlayButton();
                break;
            case R.id.btn_next_notification:
                onClickButtonNotification.onNextButton();
                break;
            case R.id.btn_close_notification:
                onClickButtonNotification.onCloseButton();
                break;
            case R.id.imv_video_notification:
                onClickButtonNotification.onOpenButton();
                break;
//            case R.id.seeBar_notification:
//                int progress = intent.getIntExtra("progress_notification", 0);
//                onClickButtonNotification.onSeek(progress);
//                break;
        }
}
    public interface OnClickButtonNotification{
         void onBackButton();
         void onPlayButton();
         void onNextButton();
         void onCloseButton();
         void onOpenButton();
    }
}
