package uk.me.ruthmills.shambhalyser.view;

import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import uk.me.ruthmills.shambhalyser.model.DataPoint;
import uk.me.ruthmills.shambhalyser.model.DataSet;
import uk.me.ruthmills.shambhalyser.service.DataService;
import uk.me.ruthmills.shambhalyser.service.FilterService;

@Route
public class MainView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static final int SAMPLE_COUNT = 10;

	private JFreeChart chart;

	@Autowired
	public MainView(DataService dataService, FilterService filterService) throws IOException {
		DataSet shambhala = filterService.lowPassFilter(dataService.readDataSet("Shambhala"));
		List<DataPoint> dataPoints = shambhala.getDataPoints();

		DefaultTableXYDataset xyDataset = new DefaultTableXYDataset();

		XYSeries seriesX = new XYSeries("x", false, false);
		XYSeries seriesY = new XYSeries("y", false, false);
		XYSeries seriesZ = new XYSeries("z", false, false);
		XYSeries seriesAbs = new XYSeries("Absolute", false, false);

		for (int i = 0; i < dataPoints.size(); i += SAMPLE_COUNT) {
			double time = dataPoints.get(i).getTime();
			double accelerationX = dataPoints.get(i).getLinearAccelerationX();
			double accelerationY = dataPoints.get(i).getLinearAccelerationY();
			double accelerationZ = dataPoints.get(i).getLinearAccelerationZ();
			double accelerationAbs = dataPoints.get(i).getAbsoluteAcceleration();
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

		chart = ChartFactory.createXYLineChart("Shambhala", "Time (s)", "Acceleration (m/s/s)", xyDataset,
				PlotOrientation.VERTICAL, true, false, false);

		JFreeChartWrapper wrapper = new JFreeChartWrapper(chart);

		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponentAsFirst(wrapper);
		add(layout);
	}
}
