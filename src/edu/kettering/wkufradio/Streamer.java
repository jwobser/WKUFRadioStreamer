package edu.kettering.wkufradio;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
// import android.widget.Toast;
import android.widget.ToggleButton;
import java.lang.Math;
import edu.kettering.wkufradio.RemoteControlReceiver;


public class Streamer extends Activity {

	/* ***** Variables ***** */
	private static boolean isPlaying; // Is media player playing?
	private static boolean isMuted; // Is media player muted?
	private static boolean isNotifying; //is Notification Active?
	private float fvolume; //volume as a float 0-1
	private int ivolume; //volume as an int 0-100.
	private static final String StreamURL = "http://audio.moses.bz:80/wkuf-lp";
	
	/* ***** View Items ***** */
	private ToggleButton btnStreamToggle;
	private ImageButton btnToggleMute;
	private SeekBar seekVolume;
	private ImageButton btnTogglePlay;
	
		
	/* ***** Notification ***** */
	private NotificationManager mNotificationManager;
	
	/* ***** Images ***** */
	Bitmap large_notification_icon;
	
	/* ***** AudioManager ***** */
	AudioManager myAudioManager = null;
	
	/* ***** Media Player ***** */
	MediaPlayer mp = null;
	
	
	
	
	
	/*
	 public static void firstPlay(){
		 mp = new MediaPlayer();
		try	{
		mp.setDataSource(StreamURL);
			} catch (IllegalArgumentException e){
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Log.d("AppStatus", "Stream Source has been set to" + StreamURL);
		
		Log.d("AppStatus", "Async Prepare");
		try	{
			mp.prepareAsync();
				} catch (IllegalArgumentException e){
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}
	*/

	
	/* ***** Listeners ***** */
	
	//Toggle Pause
	private OnClickListener playClickListener = new OnClickListener(){
		@Override
		public void onClick(View view){
			if(isPlaying == true){
				mp.pause();
				btnTogglePlay.setImageResource(R.drawable.ic_play_btn);
				isPlaying = false;
				}
			else{
				mp.start();
				btnTogglePlay.setImageResource(R.drawable.ic_pause_btn);
				isPlaying = true;
			}
		}
	};
	
	// Toggle Mute
	private OnClickListener muteClickListener = new OnClickListener(){
		@Override
		public void onClick(View view) {
			if(isMuted == false){
			// Log.d("AppStatus","isMuted Returned false; Muting");
			mp.setVolume(0, 0);
			fvolume = seekVolume.getProgress()/100;
			ivolume = seekVolume.getProgress();
			seekVolume.setProgress(0);
			isMuted = true;
			
			
			/* ***** Toast for Testing ***** */
			/*
			Context context = getApplicationContext();
			CharSequence text = String.valueOf(ivolume);
			int duration = Toast.LENGTH_LONG;
			Toast.makeText(context, text, duration).show();
			*/
			/* *****                    ***** */
			
			
			}else{
				// Log.d("AppStatus","isMuted Returned True; Restoring Volume");
				mp.setVolume(fvolume, fvolume);
				// ivolume = (int)fvolume*100;
				seekVolume.setProgress(ivolume);
				isMuted = false;
			}
		NotifyPlaying();
		}
	};

	
	// For toggling play state
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
			
			
			// Should send signal to service
			
			
			/*
			if(isChecked == true){
				isPlaying = true;
				try	{
					mp.start();
						} catch (IllegalArgumentException e){
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
				}
			else{
				isPlaying = false;
			}
			*/

/* Use to make sure that the mediaPlayer Exists before trying to operate on it?
			if(boolIsPlaying == true){
				streamerImplementation.StartPlaying();
				}
			if(boolIsPlaying == true && boolIsPlaying != false){
				//Do Something
			}
			else{
				//Do Something
			}
			
*/			
			
			// If service returns playing state, toggle notification!!
			NotifyPlaying();
			
		
		}
	};
		
	// For changing volume
	private OnSeekBarChangeListener volumeChangeListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			
			// Need to use AudioManager 
			
			/*
			float calcvolume = (float)progress; // (float)(Math.log10((double)progress)/2);
			fvolume = calcvolume/100;
			mp.setVolume(fvolume, fvolume);
			isMuted = false;
			*/
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_streamer);

		 btnStreamToggle = (ToggleButton)findViewById(R.id.btnToggleStream);
		 btnStreamToggle.setOnCheckedChangeListener(mOnCheckedChangeListener);
		 btnToggleMute = (ImageButton)findViewById(R.id.btnToggleMute);
		 btnToggleMute.setOnClickListener(muteClickListener);
		 seekVolume = (SeekBar)findViewById(R.id.seekVolume);
		 seekVolume.setOnSeekBarChangeListener(volumeChangeListener);
		 btnTogglePlay = (ImageButton)findViewById(R.id.btnTogglePlay);
		 btnTogglePlay.setOnClickListener(playClickListener);
		
		// firstPlay();
		// Log.d("AppStatus","Starting Stream");
				
		// NotifyPlaying();
		// streamerImplementation.StartPlaying();
		 
		 /* Create Audio Manager */
		 AudioManager am = mContext.getSystemService(Context.AUDIO_SERVICE);
		 // myAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		 // BroadcastReceiver myRemote =  new RemoteControlReceiver();
		 myAudioManager.registerMediaButtonEventReceiver(RemoteControlReceiver);
		 // myAudioManager.registerMediaButtonEventReceiver(new ComponentName(getPackageName(),RemoteControlReciver.getName()));
		
	}

	public void onNewIntent(Intent intent){
		Log.d("AppStatus", "New Intent Recieved");
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.streamer, menu);
		return true;
	}


	public void NotifyPlaying(){
		Log.d("AppStatus", "Attempting to creat Notification");
		/* ***** Notification ***** */
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		large_notification_icon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_large);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		
		// Create Notification
		if(isPlaying == true){
			
			if(isMuted == true){
				Log.d("AppStatus", "Notify (MUTED)");
				mBuilder
				.setSmallIcon(R.drawable.icon_small)
				.setContentTitle("Radio Playing (MUTED)")
				.setContentText("WKUF 94.3 FM-LP Stream Now Playing")
				.setLargeIcon(large_notification_icon)
				.setOngoing(true);
			}
			else{
				Log.d("AppStatus", "Notify (UNMUTED)");
				mBuilder
				.setSmallIcon(R.drawable.icon_small)
				.setContentTitle("Radio Playing")
				.setContentText("WKUF 94.3 FM-LP Stream Now Playing")
				.setLargeIcon(large_notification_icon)
				.setOngoing(true);	
			}
						
			
			Intent resultIntent = new Intent(this, Streamer.class);
			resultIntent.addFlags(67108864);
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			stackBuilder.addParentStack(Streamer.class);
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			
			mNotificationManager.notify(123, mBuilder.build());
			isNotifying = true;
			
		}
		else{
			mNotificationManager.cancel(123);
			isNotifying = false;
		}
	}
	
	public void ToggleMute(){
		
	}
	
}
		


