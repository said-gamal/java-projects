import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.Collectors;

public class HttpRequestClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        // get popular companies from the restful server as a string json
        System.out.println("Loading popular companies ...");
        HttpResponse<String> companyJobs = request("http://localhost:8080/companyJobs");
        // get popular jobs from the restful server as a string json
        System.out.println("Loading popular jobs ...");
        HttpResponse<String> popularJobs = request("http://localhost:8080/popularJobs");
        // get popular areas from the restful server as a string json
        System.out.println("Loading popular areas ...");
        HttpResponse<String> popularAreas = request("http://localhost:8080/popularAreas");
        // get all skills from the restful server as a string json
        System.out.println("Loading popular skills ...");
        HttpResponse<String> allSkills = request("http://localhost:8080/allSkills");
        // get factorized YearExp from the restful server as a string json
        System.out.println("Loading factorized YearExp ...");
        HttpResponse<String> factorizedYearExp = request("http://localhost:8080/factorized");

        // convert json to map and plot the data
        System.out.println("============================popular companies============================");
        EDA(companyJobs.body(),"Top 10 demanding companies in Egypt");

        System.out.println("============================popular jobs============================");
        EDA(popularJobs.body(),"Top 10 popular jobs in Egypt");

        System.out.println("============================popular areas============================");
        EDA(popularAreas.body(),"Top 10 demanding areas for jobs in Egypt");

        System.out.println("============================popular skills============================");
        EDA(allSkills.body(),"Top 10 required skills in Egypt");

        System.out.println("============================factorized YearExp============================");
        printFactorizedYearsExp(factorizedYearExp.body());
    }

    static HttpResponse<String> request(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    static Map<String, Integer> jsonToMap(String json){
        Map<String, Integer> resultMap = new LinkedHashMap<>();
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        for(String key:jsonObject.keySet()){
            resultMap.put(key, Integer.parseInt(jsonObject.get(key).toString()));
        }
        return resultMap;
    }

    static void printFactorizedYearsExp(String json){
        JsonArray jsonArray = JsonParser.parseString(json).getAsJsonArray();
        for (JsonElement element:jsonArray){
            System.out.println(element.getAsJsonObject());
        }
    }

    static void EDA(String jsonString, String chartTitle){
        Map<String, Integer> dataMap = jsonToMap(jsonString);
        dataMap.forEach((key, value) -> System.out.println(key + " : " + value));
        dataMap = dataMap.entrySet().stream().limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Plotter.graphPieChart(dataMap, chartTitle);
        List<Integer> values = new ArrayList<>(dataMap.values());
        List<String> keys = new ArrayList<>(dataMap.keySet());
        Plotter.graphCategoryChart(values, keys, chartTitle, "Feature", "Count");
    }
}
