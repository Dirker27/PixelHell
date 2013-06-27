package com.toastercat.tiltdemo;

import com.example.tiltdemo.R;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity
{
	private TiltView  view   = null;
	private DebugView debug  = null;
	private TiltModel model  = null;
	private Thread    updateThread = null;
	
	// tilt?
	private SensorManager sensorManager = null;
	
	final float[] mValuesMagnet      = new float[3];
    final float[] mValuesAccel       = new float[3];
    final float[] mValuesOrientation = new float[3];
    final float[] mRotationMatrix    = new float[9];

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tilt);
		
		// LINK M-V-C
		this.view = (TiltView) findViewById(R.id.tilt_view_primary);
		this.debug = (DebugView) findViewById(R.id.debug_view_primary);
		this.model = new TiltModel();
		this.view.setModel(this.model);
		this.debug.setModel(this.model);
		
		// Prime Update Thread
		this.initThread();
		
		// Prime Accelerometer
		this.sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);      
		
		final SensorEventListener mEventListener = 
			new SensorEventListener()
			{
	            public void onAccuracyChanged(Sensor sensor, int accuracy) { }
	
				@Override
				public void onSensorChanged(SensorEvent event) 
				{
	                // TODO Auto-generated method stub
	                switch (event.sensor.getType()) {
	                case Sensor.TYPE_ACCELEROMETER:
	                    System.arraycopy(event.values, 0, mValuesAccel, 0, 3);
	                    break;

	                case Sensor.TYPE_MAGNETIC_FIELD:
	                    System.arraycopy(event.values, 0, mValuesMagnet, 0, 3);
	                    break;
	                }
	            }
			};
		this.setListners(sensorManager, mEventListener);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if (this.updateThread != null) {
			this.updateThread.start();
		}
	}
	
	
	// Register the event listener and sensor type.
    public void setListners(SensorManager sensorManager, SensorEventListener mEventListener)
    {
    	// Accelerometer (Tilt)
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 
                SensorManager.SENSOR_DELAY_GAME);
        // Magnetic Field (Compass Orientation)
        sensorManager.registerListener(mEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), 
                SensorManager.SENSOR_DELAY_GAME);
    }
	
	
	private void initThread() {
		// Set Update Thread
		Runnable runner = 
			new Runnable()
			{
				@Override
				public void run()
				{
					while (model.active)
					{
						try {
							Thread.sleep(30);
							model.relaySensors(mValuesAccel, mValuesMagnet);
							model.update();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					finish();
				}
			};
		this.updateThread = new Thread(runner);
	}

}
