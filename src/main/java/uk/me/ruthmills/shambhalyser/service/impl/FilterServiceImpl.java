package uk.me.ruthmills.shambhalyser.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.psambit9791.jdsp.filter.Butterworth;

import uk.me.ruthmills.shambhalyser.model.DataPoint;
import uk.me.ruthmills.shambhalyser.model.DataSet;
import uk.me.ruthmills.shambhalyser.service.FilterService;

@Service
public class FilterServiceImpl implements FilterService {

	public DataSet lowPassFilter(DataSet input) {
		List<DataPoint> dataPoints = input.getDataPoints();
		int size = dataPoints.size();
		double[] linearAccelerationX = new double[size];
		double[] linearAccelerationY = new double[size];
		double[] linearAccelerationZ = new double[size];
		double[] absoluteAcceleration = new double[size];
		for (int i = 0; i < size; i++) {
			linearAccelerationX[i] = 0d - dataPoints.get(i).getLinearAccelerationX();
			linearAccelerationY[i] = 0d - dataPoints.get(i).getLinearAccelerationY();
			linearAccelerationZ[i] = 0d - dataPoints.get(i).getLinearAccelerationZ();
			absoluteAcceleration[i] = dataPoints.get(i).getAbsoluteAcceleration();
		}
		linearAccelerationX = filter(linearAccelerationX);
		linearAccelerationY = filter(linearAccelerationY);
		linearAccelerationZ = filter(linearAccelerationZ);
		absoluteAcceleration = filter(absoluteAcceleration);

		List<DataPoint> results = new ArrayList<>();
		for (int i = 0; i < dataPoints.size(); i++) {
			results.add(new DataPoint(dataPoints.get(i).getTime(), linearAccelerationX[i], linearAccelerationY[i],
					linearAccelerationZ[i], absoluteAcceleration[i]));
		}

		return new DataSet(results);
	}

	private double[] filter(double[] input) {
		int Fs = 200; // Sampling Frequency in Hz
		int order = 4; // order of the filter
		int cutOff = 1; // Cut-off Frequency
		Butterworth flt = new Butterworth(Fs);
		return flt.lowPassFilter(input, order, cutOff); // get the result after filtering
	}
}
