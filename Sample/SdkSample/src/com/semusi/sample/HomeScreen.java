package com.semusi.sample;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONObject;

import semusi.activitysdk.Api;
import semusi.activitysdk.ContextData;
import semusi.activitysdk.ContextSdk;
import semusi.activitysdk.SdkConfig;
import semusi.util.constants.EnumConstants.ActivityAccuracyEnum.ActivityAccuracyLevel;
import semusi.util.constants.EnumConstants.ActivityEnum.ActivityTypeInt;
import semusi.util.constants.EnumConstants.GenderEnum.GenderTypeString;
import semusi.util.constants.EnumConstants.LocationEnum.LocationTypeString;
import semusi.util.constants.EnumConstants.PlacesAccuracyEnum.PlacesAccuracyLevel;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.semusi.sdksample.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeScreen extends Activity {

	boolean isInterestShown = false;
	boolean isActivityShown = false;

	ArrayList<Interests> appInterestArr;
	InterestTokens appInterestView;
	ArrayAdapter<Interests> appInterestAdapter;

	ArrayList<Interests> browserInterestArr;
	InterestTokens browserInterestView;
	ArrayAdapter<Interests> browserInterestAdapter;

	Timer uiTimer = null;

	private enum WH {
		WIDTH, HEIGHT;
	};

	GraphicalView mActivityCaloriesChartView = null;
	GraphicalView mActivityDurationChartView = null;
	int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA,
			Color.CYAN, Color.RED, Color.YELLOW };

	CategorySeries mActivityCaloriesSeries = new CategorySeries(
			"Activity Calories");
	DefaultRenderer mActivityCaloriesRenderer = null;
	CategorySeries mActivityDurationSeries = new CategorySeries(
			"Activity Duration");
	DefaultRenderer mActivityDurationRenderer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home_screen);

		appInterestArr = new ArrayList<Interests>();
		appInterestAdapter = new ArrayAdapter<Interests>(this,
				android.R.layout.simple_list_item_1, appInterestArr);
		appInterestView = (InterestTokens) findViewById(R.id.appInterestViewLayout);
		appInterestView.setAdapter(appInterestAdapter);
		appInterestView.setPrefix("App Interests : ");
		appInterestView.setFocusable(false);
		appInterestView.showMultiLineUI();

		browserInterestArr = new ArrayList<Interests>();
		browserInterestAdapter = new ArrayAdapter<Interests>(this,
				android.R.layout.simple_list_item_1, browserInterestArr);
		browserInterestView = (InterestTokens) findViewById(R.id.browserInterestViewLayout);
		browserInterestView.setAdapter(browserInterestAdapter);
		browserInterestView.setPrefix("Browser Interests : ");
		browserInterestView.setFocusable(false);
		browserInterestView.showMultiLineUI();

		ImageView interestArrow = (ImageView) findViewById(R.id.imageView8);
		interestArrow.setImageResource(R.drawable.arrow_top);

		// If semusi is not sensing
		boolean isApiRunning = ContextSdk
				.isSemusiSensing(getApplicationContext());
		if (isApiRunning == false) {
			SdkConfig config = new SdkConfig();
			config.setPlacesAccuracyLevel(PlacesAccuracyLevel.EAccuracyHigh);
			config.setActivityAccuracyLevel(ActivityAccuracyLevel.EAccuracyHigh);
			config.setDebuggingStateAllowed(true);
			Api.startContext(getApplicationContext(), config);
		}

		// Start UI updated timer
		setupUITimer();

		checkForCrashes();
		checkForUpdates();
	}

	private void checkForCrashes() {
		CrashManager.register(this, "1aed5252ef63f34dc59868fdc7723f6c");
	}

	private void checkForUpdates() {
		UpdateManager.register(this, "1aed5252ef63f34dc59868fdc7723f6c");
	}

	@Override
	public void onPause() {
		super.onPause();

		if (uiTimer != null) {
			stopUITimer();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (uiTimer == null) {
			setupUITimer();
		}
	}

	public void stopUITimer() {
		uiTimer.cancel();
		uiTimer.purge();
		uiTimer = null;
	}

	public void setupUITimer() {
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
		}, 100, 5000);
	}

	public void updateUILayer() {
		ContextSdk sdk = new ContextSdk(HomeScreen.this.getApplicationContext());
		ContextData currentData = sdk.getCurrentContext();

		// Removing all objects from UI
		List<Object> appObjs = appInterestView.getObjects();
		for (Object obj : appObjs) {
			appInterestView.removeObject(obj);
		}
		for (int i = 0; i < appInterestArr.size(); i++) {
			appInterestArr.remove(0);
		}

		List<Object> browserObjs = browserInterestView.getObjects();
		for (Object obj : browserObjs) {
			browserInterestView.removeObject(obj);
		}
		for (int i = 0; i < browserInterestArr.size(); i++) {
			browserInterestArr.remove(0);
		}

		// Setup App interests
		List<JSONObject> appListArr = currentData.getAppInterestData();
		for (int i = 0; i < appListArr.size(); i++) {
			JSONObject obj = appListArr.get(i);
			if (obj != null) {
				try {
					String str = obj.getString("top");
					appInterestArr.add(new Interests(str));
					appInterestView.addObject(str);
				} catch (Exception e) {
					//
				}
			}
		}

		// Setup Browser interests
		List<JSONObject> browserListArr = currentData.getBrowserInterestData();
		for (int i = 0; i < browserListArr.size(); i++) {
			JSONObject obj = browserListArr.get(i);
			if (obj != null) {
				try {
					String str = obj.getString("bottom");
					browserInterestArr.add(new Interests(str));
					browserInterestView.addObject(str);
				} catch (Exception e) {
					//
				}
			}
		}

		// set background image
		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
		// set gender type
		GenderTypeString genderTypeData = currentData.getGenderType();
		ImageView genderImgView = (ImageView) findViewById(R.id.imageView2);
		if (genderTypeData.equals(GenderTypeString.MALE)) {
			genderImgView.setBackgroundResource(R.drawable.gender_male_icon);
			rootView.setBackgroundResource(R.drawable.background_male);
		} else if (genderTypeData.equals(GenderTypeString.FEMALE)) {
			genderImgView.setBackgroundResource(R.drawable.gender_female_icon);
			rootView.setBackgroundResource(R.drawable.background_female);
		} else if (genderTypeData.equals(GenderTypeString.NOGENDERVALUE)) {
			genderImgView.setBackgroundResource(R.drawable.gender_na_icon);
			rootView.setBackgroundResource(R.drawable.background_male);
		}

		// set activity type
		ActivityTypeInt activityType = currentData.getActivityType();
		ImageView activityImgView = (ImageView) findViewById(R.id.imageView5);
		if (activityType == ActivityTypeInt.RunningActivity) {
			activityImgView
					.setBackgroundResource(R.drawable.acitivity_running_icon);
		} else if (activityType == ActivityTypeInt.SittingActivity) {
			activityImgView
					.setBackgroundResource(R.drawable.acitivity_sitting_icon);
		} else if (activityType == ActivityTypeInt.SleepingActivity) {
			activityImgView
					.setBackgroundResource(R.drawable.acitivity_sleeping_icon);
		} else if (activityType == ActivityTypeInt.StandingActivity) {
			activityImgView
					.setBackgroundResource(R.drawable.acitivity_standing_icon);
		} else if (activityType == ActivityTypeInt.VehicleActivity) {
			activityImgView
					.setBackgroundResource(R.drawable.acitivity_driving_icon);
		} else if (activityType == ActivityTypeInt.WalkingActivity) {
			activityImgView
					.setBackgroundResource(R.drawable.acitivity_walking_icon);
		} else if (activityType == ActivityTypeInt.UndefinedActvity) {
			activityImgView.setBackgroundResource(R.drawable.acitivity_na_icon);
		}

		// set location type
		LocationTypeString locationType = currentData.getLocationType();
		System.out.println("aman check : locType : " + locationType);
		ImageView placeImgView = (ImageView) findViewById(R.id.imageView4);
		if (locationType == LocationTypeString.HOME) {
			placeImgView.setBackgroundResource(R.drawable.places_home_icon);
		} else if (locationType == LocationTypeString.OFFICE) {
			placeImgView.setBackgroundResource(R.drawable.places_office_icon);
		} else if (locationType == LocationTypeString.PARK) {
			placeImgView.setBackgroundResource(R.drawable.places_park_icon);
		} else if (locationType == LocationTypeString.RESTAURANT) {
			placeImgView
					.setBackgroundResource(R.drawable.places_resturant_icon);
		} else if (locationType == LocationTypeString.SHOPPING_MALL
				|| locationType == LocationTypeString.SUPER_MARKET) {
			placeImgView.setBackgroundResource(R.drawable.places_shopping_icon);
		} else if (locationType == LocationTypeString.LIFE_STYLE) {
			placeImgView.setBackgroundResource(R.drawable.places_theater_icon);
		} else if (locationType == LocationTypeString.TRANSPORT) {
			placeImgView.setBackgroundResource(R.drawable.places_transit_icon);
		} else {
			placeImgView.setBackgroundResource(R.drawable.acitivity_na_icon);
		}

		// Access pedometer history values
		long currentDateEpoch = getCurrentDateEpoch();
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
		TextView pedometerCalTxt = (TextView) findViewById(R.id.textView1);
		pedometerCalTxt.setText(precision(2, pedometerCalories) + "");
		TextView pedometerCountTxt = (TextView) findViewById(R.id.textView3);
		pedometerCountTxt.setText(pedometerCount + "");

		// Access activity history values
		ContextData[] activityHistory = sdk.getActivityHistory(
				currentDateEpoch, currentDateEpoch,
				ActivityTypeInt.RunningActivity.getActivityValue(),
				ActivityTypeInt.SittingActivity.getActivityValue(),
				ActivityTypeInt.SleepingActivity.getActivityValue(),
				ActivityTypeInt.StandingActivity.getActivityValue(),
				ActivityTypeInt.VehicleActivity.getActivityValue(),
				ActivityTypeInt.WalkingActivity.getActivityValue());
		showActivityChart(activityHistory);
	}

	public void doSlideInterestView(View view) {
		if (!isInterestShown) {
			doSlideInterestUp();
		} else {
			doSlideInterestDown();
		}
	}

	public void doSlideInterestUp() {
		LinearLayout activityLayout = (LinearLayout) findViewById(R.id.activitylayout);
		activityLayout.setVisibility(View.GONE);
		LinearLayout interestLayout = (LinearLayout) findViewById(R.id.interestlayout);
		interestLayout.setVisibility(View.VISIBLE);

		RelativeLayout myView = (RelativeLayout) findViewById(R.id.root);
		ObjectAnimator animation = ObjectAnimator.ofFloat(myView,
				"translationY", 0.0f, -1 * getSceenPercent(WH.HEIGHT, 72f));
		animation.setDuration(500);
		animation.setRepeatCount(0);
		animation.start();
		isInterestShown = !isInterestShown;

		ImageView interestArrow = (ImageView) findViewById(R.id.imageView8);
		interestArrow.setImageResource(R.drawable.arrow_down);
	}

	public void doSlideInterestDown() {
		RelativeLayout myView = (RelativeLayout) findViewById(R.id.root);
		ObjectAnimator animation = ObjectAnimator.ofFloat(myView,
				"translationY", -1 * getSceenPercent(WH.HEIGHT, 72f), 0.0f);
		animation.setDuration(500);
		animation.setRepeatCount(0);
		animation.start();
		isInterestShown = !isInterestShown;

		ImageView interestArrow = (ImageView) findViewById(R.id.imageView8);
		interestArrow.setImageResource(R.drawable.arrow_top);
	}

	public void doSlideActivityView(View view) {
		if (!isActivityShown) {
			doSlideAcitivityLeft();
		} else {
			doSlideActivityRight();
		}
	}

	public void doSlideAcitivityLeft() {
		if (mActivityCaloriesChartView != null
				&& mActivityDurationChartView != null) {
			mActivityCaloriesChartView.setVisibility(View.INVISIBLE);
			mActivityCaloriesChartView.repaint();

			mActivityDurationChartView.setVisibility(View.INVISIBLE);
			mActivityDurationChartView.repaint();
		}

		LinearLayout activityLayout = (LinearLayout) findViewById(R.id.activitylayout);
		activityLayout.setVisibility(View.VISIBLE);
		LinearLayout interestLayout = (LinearLayout) findViewById(R.id.interestlayout);
		interestLayout.setVisibility(View.GONE);

		RelativeLayout myView = (RelativeLayout) findViewById(R.id.root);
		ObjectAnimator animation = ObjectAnimator.ofFloat(myView,
				"translationX", 0.0f, -1 * getSceenPercent(WH.WIDTH, 75f));
		animation.setDuration(500);
		animation.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				if (mActivityCaloriesChartView != null
						&& mActivityDurationChartView != null) {
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mActivityCaloriesChartView
											.setVisibility(View.VISIBLE);
									mActivityCaloriesChartView.repaint();

									mActivityDurationChartView
											.setVisibility(View.VISIBLE);
									mActivityDurationChartView.repaint();
								}
							});
						}
					}, 100);
				}
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});
		animation.setRepeatCount(0);
		animation.start();
		isActivityShown = !isActivityShown;

		ImageView interestArrow = (ImageView) findViewById(R.id.imageView9);
		interestArrow.setImageResource(R.drawable.arrow_right);
	}

	public void doSlideActivityRight() {
		RelativeLayout myView = (RelativeLayout) findViewById(R.id.root);
		ObjectAnimator animation = ObjectAnimator.ofFloat(myView,
				"translationX", -1 * getSceenPercent(WH.WIDTH, 75f), 0.0f);
		animation.setDuration(500);
		animation.setRepeatCount(0);
		animation.start();
		isActivityShown = !isActivityShown;

		ImageView interestArrow = (ImageView) findViewById(R.id.imageView9);
		interestArrow.setImageResource(R.drawable.arrow_left);
	}

	@Override
	public void onBackPressed() {
		if (isInterestShown) {
			doSlideInterestDown();
		} else if (isActivityShown) {
			doSlideActivityRight();
		} else {
			super.onBackPressed();
		}
	}

	public void showActivityChart(ContextData[] activityData) {
		if (mActivityCaloriesRenderer == null) {
			mActivityCaloriesRenderer = new DefaultRenderer();
			mActivityCaloriesRenderer.setApplyBackgroundColor(false);
			mActivityCaloriesRenderer.setBackgroundColor(Color.TRANSPARENT);
			mActivityCaloriesRenderer.setChartTitle("Activity Calories");
			mActivityCaloriesRenderer.setChartTitleTextSize(30);
			mActivityCaloriesRenderer.setLabelsColor(Color.BLACK);
			mActivityCaloriesRenderer.setClickEnabled(false);
			mActivityCaloriesRenderer.setPanEnabled(false);
			mActivityCaloriesRenderer.setLabelsTextSize(20);
			mActivityCaloriesRenderer.setLegendTextSize(30);
			mActivityCaloriesRenderer.setMargins(new int[] { 10, 10, 10, 10 });
			mActivityCaloriesRenderer.setZoomButtonsVisible(false);
			mActivityCaloriesRenderer.setStartAngle(0);
		}

		if (mActivityDurationRenderer == null) {
			mActivityDurationRenderer = new DefaultRenderer();
			mActivityDurationRenderer.setApplyBackgroundColor(false);
			mActivityDurationRenderer.setBackgroundColor(Color.TRANSPARENT);
			mActivityDurationRenderer.setChartTitle("Activity Duration");
			mActivityDurationRenderer.setChartTitleTextSize(30);
			mActivityDurationRenderer.setLabelsColor(Color.BLACK);
			mActivityDurationRenderer.setClickEnabled(false);
			mActivityDurationRenderer.setPanEnabled(false);
			mActivityDurationRenderer.setLabelsTextSize(20);
			mActivityDurationRenderer.setLegendTextSize(30);
			mActivityDurationRenderer.setMargins(new int[] { 10, 10, 10, 10 });
			mActivityDurationRenderer.setZoomButtonsVisible(false);
			mActivityDurationRenderer.setStartAngle(0);
		}

		mActivityCaloriesSeries.clear();
		mActivityCaloriesRenderer.removeAllRenderers();

		mActivityDurationSeries.clear();
		mActivityDurationRenderer.removeAllRenderers();

		for (int i = 0; i < activityData.length; i++) {
			mActivityCaloriesSeries.add(activityData[i].getActivityType()
					.toString(), activityData[i].getActivityCalories());
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mActivityCaloriesSeries.getItemCount() - 1)
					% COLORS.length]);
			mActivityCaloriesRenderer.addSeriesRenderer(renderer);
		}

		for (int i = 0; i < activityData.length; i++) {
			mActivityDurationSeries.add(activityData[i].getActivityType()
					.toString(), activityData[i].getActivityDuration());
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			renderer.setColor(COLORS[(mActivityDurationSeries.getItemCount() - 1)
					% COLORS.length]);
			mActivityDurationRenderer.addSeriesRenderer(renderer);
		}

		if (mActivityCaloriesChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.activitycalorieschart);
			mActivityCaloriesChartView = ChartFactory.getPieChartView(this,
					mActivityCaloriesSeries, mActivityCaloriesRenderer);
			layout.addView(mActivityCaloriesChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
//			mActivityCaloriesChartView = ChartFactory.getPieChartView(this,
//					mActivityDurationSeries, mActivityCaloriesRenderer);
//			mActivityCaloriesChartView.repaint();
		}

		if (mActivityDurationChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.activitydurationchart);
			mActivityDurationChartView = ChartFactory.getPieChartView(this,
					mActivityDurationSeries, mActivityDurationRenderer);
			layout.addView(mActivityDurationChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
//			mActivityDurationChartView = ChartFactory.getPieChartView(this,
//				mActivityDurationSeries, mActivityDurationRenderer);
//			mActivityDurationChartView.repaint();
		}
	}

	public float getSceenPercent(WH forWH, float percent) {
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		float calPercent = 0.0f;
		if (forWH == WH.WIDTH) {
			calPercent = (width * percent) / 100;
		} else if (forWH == WH.HEIGHT) {
			calPercent = (height * percent) / 100;
		}
		return calPercent;
	}

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

	public Float precision(int decimalPlace, Float d) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
}
