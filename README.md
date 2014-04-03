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

This is how your directory structure should look like.
<img href="http://semusi.com/images/Semusi-Context-Aware-SDK-Directory-Structure_1.png"></img>


<b>Semusi SDK Directory Structure</b>

<b>Step 2</b>
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
```
<ul>
<li>Setting up intents and services – Let us setup our service in the same AndroidManifest.xml</li>
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
```

<ul>
<li>A completed AndroidManifest.xml would look quite like this.</li>
</ul>

```java
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="semusi.semusiapp" android:versionCode="1" android:versionName="1.0" >
<uses-sdk android:minSdkVersion="9" android:targetSdkVersion="16" />
<uses-feature android:name="android.hardware.sensor.accelerometer" android:required="true" />
<!-- detect unplug actions -->
<uses-permission android:name="android.permission.DEVICE_POWER" />

<!-- retrieve ps list (running tasks) -->
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

<application android:allowBackup="true" android:icon="@drawable/ic_launcher" android:label="@string/app_name" android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen" >
<activity android:name="semusi.semusiapp.MainActivity" android:label="@string/app_name" >
<intent-filter>
<action android:name="android.intent.action.MAIN" />
<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
</activity>

<service android:name="semusi.activitysdk.Api" />
<service android:name="semusi.mlservice.SemusiHAR" />
<receiver android:name="semusi.mlservice.OnAlarmReceiver" >
<intent-filter>
<action android:name="android.intent.action.BOOT_COMPLETED" >
</action>
</intent-filter>
</receiver>

</application>
</manifest>
```


<b>Step 3</b>
Import the packages – Import these packages, before we start using the SDK, rest of the imports we can resolve on the fly.

```java
import semusi.activitysdk.Api; 
import semusi.activitysdk.ContextData; 
import semusi.activitysdk.ContextSdk; 

// This code is to start the ContextSDK Service
Intent i = new Intent(getApplicationContext(), Api.class);
getApplicationContext().startService(i);

// This code is to gather current context of user
ContextSdk sdk = new ContextSdk(MainActivity.this.getApplicationContext());
ContextData currentData = sdk.getCurrentContext();

// Getting Current Activity type
ActivityType actType = currentData.getActivityType();

// Getting Gender type
GenderType genType = currentData.getGenderType();

// Getting Weight type
WeightType weType = currentData.getWeightType();

// Getting Height type
HeightType heiType = currentData.getHeightType();

// Getting User InterestData : Return will be in form of string array list
String[] interestArr = currentData.getInterestData();

// Getting User Location Info
double latitudeVal = currentData.getLocationLat();
double longitudeVal = currentData.getLocationLong();
String addressInfo = currentData.getLocationAddress();
String[] addressTypes = currentData.getLocationType();
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
Below code is used to initialize ContextSdk service

```java
//initialize ContextSdk service
Intent i = new Intent(getApplicationContext(), Api.class);
getApplicationContext().startService(i);
```

Below code is used to initialize ContextSdk with context object, and get the current activity, current demographics (Gender,Weight,Height,Interest,and location).

```java
ContextSdk sdk = new ContextSdk(MainActivity.this.getApplicationContext());

ContextData currentData = sdk.getCurrentContext();

//Getting Current Activity type and return will be of enum type ‘ActivityType’
ActivityType actType = currentData.getActivityType();

//Getting Gender type : Return will be of enum type ‘GenderType’
GenderType genType = currentData.getGenderType();

//Getting Weight type : Return will be of enum type ‘WeightType’
WeightType weType = currentData.getWeightType();

//Getting Height type : Return will be of enum type ‘HeightType’
HeightType heiType = currentData.getHeightType();

//Getting User InterestData : Return will be in form of string array list
String[] interestArr = currentData.getInterestData();

//Getting User Location Info
double latitudeVal = currentData.getLocationLat();
double longitudeVal = currentData.getLocationLong();
String addressInfo = currentData.getLocationAddress();
String[] addressTypes = currentData.getLocationType();`
```

<b>GetHistoryData API</b>
Below code is used to get the History Data of Acitvities for a given range of dates, and set of Activities for which data is to be fetched.

```java
// Initialize ContextSDK
ContextSdk sdk = new ContextSdk(MainActivity.this.getApplicationContext());

// Getting data history - Returns array
// fromDate - epoch value at 00:00:00 hrs
// toDate - epoch value at 00:00:00 hrs
// ActivityType - Enum that can be bitwise-OR'd to get different activities. Enum values as below

ContextData[] historyData = sdk.getActivityHistory(fromDate, toDate, ActivityType.WalkingActivity.ordinal() | ActivityType.SittingActivity.ordinal() | ActivityType.RunningActivity.ordinal());

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

<b>ActivityType - Enum Values</b>
StandingActivity
WalkingActivity
RunningActivity
SittingActivity
SleepingActivity
