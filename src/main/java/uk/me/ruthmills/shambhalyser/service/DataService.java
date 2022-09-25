package uk.me.ruthmills.shambhalyser.service;

import java.io.IOException;

import uk.me.ruthmills.shambhalyser.model.DataSet;

public interface DataService {

	public DataSet readDataSet(String name) throws IOException;

	public void writeDataSet(String name, int startTime, int endTime, DataSet dataSet) throws IOException;
}
