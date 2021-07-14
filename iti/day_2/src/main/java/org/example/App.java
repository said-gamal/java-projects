package org.example;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class App 
{
    public static void main(String[] args) throws IOException {
        String citiesFilePath = "src/main/resources/cities.csv";
        String countriesFilrPath = "src/main/resources/countries.csv";
        File citiesCsvFile = new File(citiesFilePath);
        CityDao citiesDao = new CityDao();
        File countriesCsvFile = new File(countriesFilrPath);
        CountryDao countriesDao = new CountryDao();
        List<City> cities = citiesDao.readFromCSV(citiesCsvFile);
        List<Country> countries = countriesDao.readFromCSV(countriesCsvFile);
        Map<String, List<City>> countriesMap = new HashMap<>();

        countries.forEach(country -> {
            List<City> countryCities = new ArrayList<>();
            cities.forEach(city -> {
                if (city.getCountryCode().equals(country.getCountryCode()))
                    countryCities.add(city);
            });
            countriesMap.put(country.getCountryCode(), countryCities);
        });

        System.out.println("press any key to print sorted cities of EG");
        System.in.read();
        countriesMap.get("EG")
                .stream()
                .sorted(Comparator.comparingDouble(City::getPopulation).reversed())
                .forEach(city -> System.out.println(city + " has population: " + city.getPopulation()));
        System.out.println("*******************************************************************************************");

        System.out.println("press any key to print highest population for every country");
        System.in.read();
        countries.forEach(country -> {
            List<City> countryCities = new ArrayList<>();
            cities.forEach(city -> {
                if (city.getCountryCode().equals(country.getCountryCode()))
                    countryCities.add(city);
            });
            if (countryCities.size() > 0) {
                City city = countryCities.stream().max(Comparator.comparingDouble(City::getPopulation)).get();
                System.out.println("Highest population city of " + country.toString() + " is " + city);
            }
        });
        System.out.println("*******************************************************************************************");

        System.out.println("press any key to print highest population for every continent");
        System.in.read();
        String[] continents = {"Asia", "Europe", "Africa", "Americas", "Oceania"};
        for (String continent : continents) {
            List<City> continentCities = new ArrayList<>();
            cities.forEach(city -> {
                if (city.getContinent().equals(continent))
                    continentCities.add(city);
            });
            if (continentCities.size() > 0) {
                City c = continentCities.stream().max(Comparator.comparingDouble(City::getPopulation)).get();
                System.out.println("Highest population city of " + continent + " is " + c);
            }
        }
        System.out.println("*******************************************************************************************");

        System.out.println("press any key to print highest population capital");
        System.in.read();
        City maxCapital = cities.stream()
                .filter(c -> c.getCapital().equals("primary"))
                .max(Comparator.comparingDouble(City::getPopulation))
                .get();
        System.out.println("Highest population capital city is: " + maxCapital);
    }
}
