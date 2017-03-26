package com.xbw.arukas;

import android.os.Bundle;

import com.xbw.arukas.left.SwipeBackActivity;
import com.xbw.arukas.left.SwipeBackLayout;


public class BaseLeftActivity extends SwipeBackActivity {
	private SwipeBackLayout mSwipeBackLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSwipeBackLayout = getSwipeBackLayout();
	}

}
