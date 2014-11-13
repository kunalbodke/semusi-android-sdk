package com.semusi.sample;

import semusi.activitysdk.Api;
import semusi.activitysdk.ContextApplication;
import semusi.activitysdk.ContextSdk;
import semusi.activitysdk.SdkConfig;
import semusi.util.constants.EnumConstants.ActivityAccuracyEnum.ActivityAccuracyLevel;
import semusi.util.constants.EnumConstants.PlacesAccuracyEnum.PlacesAccuracyLevel;

/**
 * Extend Application class with ContextApplication class
 * 
 * @author mac
 *
 */
public class MainApplication extends ContextApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		// If semusi is not sensing
		boolean isApiRunning = ContextSdk
				.isSemusiSensing(getApplicationContext());
		if (isApiRunning == false) {
			SdkConfig config = new SdkConfig();
			config.setActivityTrackingAllowedState(false);
			config.setActivityAccuracyLevel(ActivityAccuracyLevel.EAccuracyMedium);
			config.setDemographicsTrackingAllowedState(false);
			config.setPedometerTrackingStateAllowed(false);

			config.setAnalyticsTrackingAllowedState(true);
			config.setPlacesTrackingAllowedState(true);
			config.setPlacesAccuracyLevel(PlacesAccuracyLevel.EAccuracyHigh);
			config.setRuleEngineEventStateAllowed(true);
			config.setDebuggingStateAllowed(true);
			config.setContinuousSensingAllowed(false);
			Api.startContext(getApplicationContext(), config);
		}
	}
}
