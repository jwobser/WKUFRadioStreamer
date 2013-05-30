package edu.kettering.wkufradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.NotificationManager;
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
import android.widget.ToggleButton;


public class Streamer extends Activity {

	/* ***** Variables ***** */
	private static boolean isPlaying; // Is media player playing?
	private static boolean isMuted; // Is media player muted?
	private static boolean isNotifying; //is Notification Active?
			
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
	
	/* ***** Intents ***** */
	Intent PausePlayIntent = null;
	Intent StopIntent = null;
	private static final String ACTION_PLAY = "edu.kettering.action.PLAY";
	private static final String ACTION_STOP = "edu.kettering.action.STOP";
	
	
	
	

	
	/* ***** Listeners ***** */
	
	//Toggle Pause
	private OnClickListener playClickListener = new OnClickListener(){
		@Override
		public void onClick(View view){
			Log.d("AppStatus","Starting Service");
			startService(PausePlayIntent);
			
			// Need to send PausePlay intent to Service!!
			
			/*if(isPlaying == true){
				mp.pause();
				btnTogglePlay.setImageResource(R.drawable.ic_play_btn);
				isPlaying = false;
				}
			else{
				mp.start();
				btnTogglePlay.setImageResource(R.drawable.ic_pause_btn);
				isPlaying = true;
			}
			*/
		}
	};
	
	// Toggle Mute
	private OnClickListener muteClickListener = new OnClickListener(){
		@Override
		public void onClick(View view) {
		
		// Need to Send Mute to Audio Manager
		
			/*if(isMuted == false){
			// Log.d("AppStatus","isMuted Returned false; Muting");
			mp.setVolume(0, 0);
			seekVolume.setProgress(0);
			isMuted = true;
								
			
			}else{
				isMuted = false;
			}
		NotifyPlaying();*/
		}
	};

	
	// For toggling play state
	private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
			
			
			// Should send signal to service
			
			
			
			
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
		
		 PausePlayIntent = new Intent(this, edu.kettering.wkufService.WKUFStreamerService.class);
		 StopIntent = new Intent(this, edu.kettering.wkufService.WKUFStreamerService.class);
		 
		 PausePlayIntent.setAction(ACTION_PLAY);
		 StopIntent.setAction(ACTION_STOP);
		 
		 startService(PausePlayIntent);
		 
		// firstPlay();
		// Log.d("AppStatus","Starting Stream");
				
		// NotifyPlaying();
		// streamerImplementation.StartPlaying();
		 
		 /* Create Audio Manager */
		 // myAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		 // BroadcastReceiver myRemote =  new RemoteControlReceiver();
		 // myAudioManager.registerMediaButtonEventReceiver(myRemote);
		
	}

	@Override
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
		Log.d("AppStatus", "Attempting to create Notification");
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
	
		


