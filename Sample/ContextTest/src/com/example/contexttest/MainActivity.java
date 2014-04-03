package com.example.contexttest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import semusi.activitysdk.Api;
import semusi.activitysdk.ContextData;
import semusi.activitysdk.ContextSdk;
import semusi.util.constants.EnumConstants;

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
				else
					Api.startContext(getApplicationContext());
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
		ContextData currentData = sdk.getCurrentContext();

		// set gender type
		EnumConstants.GenderEnum.GenderTypeString genderTypeData = currentData
				.getGenderType();
		TextView genderType = (TextView) findViewById(R.id.genderTypeTv);
		genderType.setText(genderTypeData.toString().toUpperCase());

		// set height type
		EnumConstants.HeightEnum.HeightTypeString heightTypeData = currentData
				.getHeightType();
		TextView heightType = (TextView) findViewById(R.id.heightTypeTv);
		heightType.setText(heightTypeData.toString().toUpperCase());

		// set weight type
		EnumConstants.WeightEnum.WeightTypeString weightTypeData = currentData
				.getWeightType();
		TextView weightType = (TextView) findViewById(R.id.weightTypeTv);
		weightType.setText(weightTypeData.toString().toUpperCase());

		// set activity type
		TextView activityType = (TextView) findViewById(R.id.TextView02);
		activityType.setText(currentData.getActivityType().toString()
				.toUpperCase()
				+ "");

		// set places type
		TextView placesType = (TextView) findViewById(R.id.TextView04);
		placesType.setText(currentData.getLocationType().toUpperCase());

		// set when based value
		TextView whenText = (TextView) findViewById(R.id.TextView01);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		String dateStr = formatter.format(Calendar.getInstance().getTime());
		whenText.setText(dateStr);

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

}
