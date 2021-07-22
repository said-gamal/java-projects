package org.smile;

import org.apache.commons.csv.CSVFormat;
import smile.classification.RandomForest;
import smile.data.DataFrame;
import smile.data.formula.Formula;
import smile.data.measure.NominalScale;
import smile.data.vector.IntVector;
import smile.data.vector.StringVector;
import smile.io.Read;
import smile.plot.swing.Histogram;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static smile.math.MathEx.round;

public class SmileDemoEDA {
    private static final String trainFilePath = "src/main/resources/titanic_train.csv";
    private static final String testFilePath = "src/main/resources/titanic_test.csv";

    public static void main(String[] args) throws IOException, URISyntaxException {
        //preparing TrainData
        DataFrame trainData = prepareTrainData(trainFilePath);
        //Training model
        System.out.println("======================Training model========================");
        RandomForest model = RandomForest.fit(Formula.lhs("Survived"), trainData);
        System.out.println("feature importance:");
        System.out.println(Arrays.toString(model.importance()));
        System.out.println("model matrix: " + model.metrics());

        //TODO load test data to validate model
        //preparing TestData
        System.out.println("======================preparing TestData========================");
        DataFrame testData = prepareTestData(testFilePath);
        int[] result = model.predict(testData);
        Arrays.stream(result).forEach(System.out::print);
        double averageSurvivedTest = Arrays.stream(result).average().getAsDouble();
        System.out.println("\nAverage of Survived in TestData: " + round(averageSurvivedTest * 100, 2) + "%");
        System.out.println("model matrix: " + model.metrics());
    }

    public static int[] encodeColumn(DataFrame dataFrame, String columnName) {
        String[] distinctValues = dataFrame.stringVector(columnName).distinct().toArray(new String[]{});
        return dataFrame.stringVector(columnName).factorize(new NominalScale(distinctValues)).toIntArray();
    }

    private static void titanicEDA(DataFrame dataFrame) throws InterruptedException, InvocationTargetException {
        System.out.println("================================Titanic dataframe summary================================");
        dataFrame.summary();
        // split survived and not survived
        DataFrame titanicSurvived = DataFrame.of(dataFrame.stream().filter(value -> value.get("Survived").equals(1)));
        DataFrame titanicNotSurvived = DataFrame.of(dataFrame.stream().filter(value -> value.get("Survived").equals(0)));
        // omit null rows
        titanicNotSurvived = titanicNotSurvived.omitNullRows();
        titanicSurvived = titanicSurvived.omitNullRows();
        System.out.println("================================Survived dataframe summary================================");
        titanicSurvived.summary();
        System.out.println("================================NotSurvived dataframe summary================================");
        titanicNotSurvived.summary();
        // some stats
        System.out.println("Size of titanicSurvived: " + titanicSurvived.size());
        System.out.println("Size of titanicNotSurvived: " + titanicNotSurvived.size());
        Double averageAgeSurvived = titanicSurvived.stream()
                .mapToDouble(value -> value.isNullAt("Age") ? 0.0 : value.getDouble("Age"))
                .average()
                .orElse(0.0);
        System.out.println("Average age of titanicSurvived: " + averageAgeSurvived.intValue());
        Double averageAgeNotSurvived = titanicNotSurvived.stream()
                .mapToDouble(value -> value.isNullAt("Age") ? 0.0 : value.getDouble("Age"))
                .average()
                .orElse(0.0);
        System.out.println("Average age of titanicNotSurvived: " + averageAgeNotSurvived.intValue());
        // plot age histogram
        Histogram.of(titanicSurvived.doubleVector("Age").toDoubleArray(), 15, false)
                .canvas().setAxisLabels("Age", "Count")
                .setTitle("Age frequencies among surviving passengers")
                .window();
        Histogram.of(titanicNotSurvived.doubleVector("Age").toDoubleArray(), 15, false)
                .canvas().setAxisLabels("Age", "Count")
                .setTitle("Age frequencies among not surviving passengers")
                .window();
        // plot pclass histogram
        Histogram.of(titanicSurvived.intVector("PClassValues").toIntArray(), 4, true)
                .canvas().setAxisLabels("Classes", "Count")
                .setTitle("Pclass values frequencies among surviving passengers")
                .window();
        Histogram.of(titanicNotSurvived.intVector("PClassValues").toIntArray(), 4, true)
                .canvas().setAxisLabels("Classes", "Count")
                .setTitle("Pclass values frequencies among not surviving passengers")
                .window();
    }

    public static DataFrame prepareTrainData(String filePath) {
        PassengerProvider passengerProvider = new PassengerProvider();
        DataFrame trainData = passengerProvider.readCSV(filePath);
        // encode Sex column
        trainData = trainData.merge(IntVector.of("Gender", encodeColumn(trainData, "Sex")));
        // encode PClass column
        trainData = trainData.merge(IntVector.of("PClassValues", encodeColumn(trainData, "Pclass")));
        System.out.println("=======Encoding Non Numeric Data==============");
        System.out.println(trainData.structure());
        // drop Name, Pclass and Sex columns
        trainData = trainData.drop("Name");
        trainData = trainData.drop("Pclass");
        trainData = trainData.drop("Sex");
        System.out.println("=======After Dropping the Name, Pclass, and Sex Columns==============");
        System.out.println(trainData.structure());
        System.out.println(trainData.summary());
        // omit null rows
        trainData = trainData.omitNullRows();
        System.out.println("=======After Omitting null Rows==============");
        System.out.println(trainData.summary());

        System.out.println("=======Start of Explaratory Data Analysis==============");
        try {
            titanicEDA(trainData);
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return trainData;
    }

    public static DataFrame prepareTestData(String filePath) throws IOException, URISyntaxException {
        DataFrame testData = Read.csv(filePath, CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(',')).select("Name", "Pclass", "Sex", "Age");
        System.out.println(testData.structure());
        System.out.println("=====================================================================================================");
        System.out.println(testData.summary());
        System.out.println("=====================================================================================================");

        testData = testData.merge(StringVector.of("PClassValuesString", testData.intVector("Pclass").toStringArray()));
        testData = testData.merge(IntVector.of("Gender", encodeColumn(testData, "Sex")));
        testData = testData.merge(IntVector.of("PClassValues", encodeColumn(testData, "PClassValuesString")));
        System.out.println("=======Encoding Non Numeric Data==============");
        System.out.println(testData.structure());

        System.out.println("=======Dropping the Name, Pclass, and Sex Columns==============");
        testData = testData.drop("Name");
        testData = testData.drop("Pclass");
        testData = testData.drop("PClassValuesString");
        testData = testData.drop("Sex");

        testData = testData.omitNullRows();
        System.out.println("=======After Omitting null Rows==============");
        System.out.println(testData.structure());
        System.out.println(testData.summary());

        return testData;
    }
}