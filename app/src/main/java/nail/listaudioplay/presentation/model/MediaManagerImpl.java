package nail.listaudioplay.presentation.model;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import wseemann.media.FFmpegMediaPlayer;

import static nail.listaudioplay.ui.activity.MainActivity.TAG;

public class MediaManagerImpl implements IMedia {
    private FFmpegMediaPlayer mediaPlayer;
    private final Context context;
    private Timer timerAsync;
    private TimerTask timerTaskAsync;

    public MediaManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        init();
    }


    @Override
    public boolean isPlayning() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        } else
            return false;
    }

    private void init() {
        if (mediaPlayer == null) {
            mediaPlayer = new FFmpegMediaPlayer();
        }
    }

    public void startBackground(final OnMediaListener onMediaListener) {
        final Handler handler = new Handler();
        timerAsync = new Timer();
        timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            onMediaListener.onSetDuration(mediaPlayer.getCurrentPosition(),mediaPlayer.getDuration());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timerAsync.schedule(timerTaskAsync, 0, 500);
    }

    @Override
    public void playPrepare(final OnMediaListener onMediaListener, String url) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new FFmpegMediaPlayer();
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnPreparedListener(new FFmpegMediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final FFmpegMediaPlayer mediaPlayer) {
                        onMediaListener.onComplete();
                        startBackground(onMediaListener);
                        mediaPlayer.start();
                        Log.d(TAG, "MediaPlayer: prepared " + mediaPlayer.getDuration());
                    }
                });
                mediaPlayer.setOnCompletionListener(new FFmpegMediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(FFmpegMediaPlayer mediaPlayer) {
                        Log.d(TAG, "MediaPlayer: completion");
                        onMediaListener.onEnd();
                    }
                });

                mediaPlayer.setOnErrorListener(new FFmpegMediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(FFmpegMediaPlayer mediaPlayer, int what, int extra) {
                        Log.d(TAG, "MediaPlayer: error");
                        onMediaListener.onError(what, extra);
                        return true;
                    }
                });
                mediaPlayer.prepareAsync();
            }
        }
    }

    @Override
    public void playPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    @Override
    public void Play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    public void Pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void Stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
