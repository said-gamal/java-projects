package com.example.wuzzufjobs;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ServerConfigurer implements WebMvcConfigurer {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public SparkSession spark() {
        return SparkSession.builder()
                .appName("Wuzzuf Jobs")
                .master("local[2]")
                .getOrCreate();
    }

    @Bean
    public Dataset<Row> dataset() {
        DataFrameReader reader = spark().read();
        reader.option("header", "true");
        // read from csv file
        Dataset<Row> dataset = reader.csv("src/main/resources/Wuzzuf_Jobs.csv");
        // clean dataset
        return cleanDataset(dataset);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("landing-page");
    }

    private Dataset<Row> cleanDataset(Dataset<Row> dataset){
        // drop duplicates
        return dataset.dropDuplicates();
    }

    private Dataset<Row> factroizeColumn(Dataset<Row> dataset, String colName){
        return null;
    }
}