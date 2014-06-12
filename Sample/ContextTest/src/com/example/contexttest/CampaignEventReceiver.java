package com.example.contexttest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CampaignEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String notificationType = intent.getStringExtra("NotificationType");
		String ContentType = intent.getStringExtra("ContentType");
		String ContentData = intent.getStringExtra("ContentData");

		System.out.println("CampaignEvent Recevied of NotificationType: "
				+ notificationType + " , ofContentType: " + ContentType
				+ " , withContent: " + ContentData);

	}
}
