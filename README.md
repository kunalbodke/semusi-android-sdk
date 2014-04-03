semusi-android-sdk
==================

Android SDK code of Semusi Context SDK, Use it to enrich your Android apps with the power of context

<b>Getting Started</b><br>
In this tutorial we will guide you through the process of creating a hello world package that uses semusi service to find out which activities are being performed by the user, what is the current context and how many calories are being burnt in this process.

The Semusi Service API consists of an Android service and a base API, both Service and the API are coupled in the JAR provided with the SDK bundle.

The Semusi service need to use a number of resources in order to be able to track the user’s activities under all conditions, and make sure that the service doesn’t get killed if the phone is under stress, or of any reason; Don’t worry its battery optimized, your users won’t complain.

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
<li>Setting up hardware permissions – Copy and paste these permissions in your AndroidManifest.xml file.</li>
</ul>


```java
// Remember we use Accelerometer to find out the activities
<uses-feature
android:name="android.hardware.sensor.accelerometer"
android:required="true" />
```
<ul>
<li>Setting up user permissions – Copy and paste these permissions in your AndroidManifest.xml file.</li>
</ul>

```java
// DEVICE_POWER – We need to know the power state of device to make ourselves more efficient
// GET_TASKS – We use it to find out the current running tasks and measure their battery usage and we find our own battery usage percentage to find out how are we doing.
// BATTERY_STATS – Quite simply used to find out battery statistics.
// WRITE_EXTERNAL STORAGE – Used to write the logs and database files.
// INTERNET – Used to upload/downlaod the data to and from cloud.
// ACCESS_COARSE_LOCATION / ACCESS_FINE_LOCATION – Used to find out location; very much required to find out in-vehicle sort of cases.
// RECEIVE_BOOT_COMPLETED - Find out if the system has finished booting, and start our service.
// READ_LOGS – Read the ACRA logs.
// WAKE_LOCK – To keep processor from sleeping or screen from dimming.
// READ_PHONE_STATE - To read the user profile
// ACCESS_NETWORK_STATE – Allows the application to read network state, useful for in-vehicle activity.

<uses-permission android:name="android.permission.DEVICE_POWER" />
<uses-permission android:name="android.permission.GET_TASKS" />
<uses-permission android:name="android.permission.BATTERY_STATS" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.READ_LOGS" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="com.android.browser.permission.READ_HISTORY_BOOKMARKS" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_SMS" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.GET_ACCOUNTS" />
<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
<uses-permission android:name="YOUR.PACKAGE.NAME.permission.C2D_MESSAGE" />
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

        <receiver android:name="ruleengine.pushmanager.UAPushIntentReceiver" />
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
import semusi.util.constants.EnumConstants;
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
else
    Api.startContext(getApplicationContext());
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
Below code is used to get the History Data of Acitvities for a given range of dates, and set of Activities for which data is to be fetched.

```java
// Getting data history - Returns array
// fromDate - epoch value at 00:00:00 hrs
// toDate - epoch value at 00:00:00 hrs

ContextData[] historyData = sdk.getActivityHistory(fromDate, toDate, ActivityType.WalkingActivity.ordinal(),            
        ActivityType.SittingActivity.ordinal(), ActivityType.RunningActivity.ordinal());

// Access history values
if(historyData[0] != null) 
{
    for(int i = 0; i < historyData.length; i++)
    {
        ActivityData activity = historyData[i].getActivityType();
        float duration = historyData[i].getDuration(); // duration in minutes
        float calories = historyData[i].getCalories();
        long date = historyData[i].getDate(); // date in epoch 00:00:00
    }
}
```
