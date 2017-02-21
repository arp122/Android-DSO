package com.graph2;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.androidplot.util.PlotStatistics;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class MainActivity extends Activity implements OnDoubleTapListener, OnGestureListener{

	// redraws a plot whenever an update is received:



	private static int HISTORY_SIZE = 300;            // number of points to plot in history


	private SimpleXYSeries aprLevelsSeries = null;

	private SimpleXYSeries rollHistorySeries = null;
	private ToggleButton hold;
	private Button back;
	private XYPlot dynamicPlot;
	int count = 0;


	Thread thread ;
	//BTInterface bt;
	private TextView txt;
	private int y=1,x=3;
	int flag=0;
	float my;
	float mx;
	int height;
	int width;	
	private GestureDetectorCompat gDetector;
	TextView tvProgress;
	Dialog d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,        
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// android boilerplate stuff
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		aprLevelsSeries = new SimpleXYSeries("APR Levels");
		aprLevelsSeries.useImplicitXVals();
		// get handles to our View defined in layout.xml:
		dynamicPlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
		rollHistorySeries = new SimpleXYSeries("Roll");
		rollHistorySeries.useImplicitXVals();
		//bt=new BTInterface();
		d = new Dialog(this);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		//tvProgress = (TextView)findViewById(R.id.tvProgess);
		//tvProgress.setText("Switching on bluetooth");
		d.setContentView(R.layout.progressdialog);
		d.show();
		//connect();
		back=(Button)findViewById(R.id.back);
		hold=(ToggleButton)findViewById(R.id.toggleButton1);
		this.gDetector = new GestureDetectorCompat(this,this);


		dynamicPlot.setDomainBoundaries(0, 3, BoundaryMode.FIXED);
		dynamicPlot.addSeries(rollHistorySeries, new LineAndPointFormatter(Color.rgb(243, 155, 9), null, null, null));
		dynamicPlot.setDomainStepValue(5);
		dynamicPlot.setTicksPerRangeLabel(3);
		dynamicPlot.setDomainLabel("Sample Index");
		dynamicPlot.getDomainLabelWidget().pack();
		dynamicPlot.setRangeLabel("Angle (Degs)");
		dynamicPlot.getRangeLabelWidget().pack();
		dynamicPlot.getGraphWidget().getBackgroundPaint().setColor(Color.rgb(23	, 160, 134));
		dynamicPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.rgb(41, 127, 184));

		// setup checkboxes:

		final PlotStatistics levelStats = new PlotStatistics(1000, false);
		final PlotStatistics histStats = new PlotStatistics(1000, false);


		dynamicPlot.addListener(histStats);
		// only display whole numbers in domain labels

		height=dynamicPlot.getLayoutParams().height;
		width=dynamicPlot.getLayoutParams().width;


		dynamicPlot.setGridPadding(5, 0, 5, 0);

		//bt.connect();


		// thin out domain/range tick labels so they dont overlap each other:



		// freeze the range boundaries:
		dynamicPlot.setRangeBoundaries(-y, y, BoundaryMode.FIXED);

		// kick off the data generating thread:

		//thread.start();


	}



	public void ampInc(View v){
		if(y==1)
			y=2;
		else if(y==2)
			y=5;
		else if(y==5)
			y=10;
		else if(y>=10 && y<100){
			y=y/10;
			if(y==1)
				y=20;
			if(y==2)
				y=50;
			if(y==5)
				y=100;
		}
		else if(y>=100 && y<1000){
			y=y/100;
			if(y==1)
				y=200;
			if(y==2)
				y=500;
			if(y==5)
				y=1000;
		}
		else if(y>=1000 && y<10000){
			y=y/1000;
			if(y==1)
				y=2000;
			if(y==2)
				y=5000;
			if(y==5)
				y=10000;
		}
		else if(y>=10000)
			y=10000;
		else
			y=1;


		dynamicPlot.setRangeBoundaries(-y,y, BoundaryMode.FIXED);

	}
	public void ampDec(View v){
		if(y==1)
			y=1;
		if(y==2)
			y=1;
		if(y==5)
			y=2;
		if(y==10)
			y=5;
		if(y>10 && y<=50){
			y=y/10;
			if(y==1)
				y=5;
			if(y==2)
				y=10;
			if(y==5)
				y=20;
		}
		if(y>=100 && y<=500){
			y=y/100;
			if(y==1)
				y=50;
			if(y==2)
				y=100;
			if(y==5)
				y=200;
		}
		if(y>=1000 && y<=5000){
			y=y/1000;
			if(y==1)
				y=500;
			if(y==2)
				y=1000;
			if(y==5)
				y=2000;
		}
		if(y>=10000)
			y=5000;

		dynamicPlot.setRangeBoundaries(-y, y, BoundaryMode.FIXED);

	}
	public void timeInc(View v){
		if(x>300)
			HISTORY_SIZE=x;
		x=x+10;
		dynamicPlot.setDomainBoundaries(0, x, BoundaryMode.FIXED);

	}
	public void timeDec(View v){
		if(x<0)
			x=0;
		else
			x=x-10;
		dynamicPlot.setDomainBoundaries(0, x, BoundaryMode.FIXED);
	}



	@SuppressWarnings("deprecation")
	public void toggleHold(View view) throws InterruptedException{
		// Is the toggle on?
		boolean on = ((ToggleButton) view).isChecked();

		if (on) {


			//wait();
			wait = true;

		} else {
			//notifyAll();
			wait = false;

		}

	}

	volatile boolean  wait = false;
	volatile boolean waitSnap = false;

	public void snap(View view) {
		final int height;
		final int width;
		waitSnap = true;
		height=dynamicPlot.getHeight();
		width=dynamicPlot.getWidth();

		dynamicPlot.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// change image

				Bitmap bitmap = takeScreenshot();
				saveBitmap(bitmap);




				dynamicPlot.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
				waitSnap = false;
			}

		}, 1000); 


	}
	public Bitmap takeScreenshot() {
		View rootView = findViewById(android.R.id.content).getRootView();
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}

	public void saveBitmap(Bitmap bitmap) {
		File graphshotsDirectory = new File("/sdcard/Graphshot/");
		// have the object build the directory structure, if needed.
		graphshotsDirectory.mkdirs();
		File imagePath = new File(Environment.getExternalStorageDirectory() + "/Graphshot/screenshot9.png");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(imagePath);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			Log.e("GREC", e.getMessage(), e);
		} catch (IOException e) {
			Log.e("GREC", e.getMessage(), e);
		}
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		float x,y;	



		x=dynamicPlot.getX();
		y=dynamicPlot.getY();
		if(mx<=x+dynamicPlot.getWidth() && my<=y+dynamicPlot.getHeight()){


			dynamicPlot.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			back.setVisibility(View.VISIBLE);
		}


		// TODO Auto-generated method stub
		// Implement code here!!!

		return true;
	}
	public void back(View v){

		dynamicPlot.setLayoutParams(new RelativeLayout.LayoutParams(width,height));
		back.setVisibility(View.INVISIBLE);
	}

	@Override 
	public boolean onTouchEvent(MotionEvent event) { 
		this.gDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation

		mx=event.getX();
		my=event.getY();
		return super.onTouchEvent(event);
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return true;
	}
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return true;
	}


	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try{
			closeBT();

		}catch(IOException e){

		}
		super.onDestroy();
	}



	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice = null;
	OutputStream mmOutputStream;
	InputStream mmInputStream;
	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;
	volatile double currentValue = 0;

	@SuppressLint("NewApi")
	public void connect()
	{
		System.out.println("Yo about to find");
		findBT();

		while(1 < 2)
		{
			try 
			{

				System.out.println("Yo about to open");
				openBT();
			}
			catch (IOException ex) 
			{
				ex.printStackTrace();
			}
			if(mmSocket.isConnected())
			{
				break;
			}
		}
	}

	void findBT()
	{
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null)
		{
			//myLabel.setText("No bluetooth adapter available");
		}

		if(!mBluetoothAdapter.isEnabled())
		{

			mBluetoothAdapter.enable();
			while(!mBluetoothAdapter.isEnabled());
		}
		//tvProgress.setText("Searching paired devices for ANDROID DSO");
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if(pairedDevices.size() > 0)
		{
			for(BluetoothDevice device : pairedDevices)
			{
				if(device.getName().equals("ANDROID DSO")) 
				{
					mmDevice = device;

					//Toast.makeText(this, "Found", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}
		if(mmDevice == null)
		{
			//Toast.makeText(this, "Device not found", Toast.LENGTH_SHORT).show();
		}
	}


	@SuppressLint("NewApi")
	void openBT() throws IOException
	{
		//tvProgress.setText("Connecting to ANDROID DSO");
		System.out.println("Yo in open");
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID


		mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);        
		mmSocket.connect();
		if(mmSocket.isConnected() == false)
		{
			//Toast.makeText(getApplicationContext(), "getting socket failed, try again",Toast.LENGTH_SHORT).show();
			System.out.println("Yo socket not connected");
		}
		else
		{
			//Toast.makeText(getApplicationContext(), "got socket",Toast.LENGTH_SHORT).show();
			System.out.println("Yo socket connected");
		}
		mmOutputStream = mmSocket.getOutputStream();
		mmInputStream = mmSocket.getInputStream();

		beginListenForData();
		d.dismiss();

		//myLabel.setText("Bluetooth Opened");
	}

	void beginListenForData()
	{

		//final Handler handler = new Handler(); 
		final byte delimiter = 10; //This is the ASCII code for a newline character

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable()
		{
			public void run()
			{                
				while(!Thread.currentThread().isInterrupted() && !stopWorker)
				{
					try 
					{

						int bytesAvailable = mmInputStream.available();                        
						if(bytesAvailable > 0)
						{
							byte[] packetBytes = new byte[bytesAvailable];
							mmInputStream.read(packetBytes);
							for(int i=0;i<bytesAvailable;i++)
							{
								byte b = packetBytes[i];
								if(b == delimiter)
								{
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
									final String data = new String(encodedBytes, "US-ASCII");
									readBufferPosition = 0;


									try
									{

										if(wait != true && waitSnap != true)
										{
											currentValue = Double.parseDouble(data);

											Number[] series1Numbers = {currentValue};
											aprLevelsSeries.setModel(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);

											if (rollHistorySeries.size() > HISTORY_SIZE) 
											{
												rollHistorySeries.removeFirst();

											}

											rollHistorySeries.addLast(null, currentValue);

											count = (count + 1)%10; 
											if(count == 0)
											{
												dynamicPlot.redraw();
											}
										}
									}
									catch(NumberFormatException NFE)
									{Log.d("exception","exp");	
									NFE.printStackTrace();
									}
								}
								else
								{
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					} 
					catch (IOException ex) 
					{
						stopWorker = true;
					}
				}
			}
		});

		workerThread.start();
	}


	void sendData(String msg) throws IOException
	{
		msg += "\n";
		mmOutputStream.write(msg.getBytes());
		//myLabel.setText("Data Sent");
	}

	void closeBT() throws IOException
	{
		stopWorker = true;
		mmOutputStream.close();
		mmInputStream.close();
		mmSocket.close();
		//myLabel.setText("Bluetooth Closed");
		//finish();
	}
}