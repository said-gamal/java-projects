package com.example.wuzzufjobs;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ServerController {
    @Autowired
    private SparkSession spark;

    @Autowired
    private Dataset<Row> dataset;

    @GetMapping("/companyJobs")
    public Map<String, Integer> getCompanyJobs() {
        dataset.createOrReplaceTempView("Jobs");
        // Count the jobs for each company and display that in order (What are the most demanding companies for jobs?)
        Dataset<Row> companyJobs = spark.sql(
                "select Company, count(*) as Jobs_Count "
                        + "from Jobs "
                        + "group by Company "
                        + "order by Jobs_Count desc"
        );
        return convertDatasetToMap(companyJobs);
    }

    @GetMapping("/popularJobs")
    public Map<String, Integer> getPopularJobs() {
        dataset.createOrReplaceTempView("Jobs");
        // What are it the most popular job titles?
        Dataset<Row> popularJobs = spark.sql(
                "select Title, count(*) as Repetitions "
                        + "from Jobs "
                        + "group by Title "
                        + "order by Repetitions desc"
        );
        return convertDatasetToMap(popularJobs);
    }

    @GetMapping("/popularAreas")
    public Map<String, Integer> getPopularAreas() {
        dataset.createOrReplaceTempView("Jobs");
        // The most popular areas
        Dataset<Row> popularAreas = spark.sql(
                "select Location, count(*) as Repetitions "
                        + "from Jobs "
                        + "group by Location "
                        + "order by Repetitions desc"
        );
        return convertDatasetToMap(popularAreas);
    }

    @GetMapping("/allSkills")
    public Map<String, Integer> getallSkills() {
        dataset.createOrReplaceTempView("Jobs");
        // Skills one by one and how many each repeated
        Dataset<Row> allSkills = spark.sql(
                "select Skills "
                        + "from Jobs"
        );
        return convertSkillsToMap(allSkills);
    }

    @GetMapping("/factorized")
    public List<Map.Entry<String, Integer>> getFactorizedYearsExp() {
        dataset.createOrReplaceTempView("Jobs");
        // select YearsExp column
        Dataset<Row> yearsExp = spark.sql("select YearsExp from Jobs");
        // choose distinct values
        List<String> allValues = yearsExp.collectAsList()
                .stream()
                .map(Row::toString)
                .collect(Collectors.toList());
        List<String> distinctValues = allValues.stream().distinct().collect(Collectors.toList());
        // map every distinct value into unique integer
        Map<String, Integer> factoriziationMap = new LinkedHashMap<>();
        int i = 0;
        for (String value:distinctValues){
            factoriziationMap.put(value, i++);
        }
        // factorize all values
        List<Map.Entry<String, Integer>> factorizedList = new ArrayList<>();
        for(String value:allValues){
            factorizedList.add(Map.entry(value, factoriziationMap.get(value)));
        }
        return factorizedList;
    }

    Map<String, Integer> convertDatasetToMap(Dataset<Row> dataset) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Row row : dataset.collectAsList()) {
            String title = row.get(0).toString();
            int count = Integer.parseInt(row.get(1).toString());
            map.put(title, count);
        }
        return map;
    }

    Map<String, Integer> convertSkillsToMap(Dataset<Row> allSkills) {
        Map<String, Integer> skillsMap = new LinkedHashMap<>();
        Map<String, Integer> sortedSkillsMap = new LinkedHashMap<>();
        allSkills.collectAsList().forEach(row -> {
                    String line = row.toString()
                            .trim()
                            .replace("[", "")
                            .replace("]", "")
                            .toUpperCase();
                    for (String skill : line.split(", ")) {
                        if (skillsMap.containsKey(skill)) {
                            int counter = skillsMap.get(skill);
                            skillsMap.put(skill, counter + 1);
                        } else {
                            skillsMap.put(skill, 1);
                        }
                    }
                }
        );
        // sort skills map
        List<Map.Entry<String, Integer>> skillsList = skillsMap.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry->-entry.getValue()))
                .collect(Collectors.toList());
        for(Map.Entry<String, Integer> entry:skillsList){
            sortedSkillsMap.put(entry.getKey(), entry.getValue());
        }
        return sortedSkillsMap;
    }
}
