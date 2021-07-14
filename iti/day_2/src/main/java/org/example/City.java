package org.example;

public class City {

    private String cityId;
    private String cityName;
    private String countryCode;
    private String capital;
    private double population;
    private String continent;

    public City(String cityId, String cityName, String countryCode, String capital, double population, String continent) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.capital = capital;
        this.population = population;
        this.continent = continent;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(double population) {
        this.population = population;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    @Override
    public String toString() {
        return cityName;
    }

}
