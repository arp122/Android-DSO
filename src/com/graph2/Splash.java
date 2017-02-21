package com.graph2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ViewFlipper;

public class Splash extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,        
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		final ViewFlipper viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		viewFlipper.setFlipInterval(2000);
		viewFlipper.startFlipping();
		
		Thread t = new Thread()
		{
			public void run()
			{
				try
				{
					Thread.sleep(3500);
				}
				catch(InterruptedException IE)
				{
					IE.printStackTrace();
				}
				finally
				{
					Intent intent = new Intent(Splash.this,MainActivity.class);
					startActivity(intent);
					viewFlipper.stopFlipping();
					finish();
				}
			}
		};
		t.start();
	}
}
