package org.ellisto.thecoffeeapp;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class TimerService extends Service {
	int targetMin = 4;
	int targetSec = 0;
	long targetMs = 0;
	boolean stirAlerted = false;
	long stirTimeMs = 3000;// 60000;

	public static final String NOTIFICATION = "org.ellisto.thecoffeeapp.receiver";
	public static final String MILLIS_REMAINING = "org.ellisto.thecoffeeapp.MINUTES_REMAINING";
	public static final String STIR_ALERT = "org.ellisto.thecoffeeapp.STIR_ALERT";

	// @Override
	// protected void onHandleIntent(Intent workIntent) {
	// targetMs = targetMin * 60000 + targetSec * 1000;
	//
	// new CountDownTimer(targetMs, 1000) {
	//
	// public void onTick(long remainingMs) {
	//
	// broadcastTimerUpdate(remainingMs);
	//
	// if (!stirAlerted && remainingMs <= targetMs - stirTimeMs) {
	// stirAlerted = true;
	// broadcastStirAlert();
	// }
	// }
	//
	// public void onFinish() {
	// broadcastTimerUpdate(0);
	// }
	// }.start();
	// }
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		targetMs = targetMin * 60000 + targetSec * 1000;

		new CountDownTimer(targetMs, 1000) {

			public void onTick(long remainingMs) {

				broadcastTimerUpdate(remainingMs);

				if (!stirAlerted && remainingMs <= targetMs - stirTimeMs) {
					stirAlerted = true;
					broadcastStirAlert();
				}
			}

			public void onFinish() {
				broadcastTimerUpdate(0);
				stopSelf();
			}
		}.start();
		return Service.START_NOT_STICKY;
	}

	private void broadcastTimerUpdate(long milliseconds) {
		Intent intent = new Intent(NOTIFICATION);

		intent.putExtra(MILLIS_REMAINING, milliseconds);
		sendBroadcast(intent);
	}

	private void broadcastStirAlert() {
		Intent intent = new Intent(NOTIFICATION);
		intent.putExtra(STIR_ALERT, true);
		intent.putExtra(MILLIS_REMAINING, (long) -1);
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
