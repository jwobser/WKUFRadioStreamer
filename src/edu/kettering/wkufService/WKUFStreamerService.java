package edu.kettering.wkufService;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.media.MediaPlayer;

public class WKUFStreamerService extends Service implements
		MediaPlayer.OnPreparedListener {
	
	private static final String ACTION_PLAY = "edu.kettering.action.PLAY";
	private static final String ACTION_STOP = "edu.kettering.action.STOP";

	/* ***** Variables ***** */
	private static boolean isPlaying; // Is media player playing?
	private static final String StreamURL = "http://audio.moses.bz:80/wkuf-lp";

	/* ***** Media Player ***** */
	MediaPlayer mp = null;

	@Override
	public IBinder onBind(Intent intent) {
	
		return null;
	
		}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	public int playPause() {

		if (isPlaying == true) {
			mp.pause();
			isPlaying = false;
			return 2; // indicate paused
		} else {
			mp.start();
			isPlaying = true;
			return 1; // indicate playing
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (intent.getAction().equals(ACTION_PLAY)) {
			if (mp == null) {
				// If mp doesn't exist, create it and set the volume.
				mp = new MediaPlayer();
				mp.setVolume(1, 1); // Set volume to max scale by default. Will
									// need to adjust using system volume.

				try { // Try setting stream source
					mp.setDataSource(StreamURL);
				} catch (IllegalArgumentException e) {
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
			}
			Log.d("AppStatus", "Asynchronously Prepare Streamer");
			try {
				mp.prepareAsync();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		playPause();
		
	return 0;

	}
	
	}
