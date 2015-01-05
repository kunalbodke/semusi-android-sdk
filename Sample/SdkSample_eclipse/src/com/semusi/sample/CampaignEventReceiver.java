package com.semusi.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CampaignEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String extUrl = intent.getStringExtra("Exturl");
		String clicked = intent.getStringExtra("Clicked");

		System.out.println("CampaignEvent Recevied with ExtUrl : " + extUrl
				+ " , " + clicked);
	}
}
