package org.example;

public class Pyramid {
    private String pharaoh;
    private String modernName;
    private String site;
    private double height;
    private String material;

    public Pyramid(String pharaoh, String modernName, String site, double height, String material) {
        this.pharaoh = pharaoh;
        this.modernName = modernName;
        this.site = site;
        this.height = height;
        this.material = material;
    }

    public String getPharaoh() {
        return pharaoh;
    }

    public void setPharaoh(String pharaoh) {
        this.pharaoh = pharaoh;
    }

    public String getModernName() {
        return modernName;
    }

    public void setModernName(String modernName) {
        this.modernName = modernName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return "Pyramid{" + "pharaoh=" + pharaoh + ", modernName=" + modernName + ", site=" + site + ", height=" + height + ", material=" + material + '}';
    }
}
