package com.inventorydelivery;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;

public class BaseActivty extends Activity implements AppConstants {

	BaseActivty ACTIVTY_INSTANCE = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ACTIVTY_INSTANCE = this;
	}



}
