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


public class TiltView extends View
{
	private Paint bckgrd = null;
	private Paint typewriter = null;
	private Paint brush = null;
	
	private TiltModel model = null;

	public TiltView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.setClickable(true);
		this.setKeepScreenOn(true);
		
		this.bckgrd = new Paint();
		this.bckgrd.setColor(Color.rgb(200, 200, 200));
		
		this.typewriter = new Paint();
		this.typewriter.setColor(Color.rgb(10, 10, 10));
		
		this.brush = new Paint();
		this.brush.setColor(Color.rgb(10, 178, 10));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		boolean b = false;
		
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			b = true;
			
			this.model.active = false;
		}
		
		return b;
	}
	
	@Override
	public void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas);
		
		int h = canvas.getHeight();
		int w = canvas.getWidth();
		canvas.drawRect(0, 0, w, h, this.bckgrd);
		if (this.model != null) 
		{
			canvas.drawText("x : " + this.model.ball_x, 10, 15, this.typewriter);
			canvas.drawText("y : " + this.model.ball_y, 10, 30, this.typewriter);
			
			canvas.drawCircle((w/2) + this.model.ball_x, (h/2) + this.model.ball_y, 10, this.brush);
		}
	}
	
	public void setModel(TiltModel model) {
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
