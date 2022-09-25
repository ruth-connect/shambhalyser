package uk.me.ruthmills.shambhalyser.view;

import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import uk.me.ruthmills.shambhalyser.model.DataPoint;
import uk.me.ruthmills.shambhalyser.model.DataSet;
import uk.me.ruthmills.shambhalyser.service.DataService;

@Route
public class MainView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static final int SAMPLE_COUNT = 50;

	private DataService dataService;
	private JFreeChart chart;

	private static final Logger logger = LoggerFactory.getLogger(MainView.class);

	@Autowired
	public MainView(DataService dataService) throws IOException {
		this.dataService = dataService;

		DataSet shambhala = dataService.readDataSet("Shambhala");
		List<DataPoint> dataPoints = shambhala.getDataPoints();

		DefaultTableXYDataset xyDataset = new DefaultTableXYDataset();
		NumberAxis numberAxis = new NumberAxis("Acceleration");

		XYSeries seriesX = new XYSeries("x", false, false);
		XYSeries seriesY = new XYSeries("y", false, false);
		XYSeries seriesZ = new XYSeries("z", false, false);
		XYSeries seriesAbs = new XYSeries("Absolute", false, false);

		for (int i = 0; i < dataPoints.size(); i += SAMPLE_COUNT) {
			double time = dataPoints.get(i).getTime();
			double accelerationX = 0;
			double accelerationY = 0;
			double accelerationZ = 0;
			double accelerationAbs = 0;
			int j = 0;
			for (j = i; j < i + SAMPLE_COUNT && j < shambhala.getDataPoints().size(); j++) {
				accelerationX += dataPoints.get(j).getLinearAccelerationX();
				accelerationY += dataPoints.get(j).getLinearAccelerationY();
				accelerationZ += dataPoints.get(j).getLinearAccelerationZ();
				accelerationAbs += dataPoints.get(j).getAbsoluteAcceleration();
			}
			accelerationX = accelerationX / SAMPLE_COUNT;
			accelerationY = accelerationY / SAMPLE_COUNT;
			accelerationZ = accelerationZ / SAMPLE_COUNT;
			accelerationAbs = accelerationAbs / SAMPLE_COUNT;
			if (time >= 120d && time < 200d) {
				seriesX.add(time, accelerationX);
				seriesY.add(time, accelerationY);
				seriesZ.add(time, accelerationZ);
				seriesAbs.add(time, accelerationAbs);
			}
		}

		xyDataset.addSeries(seriesX);
		xyDataset.addSeries(seriesY);
		xyDataset.addSeries(seriesZ);
		xyDataset.addSeries(seriesAbs);

		chart = ChartFactory.createXYLineChart("Shambhala", "Time", "Acceleration", xyDataset, PlotOrientation.VERTICAL,
				true, false, false);

		JFreeChartWrapper wrapper = new JFreeChartWrapper(chart);

		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponentAsFirst(wrapper);
		add(layout);
	}
}
