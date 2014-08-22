package org.ellisto.thecoffeeapp;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends ActionBarActivity {
	TextView timerTextView, nextStepTextView;
	Button stopAlarmButton;
	int targetMin = 4;
	int targetSec = 0;
	long targetMs = 0;
	boolean stirAlerted = false;
	long stirTimeMs = 1000;// 60000;

	MediaPlayer alarmMp = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		timerTextView = (TextView) findViewById(R.id.timerTextView);
		nextStepTextView = (TextView) findViewById(R.id.nextStepTextView);
		stopAlarmButton = (Button) findViewById(R.id.stopAlarmButton);

		targetMs = targetMin * 60000 + targetSec * 1000;

		new CountDownTimer(targetMs, 1000) {

			public void onTick(long remainingMs) {
				int seconds = (int) (remainingMs / 1000);
				int minutes = seconds / 60;
				seconds = seconds % 60;

				timerTextView.setText(String
						.format("%d:%02d", minutes, seconds));

				if (!stirAlerted && remainingMs <= targetMs - stirTimeMs) {
					stirAlerted = true;
					alertStir();
				}
			}

			public void onFinish() {
				timerTextView.setText("0:00");
				// TODO: pull this from a resource
				nextStepTextView.setText("Press and enjoy!");
				playAlarmSound();
				// TODO: vibrate
			}
		}.start();
	}

	protected void alertStir() {
		// TODO: pull this from a resource
		nextStepTextView.setText("Time to stir and add the rest of the water!");
		playAlarmSound();
		// TODO: vibrate
	}

	protected void playAlarmSound() {
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_ALARM);
		alarmMp = MediaPlayer.create(getApplicationContext(), notification);
		try {
			alarmMp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alarmMp.setLooping(true);
		alarmMp.start();
		stopAlarmButton.setVisibility(View.VISIBLE);
	}

	public void stopAlarm(View view) {
		if (alarmMp != null) {
			alarmMp.stop();
			alarmMp = null;
		}
		stopAlarmButton.setVisibility(View.INVISIBLE);
	}
}
