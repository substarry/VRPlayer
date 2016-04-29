/**
 * Copyright 2015 Dennis Ippel
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.rajawali3d.vr;

import android.app.ActionBar;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import org.rajawali3d.vr.renderer.RajawaliVRRenderer;
import org.rajawali3d.vr.surface.RajawaliVRSurfaceView;

/**
 * @author dennis.ippel
 */
public class RajawaliVRActivity extends CardboardActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CardboardView cardboardView = new CardboardView(this);

        addContentView(cardboardView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT));

		cardboardView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	            | View.SYSTEM_UI_FLAG_IMMERSIVE);

        setCardboardView(cardboardView);
	}

	protected void setRenderer(RajawaliVRRenderer renderer) {
		getCardboardView().setRenderer(renderer);
	}

	protected void setOnTouchListener(View.OnTouchListener listener){
		getCardboardView().setOnTouchListener(listener);
	}

	protected void setVRModeEnabled(boolean enabled) {
		getCardboardView().setVRModeEnabled(enabled);
	}

	protected boolean getVRMode() {
		return getCardboardView().getVRMode();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
}

