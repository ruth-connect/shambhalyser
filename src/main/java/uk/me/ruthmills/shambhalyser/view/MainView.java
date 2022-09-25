package uk.me.ruthmills.shambhalyser.view;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
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

	private DataService dataService;
	private JFreeChart chart;

	private static final Logger logger = LoggerFactory.getLogger(MainView.class);

	@Autowired
	public MainView(DataService dataService) throws IOException {
		this.dataService = dataService;

		DataSet shambhala = dataService.readDataSet("Shambhala");
		List<DataPoint> dataPoints = shambhala.getDataPoints();

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (int i = 0; i < dataPoints.size(); i += 100) {
			double time = dataPoints.get(i).getTime();
			double accelerationX = 0;
			double accelerationY = 0;
			double accelerationZ = 0;
			int j = 0;
			for (j = i; j < i + 100 && j < shambhala.getDataPoints().size(); j++) {
				accelerationX += dataPoints.get(j).getLinearAccelerationX();
				accelerationY += dataPoints.get(j).getLinearAccelerationY();
				accelerationZ += dataPoints.get(j).getLinearAccelerationZ();
			}
			accelerationX = accelerationX / 100;
			accelerationY = accelerationY / 100;
			accelerationZ = accelerationZ / 100;
			logger.info("Time: " + time + ", x: " + accelerationX);
			if (time >= 120d && time < 200d) {
				dataset.addValue(accelerationX, "x", Double.toString(time));
				dataset.addValue(accelerationY, "y", Double.toString(time));
				dataset.addValue(accelerationZ, "z", Double.toString(time));
			}
		}

		chart = ChartFactory.createLineChart("Shambhala", "Time", "Acceleration", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinesVisible(false);

		JFreeChartWrapper wrapper = new JFreeChartWrapper(chart);

		final VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		layout.addComponentAsFirst(wrapper);
		add(layout);
	}
}
