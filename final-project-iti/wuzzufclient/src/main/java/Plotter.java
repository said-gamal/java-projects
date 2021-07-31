import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.util.List;
import java.util.Map;

public class Plotter {
    // draw category graph
    static void graphCategoryChart(List<Integer> values, List<String> labels, String title, String xTitle, String yTitle) {
        CategoryChart chart = new CategoryChartBuilder()
                .title(title)
                .xAxisTitle(xTitle)
                .yAxisTitle(yTitle)
                .build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.getStyler().setStacked(true);
        chart.addSeries(title, labels, values);
        new SwingWrapper(chart).displayChart();
    }

    // draw pie chart
    static void graphPieChart(Map<String, Integer> values, String title) {
        PieChart chart = new PieChartBuilder()
                .title(title)
                .build();
        values.forEach(chart::addSeries);
        new SwingWrapper(chart).displayChart();
    }
}
