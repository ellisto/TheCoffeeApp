package org.ellisto.thecoffeeapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class TimerActivity extends ActionBarActivity {
	TextView timerTextView, nextStepTextView;
	int targetMin = 4;
	int targetSec = 0;
	long targetMs = 0;
	long stirTimeMs = 60000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		timerTextView = (TextView) findViewById(R.id.timerTextView);
		nextStepTextView = (TextView) findViewById(R.id.nextStepTextView);

		targetMs = targetMin * 60000 + targetSec * 1000;

		new CountDownTimer(targetMs, 1000) {

			public void onTick(long remainingMs) {
				int seconds = (int) (remainingMs / 1000);
				int minutes = seconds / 60;
				seconds = seconds % 60;

				timerTextView.setText(String
						.format("%d:%02d", minutes, seconds));

				if (remainingMs <= targetMs - stirTimeMs) {
					alertStir();
				}
			}

			public void onFinish() {
				timerTextView.setText("0:00");
				// TODO: pull this from a resource
				nextStepTextView.setText("Press and enjoy!");
				// TODO: play a sound, vibrate
			}
		}.start();
	}

	protected void alertStir() {
		// TODO: pull this from a resource
		nextStepTextView.setText("Time to stir and add the rest of the water!");

		// TODO: play a sound, vibrate

	}
}
