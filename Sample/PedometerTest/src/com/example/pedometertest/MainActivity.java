package com.example.pedometertest;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import semusi.activitysdk.Api;
import semusi.activitysdk.ContextData;
import semusi.activitysdk.ContextSdk;
import semusi.activitysdk.SdkConfig;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	Timer uiTimer = null;

	Button btnStartStop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		//
		btnStartStop = (Button) findViewById(R.id.button1);
		setBtnUI();
		btnStartStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isApiRunning = ContextSdk
						.isSemusiSensing(getApplicationContext());
				if (isApiRunning)
					Api.stopContext();
				else {
					SdkConfig config = new SdkConfig();

				    config.setActivityTrackingAllowedState(true);
				    config.setPedometerTrackingStateAllowed(true);
				    config.setAnalyticsTrackingAllowedState(false);
				    config.setDemographicsTrackingAllowedState(false);
				    config.setPlacesTrackingAllowedState(false);
				    config.setRuleEngineEventStateAllowed(false);
				    
					Api.startContext(getApplicationContext(), config);
				}
			}
		});

		uiTimer = new Timer();
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						updateUILayer();
					}
				});
			}
		}, 1000, 2000);
	}

	private void updateUILayer() {
		// Initialize Sdk object to gather current context data of user
		ContextSdk sdk = new ContextSdk(
				MainActivity.this.getApplicationContext());
		
		long currentDateEpoch = getCurrentDateEpoch();

		// Access pedometer history values
		ContextData[] pedometerData = sdk.getPedometerHistory(currentDateEpoch,
				currentDateEpoch);

		int pedometerCount = 0;
		float pedometerCalories = 0;

		if (pedometerData[0] != null) {
			for (int i = 0; i < pedometerData.length; i++) {
				pedometerCount = pedometerData[i].getPedometerCount();
				pedometerCalories = pedometerData[i]
						.getPedometerCountCalories();
			}
		}

		// set pedometer value
		TextView pedometerCountText = (TextView) findViewById(R.id.TextViewPedo);
		pedometerCountText.setText(pedometerCount + "");

		// set pedometer value
		TextView pedometerCalText = (TextView) findViewById(R.id.TextViewPedoCal);
		pedometerCalText.setText(pedometerCalories + "");

		// set apiVersion
		TextView apiVersion = (TextView) findViewById(R.id.apiVersion);
		try {
			String version = "v"
					+ getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			apiVersion.setText(version);
		} catch (Exception e) {
			e.printStackTrace();
		}

		setBtnUI();
	}

	private void setBtnUI() {
		boolean isApiRunning = ContextSdk
				.isSemusiSensing(getApplicationContext());
		if (isApiRunning)
			btnStartStop.setText("Stop");
		else
			btnStartStop.setText("Start");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Get current date (00:00:00) in epoch time
	 * 
	 * @return epoch time of current day in seconds
	 */
	public long getCurrentDateEpoch() {

		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		long epoch_time = cal.getTimeInMillis() / 1000;

		return epoch_time;
	}

}
