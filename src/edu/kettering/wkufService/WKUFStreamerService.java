package edu.kettering.wkufService;

import java.io.IOException;

import edu.kettering.wkufradio.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class WKUFStreamerService extends Service implements MediaPlayer.OnPreparedListener {
	private static final String ACTION_PLAY = "edu.kettering.action.PLAY";
	
	
	
	/* ***** Variables ***** */
	private static boolean isPlaying; // Is media player playing?
	private static boolean isMuted; // Is media player muted?
	private static boolean isNotifying; //is Notification Active?
	private float fvolume; //volume as a float 0-1
	private int ivolume; //volume as an int 0-100.
	private static final String StreamURL = "http://audio.moses.bz:80/wkuf-lp";
	
	
	/* ***** Media Player ***** */
	MediaPlayer mp = null;
	
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		
	}
	
	
	public int playPause(Intent intent, int flags, int startId){
		
		if(isPlaying == true){
			mp.pause();
			isPlaying = false;
			return 2; // indicate paused
			}
		else{
			mp.start();
			isPlaying = true;
			return 1; // indicate playing
		}
		
	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		
		if(intent.getAction().equals(ACTION_PLAY)){
			if(mp == null){
				// If mp doesn't exist, create it and set the volume.
				mp = new MediaPlayer(); 
				mp.setVolume(1,1); //Set volume to max scale by default. Will need to adjust using system volume.
			}
			
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
	return 0;
	
	}
	

}
