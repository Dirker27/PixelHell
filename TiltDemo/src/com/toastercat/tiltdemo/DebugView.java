package com.toastercat.tiltdemo;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DebugView extends View
{
	private long drawFrames = 0;
	
	private Paint bckgrd = null;
	private Paint typewriter = null;
	private TiltModel model = null;
	
	public DebugView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		this.bckgrd = new Paint();
		this.bckgrd.setColor(Color.rgb(178, 10, 10));
		
		this.typewriter = new Paint();
		this.typewriter.setColor(Color.rgb(10, 10, 10));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		boolean b = false;
		
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			b = true;
		}
		
		return b;
	}
	
	@Override
	public void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), this.bckgrd);
		
		this.drawFrames++;
		canvas.drawText("Frames:      " + this.drawFrames, 10, 20, this.typewriter);
		if (this.model != null) {
			canvas.drawText("d_x : " + this.model.theta, 10, 50, this.typewriter);
			canvas.drawText("d_y : " + this.model.phi, 10, 65, this.typewriter);
			canvas.drawText("d_? : " + this.model.rho, 10, 80, this.typewriter);
		}
	}	
	
	public void setModel(TiltModel model)
	{
		this.model = model;
		this.model.addObserver(new Overseer());
	}
	
	/**
	 * Overseer 
	 */
	private class Overseer
		implements Observer
	{
		/**
		 * Invalidates View, Sets To Re-Draw
		 */
		public void update(Observable observable, Object data)
		{
			postInvalidate();
		}
	}
}
