semusi-android-sdk
==================

Android SDK code of Semusi Context SDK, Use it to enrich your Android apps with the power of context

<b>Getting Started</b><br>
In this tutorial we will guide you through the process of creating a hello world package that uses Semusi service to find out which activities are being performed by the user, what is the current context and how many calories are being burnt in this process. The Semusi service also tells you the user demographics - the gender, height and weight of the user, along with the current place i.e. work, home, in-transit, other. We also give you user-specific analytics through our Semusi Analytics site, and to take action based on these contexts.

The Semusi Service API consists of an Android service and a base API, both Service and the API are coupled in the JAR provided with the SDK bundle.

The Semusi service need to use a number of resources in order to be able to track the user’s activities under all conditions, and make sure that the service doesn’t get killed if the phone is under stress, or of any reason; Don’t worry about its battery consumption as it is optimized and your users won’t complain.

We’ll see what permissions are those and how they help us; the base API however uses Internet as well so internet permissions will also be required.

The min. SDK version for your application should be 9 while targetSDKVersion can be 19.

<b>Step 1</b><br>
<ul>
<li>Put the contextsdk.jar in the libs folder, and that's it.</li>
</ul>

This is how your directory structure should look like.<br><br>
<img src="http://semusi.com/images/Semusi-Context-Aware-SDK-Directory-Structure_1.png"></img>


<b>Semusi SDK Directory Structure</b>

<b>Step 2</b>
<ul>
<li>You need to replace "YOUR.PACKAGE.NAME" with your application's package name.</li>
</ul>

<ul>
<li>Setting up user permissions – Copy and paste these permissions in your AndroidManifest.xml file.</li>
</ul>

```java

// WRITE_EXTERNAL STORAGE – Used to write logs.
// READ_LOGS – Read logs for ACRA.
// INTERNET – Used to upload/downlaod the data to and from the cloud.
// READ_PHONE_STATE - To read the user profile
// ACCESS_NETWORK_STATE – Allows the application to read network state, and check for internet availability.
// RECEIVE_BOOT_COMPLETED - Find out if the system has finished booting, and start our service.
// WAKE_LOCK – To keep processor from sleeping and optimizing algorithm vs battery use.
// ACCESS_COARSE_LOCATION – Used to find out location; required to find out in-vehicle activity and places detection.
// c2dm.permission.RECEIVE - Allow push messages for rules
// permission.C2D_MESSAGE - Allow executing rules
// VIBRATE - Used for push handling system
// READ_HISTORY_BOOKMARKS - Allows for better prediction
// READ_SMS - Allows for better prediction
// GET_ACCOUNTS - allows for better prediction


// Mandatory Permissions
    <!-- Mandatory for error Logging -->    
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_LOGS" />
    
    <!-- Mandatory for registering app to Semusi analytics, checking internet connectivity, 
    	getting Device ID and IMEI -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    
    <!-- Mandatory for background functionality of app -->
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <!-- Mandatory for app start on boot -->
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

    <!-- Mandatory for Rule Handling -->
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
    <uses-permission android:name="YOUR.PACKAGE.NAME.permission.C2D_MESSAGE" />

// Optional - Use as per desired functionality
    <!-- Mandatory only for Places detection and In-vehicle activity detection functionality -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    <!-- Optional for Places -->
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    
    <!-- Optional for better prediction -->
    <uses-permission android:name="android.permission.READ_SMS" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="com.android.browser.permission.READ_HISTORY_BOOKMARKS" />
    
```

<ul>
<li>Setting up C2D permissions – Copy and paste these permissions in your AndroidManifest.xml file.</li>
</ul>
```java
<permission
    android:name="YOUR.PACKAGE.NAME.permission.C2D_MESSAGE"
    android:protectionLevel="signature" />
```

<ul>
<li>Setting up 'Application' class – Copy and paste this in your AndroidManifest.xml file.</li>
</ul>
```java
<application android:name="YOUR.PACKAGE.NAME.MyApplication" >
```        

<ul>
<li>Setting up services and recievers – Let us setup our services and recievers in the same AndroidManifest.xml</li>
</ul>
```java
// Copy over this xml to your androidmanifest.xml
// The base API service, which is responsible for making sure that the core services collecting and analyzing the data keep running
<service android:name="semusi.activitysdk.Api" />
```

```java
// This is the core service, which is based on native sensor data processing and native c++ based machine learning algorithms
<service android:name="semusi.mlservice.SemusiHAR" />
```

