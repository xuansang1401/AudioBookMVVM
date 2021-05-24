package com.t3h.basemvvm.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.AudioBook;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.databinding.ActivityMainBinding;
import com.t3h.basemvvm.service.AudioPlayerService;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivityJava extends AppCompatActivity implements View.OnTouchListener {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private UpdateUIReceiver myReceiverUpdateUI;
    private int heightDevice;
    private AudioPlayerService mService;
    private boolean isBinded = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            AudioPlayerService.AudioBinder binder = (AudioPlayerService.AudioBinder) service;
            mService = binder.getService();
            isBinded = true;
            handlServiceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBinded = false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        heightDevice = displayMetrics.heightPixels;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host) ;
        NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.getNavController());
        setView();
        setClick();
        updateSeekBar();

        myReceiverUpdateUI = new UpdateUIReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.UpdateUI.MainActivityJava");
        registerReceiver(myReceiverUpdateUI, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectService();
    }

    private void connectService() {
        Intent playIntent = new Intent(this, AudioPlayerService.class);
        bindService(playIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public class UpdateUIReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("action.UpdateUI.MainActivityJava")) {
                Book book = (Book) intent.getExtras().getSerializable("book");
                if (book != null) {
                    updateUI(book);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private void updateUI(Book book) {
        binding.tvNameSongMainActivity.setText(book.getTitle());
        binding.tvItemTitle.setText(book.getTitle());
        binding.tvItemTitle1.setText(book.getTitle());
        binding.tvOwnerMainActivity.setText(book.getAuthor());
        binding.tvItemChane.setText(book.getAuthor());
        Glide.with(this).load(book.getImage()).into(binding.imvImageVideo);
        Glide.with(this).load(book.getImage()).into(binding.ivMusic);

        Log.e("sang", "isPlaying: "+ mService.getManagerPlayer().isPlaying);
        if (isBinded) {
            if (mService.getManagerPlayer().isPlaying) {
                binding.btnPlay.setImageResource(R.drawable.baseline_pause_circle_filled_white_36dp);
                binding.btnPlayPause.setImageResource(R.drawable.exo_controls_pause);


            } else {
                binding.btnPlay.setImageResource(R.drawable.baseline_play_circle_filled_white_36dp);
                binding.btnPlayPause.setImageResource(R.drawable.exo_controls_play);

            }
        }
    }
    private void setView() {
        binding.layoutContentPlayingVideo.animate().translationY(heightDevice);
        binding.tvItemTitle.setSelected(true);
        binding.tvItemChane.setSelected(true);
    }
    private void updateSeekBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        if (isBinded && mService != null && mService.getManagerPlayer().isPlaying) {
                            bundle.putLong("currentPositionTime", mService.getManagerPlayer().getCurrentPosition());
                            bundle.putLong("maxTime", mService.getManagerPlayer().getMilisOfVideo());
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            long currentPositionTime = msg.getData().getLong("currentPositionTime");
            long maxTime = msg.getData().getLong("maxTime");

            if (isBinded && mService != null) {
                if (mService.getManagerPlayer().isPlaying) {
                    binding.seeBar.setMax((int) maxTime);
                    binding.seeBar.setProgress((int) currentPositionTime);
                    binding.tvTimeCurrent.setText(viewModel.milisToString(currentPositionTime));
                    binding.tvTotalTime.setText(viewModel.milisToString(maxTime));
                }
            }
        }
    };
    private void setClick() {

        binding.llSmallPlayerMainActivity.setOnClickListener(view -> {
            binding.layoutContentPlayingVideo.setVisibility(View.VISIBLE);
            binding.layoutContentPlayingVideo.animate().translationY(0).setDuration(400);
        });

        binding.layoutContentPlayingVideo.setOnTouchListener(this);

        binding.btnDown.setOnClickListener(view -> {
            binding.layoutContentPlayingVideo.animate().translationY(heightDevice).setDuration(400);
        });

        binding.seeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mService.getManagerPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.btnCancelLlSmall.setOnClickListener(view -> {
            mService.getManagerPlayer().stopEXO();
            binding.llSmallPlayerMainActivity.setVisibility(View.GONE);
        });
    }

    @SuppressLint("NewApi")
    private void handlServiceConnected() {

//        if (mService.getManagerPlayer().isPlaying) {
//            binding.btnPlay.setImageResource(R.drawable.baseline_pause_circle_filled_white_36dp);
//            binding.btnPlayPause.setImageResource(R.drawable.exo_controls_pause);
//
//
//        } else {
//            binding.btnPlay.setImageResource(R.drawable.baseline_play_circle_filled_white_36dp);
//            binding.btnPlayPause.setImageResource(R.drawable.exo_controls_play);
//
//        }
        binding.btnPlay.setOnClickListener(view -> {
            if (mService.getManagerPlayer().isPlaying) {
                mService.getManagerPlayer().pauseEXO();
                binding.btnPlay.setImageResource(R.drawable.baseline_play_circle_filled_white_36dp);
                binding.btnPlayPause.setImageResource(R.drawable.exo_controls_play);
            } else {

                mService.getManagerPlayer().playEXO();
                binding.btnPlay.setImageResource(R.drawable.baseline_pause_circle_filled_white_36dp);
                binding.btnPlayPause.setImageResource(R.drawable.exo_controls_pause);

            }
            mService.showNotification();
        });
        binding.btnPlayPause.setOnClickListener(view -> {
            if (mService.getManagerPlayer().isPlaying) {
                mService.getManagerPlayer().pauseEXO();
                binding.btnPlay.setImageResource(R.drawable.baseline_play_circle_filled_white_36dp);
                binding.btnPlayPause.setImageResource(R.drawable.exo_controls_play);
            } else {
                mService.getManagerPlayer().playEXO();
                binding.btnPlay.setImageResource(R.drawable.baseline_pause_circle_filled_white_36dp);
                binding.btnPlayPause.setImageResource(R.drawable.exo_controls_pause);

            }
            mService.showNotification();
        });
        binding.btnFastforward.setOnClickListener(view->{
            mService.playNext();
        });
        binding.btnRewind.setOnClickListener(view->{
            mService.playPrevios();
        });
        binding.btnCancelLlSmall.setOnClickListener(view -> {
            mService.stopPlayer();
            binding.llSmallPlayerMainActivity.setVisibility(View.GONE);
        });
    }

    float dY;
    float dYLast;
    float dYLastRCV;
    float dYRcv;
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                dYLast = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                if (event.getRawY() + dY < 0) {
                    break;
                }

                view.animate()
//                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;

            case MotionEvent.ACTION_UP:
                if (event.getRawY() - dYLast > (heightDevice / 4)) {
                    view.animate()
//                        .x(event.getRawX() + dX)
                            .y(heightDevice)
                            .setDuration(400)
                            .start();
                    return true;
                } else {
                    view.animate()
//                        .x(event.getRawX() + dX)
                            .y(0)
                            .setDuration(200)
                            .start();
                    return true;
                }

            default:
                return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {

        if (isBinded) {
            // Unbind Service
            this.unbindService(serviceConnection);
            isBinded = false;
        }
        unregisterReceiver(myReceiverUpdateUI);
        super.onDestroy();
    }
    public AudioPlayerService getmService() {
        if (isBinded) {
            return mService;
        } else {
            return null;
        }
    }

    public void playEXOVideo(Book book) {
        Log.e("sang","book: "+ book.getMp3());
        binding.llSmallPlayerMainActivity.setVisibility(View.VISIBLE);
        binding.layoutContentPlayingVideo.animate().translationY(0).setDuration(400);
        if (isBinded && mService != null) {
            mService.playEXOVideo( book);

            viewModel.addHistoryBook(book);
        }
    }

}
