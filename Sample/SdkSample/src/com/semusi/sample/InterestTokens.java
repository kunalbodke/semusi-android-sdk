package com.semusi.sample;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semusi.sdksample.R;
import com.tokenautocomplete.TokenCompleteTextView;

public class InterestTokens extends TokenCompleteTextView {

	public InterestTokens(Context context) {
		super(context);
	}

	public InterestTokens(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public InterestTokens(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected View getViewForObject(Object object) {
		String p = (String) object;

		LayoutInflater l = (LayoutInflater) getContext().getSystemService(
				Activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout view = (LinearLayout) l.inflate(R.layout.token_interests,
				(ViewGroup) InterestTokens.this.getParent(), false);
		((TextView) view.findViewById(R.id.interest)).setText(p);
		return view;
	}

	@Override
	protected Object defaultObject(String completionText) {
		return new Interests(completionText);
	}
}
