package uk.me.ruthmills.shambhalyser.model;

public class DataPoint {
	private double time;
	private double linearAccelerationX;
	private double linearAccelerationY;
	private double linearAccelerationZ;
	private double absoluteAcceleration;

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getLinearAccelerationX() {
		return linearAccelerationX;
	}

	public void setLinearAccelerationX(double linearAccelerationX) {
		this.linearAccelerationX = linearAccelerationX;
	}

	public double getLinearAccelerationY() {
		return linearAccelerationY;
	}

	public void setLinearAccelerationY(double linearAccelerationY) {
		this.linearAccelerationY = linearAccelerationY;
	}

	public double getLinearAccelerationZ() {
		return linearAccelerationZ;
	}

	public void setLinearAccelerationZ(double linearAccelerationZ) {
		this.linearAccelerationZ = linearAccelerationZ;
	}

	public double getAbsoluteAcceleration() {
		return absoluteAcceleration;
	}

	public void setAbsoluteAcceleration(double absoluteAcceleration) {
		this.absoluteAcceleration = absoluteAcceleration;
	}
}
