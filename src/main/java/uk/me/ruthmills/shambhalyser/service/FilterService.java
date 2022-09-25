package uk.me.ruthmills.shambhalyser.service;

import uk.me.ruthmills.shambhalyser.model.DataSet;

public interface FilterService {

	public DataSet lowPassFilter(DataSet input);
}
