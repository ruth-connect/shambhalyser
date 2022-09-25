package uk.me.ruthmills.shambhalyser.model;

import java.util.List;

public class DataSet {
	private List<DataPoint> dataPoints;

	public DataSet(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}

	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(List<DataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}
}
