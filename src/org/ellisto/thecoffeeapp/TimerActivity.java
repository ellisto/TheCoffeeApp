package org.ellisto.thecoffeeapp;

import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends ActionBarActivity {
	private static final String NEXT_STEP = "org.ellisto.thecoffeeapp.NEXT_STEP_TEXT";
	private static final String ALARM_RINGING = "org.ellisto.thecoffeeapp.ALARM_RINGING";
	TextView timerTextView, nextStepTextView;
	Button stopAlarmButton;
	boolean alarmRinging = false;

	MediaPlayer alarmMp = null;

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				long remainingMs = bundle
						.getLong(TimerService.MILLIS_REMAINING);
				if (remainingMs >= 0) {
					int seconds = (int) (remainingMs / 1000);
					int minutes = seconds / 60;
					seconds = seconds % 60;
					timerTextView.setText(String.format("%d:%02d", minutes,
							seconds));
					if (remainingMs == 0) {
						// TODO: pull this from a resource
						nextStepTextView.setText("Press and enjoy!");
						playAlarmSound();
						// TODO: vibrate
					}
				}
				if (bundle.getBoolean(TimerService.STIR_ALERT)) {
					alertStir();
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		timerTextView = (TextView) findViewById(R.id.timerTextView);
		nextStepTextView = (TextView) findViewById(R.id.nextStepTextView);

		stopAlarmButton = (Button) findViewById(R.id.stopAlarmButton);

		if (savedInstanceState != null) {
			nextStepTextView.setText(savedInstanceState.getString(NEXT_STEP));
			alarmRinging = savedInstanceState.getBoolean(ALARM_RINGING);
		}

		if (alarmRinging) {
			stopAlarmButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(TimerService.NOTIFICATION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString(NEXT_STEP, nextStepTextView.getText()
				.toString());
		savedInstanceState.putBoolean(ALARM_RINGING, alarmRinging);
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
		alarmRinging = true;
	}

	public void stopAlarm(View view) {
		if (alarmMp != null) {
			alarmMp.stop();
			alarmMp = null;
		}
		alarmRinging = false;
		stopAlarmButton.setVisibility(View.INVISIBLE);
	}
}