```java
// This is the core service which is used to receive campaign events
<service android:name="semusi.ruleengine.rulemanager.RuleGatherService" />
```

```java
// A receiver, for boot completed intent, which is majorly used by our services to know when the phone has been rebooted and finished booting, so we could restart our STICKY services.
<receiver android:name="semusi.mlservice.OnAlarmReceiver" >
<intent-filter>
<action android:name="android.intent.action.BOOT_COMPLETED" >
</action>
</intent-filter>
</receiver>

// A receiver for rule execution.
<receiver
    android:name="ruleengine.rulemanager.RuleTimerEventHandler"
    android:enabled="true" />

```

Here is the process for getting the AppID, AppKey, and APIKey

<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+12%3A45%3A12.png"></img>
<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+12%3A45%3A24.png"></img>
<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+12%3A54%3A00.png"></img>
<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+12%3A54%3A15.png"></img>
<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+12%3A59%3A26.png"></img>
<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+13%3A03%3A00.png"></img>
<img src="https://s3.amazonaws.com/git_images/Screenshot+from+2014-04-07+13%3A03%3A10.png"></img>

```java
// You need to login/register for Semusi SDK features. Use the keys provided, and add below.

<meta-data
    android:name="com.google.android.gms.version"
    android:value="4132500" />

<!-- Required Meta-Data keys for SemusiSDK -->
<meta-data
    android:name="com.semusi.analytics.appid"
    android:value="YOUR.APPLICATION.APPID" />
<meta-data
    android:name="com.semusi.analytics.appkey"
    android:value="YOUR.APPLICATION.APPKEY" />
<meta-data
    android:name="com.semusi.analytics.apikey"
    android:value="YOUR.APPLICATION.APIKEY" />
<!-- End of required meta-data -->
```

```java
<!-- PushHandling setup start -->
        <receiver android:name="com.urbanairship.CoreReceiver" />
        <receiver
            android:name="com.urbanairship.push.GCMPushReceiver"
            android:permission="com.google.android.c2dm.permission.SEND" >
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <action android:name="com.google.android.c2dm.intent.REGISTRATION" />

                <category android:name="YOUR.PACKAGE.NAME" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.YOUR.PACKAGE.NAME" />

                <data android:scheme="package" />
            </intent-filter>
        </receiver>

        <service
            android:name="com.urbanairship.push.PushService"
            android:label="Push Notification Service" />
        <service
            android:name="com.urbanairship.push.PushWorkerService"
            android:label="Push Notification Worker Service" />
        <service
            android:name="com.urbanairship.analytics.EventService"
            android:label="Event Service" />

        <provider
            android:name="com.urbanairship.UrbanAirshipProvider"
            android:authorities="YOUR.PACKAGE.NAME.urbanairship.provider"
            android:exported="false"
            android:multiprocess="true" />

        <service android:name="com.urbanairship.richpush.RichPushUpdateService" />
        <service
            android:name="com.urbanairship.location.LocationService"
            android:label="Segments Service" />

        <meta-data
            android:name="com.urbanairship.autopilot"
            android:value="ruleengine.pushmanager.UAAutoPilotRecevier" />

        <receiver android:name="semusi.ruleengine.pushmanager.UAPushIntentReceiver" />
        <!-- PushHandling Entry end -->
```


<b>Step 3</b>

<ul>
<li>Sample code for application class. You need to override 'Application' class with 'ContextApplication'.</li>
</ul>
```java
package YOUR.PACKAGE.NAME;

import semusi.activitysdk.ContextApplication;

public class MyApplication extends ContextApplication {

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
```

<ul>
<li>Import the packages – Import these packages, before we start using the SDK, rest of the imports we can resolve on the fly.</li>
</ul>

```java
import semusi.activitysdk.Api;
import semusi.activitysdk.ContextData;
import semusi.activitysdk.ContextSdk;
import semusi.activitysdk.SdkConfig;
import semusi.util.constants.EnumConstants;
import semusi.util.constants.EnumConstants.ActivityAccuracyEnum.ActivityAccuracyLevel;
import semusi.util.constants.EnumConstants.PlacesAccuracyEnum.PlacesAccuracyLevel;
```

<hr>

<b>Libraries</b>
The Semusi service uses a number of java libraries and natively compiled modules to track the activities facilitate the data retrieval and log the events.

<b>Beta Client Libraries</b>
Use Caution! These libraries are still in beta phases, so feel free to play with them, but be ready to receive more and more updates, which will make them more solid in future.

