package uk.me.ruthmills.shambhalyser.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.shambhalyser.model.DataPoint;
import uk.me.ruthmills.shambhalyser.model.DataSet;
import uk.me.ruthmills.shambhalyser.service.DataService;

@Service
public class DataServiceImpl implements DataService {

	private static final Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

	@PostConstruct
	public void initialise() throws IOException {
		DataSet dataSet = readDataSet("Shambhala");
		logger.info("Number of data points: " + dataSet.getDataPoints().size());
	}

	@Override
	public DataSet readDataSet(String name) throws IOException {
		Path path = FileSystems.getDefault().getPath("src/main/resources/data/" + name, "Raw Data.csv");
		FileReader fileReader = new FileReader(path.toFile());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder().setHeader().build().parse(fileReader);
		List<DataPoint> dataPoints = StreamSupport.stream(records.spliterator(), false)
				.map(record -> new DataPoint(Double.parseDouble(record.get("Time (s)")),
						Double.parseDouble(record.get("Linear Acceleration x (m/s^2)")),
						Double.parseDouble(record.get("Linear Acceleration y (m/s^2)")),
						Double.parseDouble(record.get("Linear Acceleration z (m/s^2)")),
						Double.parseDouble(record.get("Absolute acceleration (m/s^2)"))))
				.collect(Collectors.toList());
		return new DataSet(dataPoints);
	}
}
