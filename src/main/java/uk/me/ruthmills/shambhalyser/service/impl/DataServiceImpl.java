package uk.me.ruthmills.shambhalyser.service.impl;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import uk.me.ruthmills.shambhalyser.model.DataPoint;
import uk.me.ruthmills.shambhalyser.model.DataSet;
import uk.me.ruthmills.shambhalyser.service.DataService;

@Service
public class DataServiceImpl implements DataService {

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

	@Override
	public void writeDataSet(String name, int startTime, int endTime, DataSet dataSet) throws IOException {
		List<DataPoint> dataPoints = dataSet.getDataPoints();
		Path path = FileSystems.getDefault().getPath("src/main/resources/data/" + name, "Modified Data.csv");
		FileWriter fileWriter = new FileWriter(path.toFile());
		try (CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.builder()
				.setHeader("Time (s)", "Linear Acceleration x (m/s^2)", "Linear Acceleration y (m/s^2)",
						"Linear Acceleration z (m/s^2)", "Absolute acceleration (m/s^2)", "Velocity x (m/s)",
						"Velocity y (m/s)", "Velocity z (m/s)", "Position x (m)", "Position y (m)", "Position z (m)")
				.build())) {
			double velocityX = 0d;
			double velocityY = 0d;
			double velocityZ = 0d;
			double positionX = 0d;
			double positionY = 0d;
			double positionZ = 0d;
			for (int i = 0; i < dataPoints.size(); i++) {
				DataPoint now = dataPoints.get(i);
				double time = now.getTime();
				if (time >= (double) startTime && now.getTime() < (double) endTime && i < dataPoints.size() - 1) {
					double linearAccelerationX = now.getLinearAccelerationX();
					double linearAccelerationY = now.getLinearAccelerationY();
					double linearAccelerationZ = now.getLinearAccelerationZ();
					double absoluteAcceleration = now.getAbsoluteAcceleration();

					printer.printRecord(time, linearAccelerationX, linearAccelerationY, linearAccelerationZ,
							absoluteAcceleration, velocityX, velocityY, velocityZ, positionX, positionY, positionZ);

					double timeDelta = dataPoints.get(i + 1).getTime() - time;

					// Position: s = ut + 1/2 at 2
					positionX = positionX + (velocityX * timeDelta)
							+ (0.5d * linearAccelerationX * timeDelta * timeDelta);
					positionY = positionY + (velocityY * timeDelta)
							+ (0.5d * linearAccelerationY * timeDelta * timeDelta);
					positionZ = positionZ + (velocityZ * timeDelta)
							+ (0.5d * linearAccelerationZ * timeDelta * timeDelta);

					// Velocity: v = u + at
					velocityX = velocityX + (linearAccelerationX * timeDelta);
					velocityY = velocityY + (linearAccelerationY * timeDelta);
					velocityZ = velocityZ + (linearAccelerationZ * timeDelta);
				}
			}
		}
	}
}
