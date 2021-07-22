package org.Xchart;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XChartTitanic {
    public static void main(String[] args) {
        final String path = "src/main/resources/files/titanic.json";
        // get passengers from json file
        List<TitanicPassenger> allTitanicPassengers = getPassengersFromJsonFile(path);

        // list of passengers' ages (first 5)
        List<Float> pAges = allTitanicPassengers
                .stream()
                .map(TitanicPassenger::getAge)
                .limit(5)
                .collect(Collectors.toList());
        // list of passengers' fares (first 5)
        List<Float> pFares = allTitanicPassengers
                .stream()
                .map(TitanicPassenger::getFare)
                .limit(5)
                .collect(Collectors.toList());
        // list of passengers' names (first 5)
        List<String> pNames = allTitanicPassengers
                .stream()
                .map(TitanicPassenger::getName)
                .limit(5)
                .collect(Collectors.toList());
        // list of passengers' calss
        Map<String, Long> pClasses = allTitanicPassengers
                .stream()
                .collect(Collectors.groupingBy(TitanicPassenger::getPclass, Collectors.counting()));
        // list of passengers' gender
        Map<String, Long> pGenders = allTitanicPassengers
                .stream()
                .collect(Collectors.groupingBy(TitanicPassenger::getSex, Collectors.counting()));
        // list of passengers' embarked
        Map<String, Long> pEmbarked = allTitanicPassengers
                .stream()
                .collect(Collectors.groupingBy(TitanicPassenger::getEmbarked, Collectors.counting()));

        // graph passengers' names vs ages
        graphCategoryChart(pAges, pNames, "Sample of passengers ages", "Name", "Age");
        // graph passengers' names vs fares
        graphCategoryChart(pFares, pNames, "Sample of passengers fares", "Name", "Fare");
        // graph passengers' classes
        graphPieChart(pClasses, "Passengers classes");
        // graph passengers' genders
        graphPieChart(pGenders, "Passengers genders");
        // graph passengers' embarked
        graphPieChart(pEmbarked, "Passengers embarked");
    }

    // use Jacson to get titanic passengers objects from json file
    static List<TitanicPassenger> getPassengersFromJsonFile(String path) {
        List<TitanicPassenger> allPassengers = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (InputStream input = new FileInputStream(path)) {
            // read json file
            allPassengers = objectMapper.readValue(input, new TypeReference<List<TitanicPassenger>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allPassengers;
    }

    // draw category graph
    static void graphCategoryChart(List<Float> values, List<String> labels, String title, String xTitle, String yTitle) {
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
    static void graphPieChart(Map<String, Long> values, String title) {
        PieChart chart = new PieChartBuilder()
                .title(title)
                .build();
        values.forEach(chart::addSeries);
        new SwingWrapper(chart).displayChart();
    }
}
