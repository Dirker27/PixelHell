package com.toastercat.tiltdemo;

import java.util.Observable;

public class TiltModel extends Observable
{
	protected float theta = 0f; // x-y plane
	protected float phi   = 0f; // y-z plane
	protected float rho   = 0f; // lateral rotation
	
	protected float yaw   = 0f;
	protected float pitch = 0f;
	protected float roll  = 0f;
	
	protected float[] tilt_data = {0f, 0f, 0f};
	protected float[] gravity   = {0f, 0f, 0f};
	protected float[] magnet    = {0f, 0f, 0f};
	
	protected float ball_x = 0f;
	protected float ball_y = 0f;	
	
	protected boolean active = true;
	
	public TiltModel() { }
	
	public void relaySensors(float[] mValuesAccel, float[] mValueMagnet) 
	{
		this.theta = mValuesAccel[0];
		this.phi   = mValuesAccel[1];
		this.rho   = mValuesAccel[2];
		
		this.ball_x -= this.theta;
		this.ball_y += this.phi;
	}
	
	public void update() 
	{ 	
		this.setChanged();
		this.notifyObservers();	
	}
}
