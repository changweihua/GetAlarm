package net.cmono.receivers;

import net.cmono.getalarm.GetAlarmApplication;
import net.cmono.getalarm.R;
import net.cmono.utils.Logger;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService  extends Service
{
    private static final String TAG = "MusicService";
    
    private MediaPlayer player;
    
    private AudioManager mAm;
    
    private MyOnAudioFocusChangeListener mListener;

    
    @Override
    public void onCreate()
    {
        
        player = MediaPlayer.create(this, R.raw.noti);  // 在res目录下新建raw目录，复制一个test.mp3文件到此目录下。
        player.setLooping(false);
        
        mAm = (AudioManager)GetAlarmApplication.getAppContext().getSystemService(Context.AUDIO_SERVICE);
        mListener = new MyOnAudioFocusChangeListener();
    }
    

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    

    @Override
    public void onStart(Intent intent, int startid)
    {
        
        // Request audio focus for playback
        int result = mAm.requestAudioFocus(mListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            // Start playback.
            player.start();
        }
        else
        {
        }
    }
    

    @Override
    public void onDestroy()
    {
        player.stop();
        
        mAm.abandonAudioFocus(mListener);
    }
    

    private class MyOnAudioFocusChangeListener implements
            OnAudioFocusChangeListener
    {
        @Override
        public void onAudioFocusChange(int focusChange)
        {
        	Logger.d(TAG, "focusChange=" + focusChange);  
        }
    }
}