<b>Java libraries</b>
contextsdk.jar - the basic activity tracking service and base api.

<hr>

<b>API Reference</b><br>
<b>PULL API</b><br>

```java
// Below code is used to initialize Semusi service
// This is NECESSARY to make Semusi SDK work

boolean isApiRunning = ContextSdk.isSemusiSensing(getApplicationContext());

if (isApiRunning)
    Api.stopContext();
else {
    // Config to handle enable-disable state
    SdkConfig config = new SdkConfig();
    config.setPlacesAccuracyLevel(PlacesAccuracyLevel.EAccuracyHigh);
    config.setActivityAccuracyLevel(ActivityAccuracyLevel.EAccuracyHigh);
    config.setActivityTrackingAllowedState(true);
    config.setAnalyticsTrackingAllowedState(true);
    config.setDemographicsTrackingAllowedState(true);
    config.setPedometerTrackingStateAllowed(true);
    config.setPlacesTrackingAllowedState(true);
    config.setRuleEngineEventStateAllowed(true);
    
    // start semusi context sensing with config
    // by default all setters are enabled and accuracy level to High
    Api.startContext(getApplicationContext(), config);
}
```

Below code is used to initialize ContextSdk with context object, and get the current activity, current demographics (Gender,Weight,Height,Interest,and location).

```java
	ContextSdk sdk = new ContextSdk(
				MainActivity.this.getApplicationContext());

	ContextData currentData = sdk.getCurrentContext();

        //Getting Gender type : Return will be of enum type 'EnumConstants.GenderEnum.GenderTypeString'
	EnumConstants.GenderEnum.GenderTypeString genderTypeData = currentData
				.getGenderType();

        //Getting Height type : Return will be of enum type 'EnumConstants.HeightEnum.HeightType'
	EnumConstants.HeightEnum.HeightTypeString heightTypeData = currentData
				.getHeightType();

        //Getting Weight type : Return will be of enum type 'EnumConstants.WeightEnum.WeightType'
	EnumConstants.WeightEnum.WeightTypeString weightTypeData = currentData
				.getWeightType();

        //Getting Current Activity type and return will be of enum type 'EnumConstants.ActivityEnum.ActivityTypeInt'
        EnumConstants.ActivityEnum.ActivityTypeInt actType = currentData.getActivityType();

        //Getting User Places Info and return will be of type 'String'
        String place = currentData.getLocationType();
```

<b>GetHistoryData API</b>
Below code is used to get the History Data of Acitvities for a given range of dates, and set of Activities for which data is to be fetched. For accessing data for a single date, use the same date epoch for fromDateEpoch and toDateEpoch.

```java
// Getting data history - Returns array
// fromDate - epoch value at 00:00:00 hrs
// toDate - epoch value at 00:00:00 hrs

ContextData[] historyData = sdk.getActivityHistory(fromDateEpoch, toDateEpoch, 
				ActivityTypeInt.WalkingActivity.getActivityValue(),            
				ActivityTypeInt.StandingActivity.getActivityValue(),
				ActivityTypeInt.SittingActivity.getActivityValue(),
				ActivityTypeInt.SleepingActivity.getActivityValue(),
				ActivityTypeInt.RunningActivity.getActivityValue(),
				ActivityTypeInt.VehicleActivity.getActivityValue());

// Access history values
if(historyData[0] != null) 
{
    for(int i = 0; i < historyData.length; i++)
    {
        ActivityTypeInt activity = historyData[i].getActivityType();
        float duration = historyData[i].getActivityDuration(); // duration in minutes
        float calories = historyData[i].getActivityCalories();
        long date = historyData[i].getDate(); // date in epoch 00:00:00
    }
}
```

<b>GetPedometerHistory API</b>
Below code is used to get Pedometer Data for a given range of dates. For accessing data for a single date, use the same date epoch for fromDateEpoch and toDateEpoch.

```java
// Access pedometer history values
ContextData[] pedometerData = sdk.getPedometerHistory(fromDateEpoch, toDateEpoch);

int pedometerCount = 0;
float pedometerCalories = 0;
		
if(pedometerData[0] != null) {
	for(int i = 0; i < pedometerData.length; i++)
	{
		pedometerCount = pedometerData[i].getPedometerCount();
		pedometerCalories = pedometerData[i].getPedometerCountCalories();
	}
}
```


<b>Campaign Event Listener</b>
Below code is used to capture campaign events

```java
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
```
