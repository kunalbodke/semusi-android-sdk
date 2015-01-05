package com.semusi.sdksample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ContextEventReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String contextChangedType = intent.getStringExtra("EventType");
		String contextChangedVal = intent.getStringExtra("EventVal");

		System.out.println("ContextEvent Recevied with ExtUrl : "
				+ contextChangedType + " , " + contextChangedVal);
	}
}
