package com.animelabs.asheeshsharma.dressupapp.Model;

/**
 * Created by Asheesh.Sharma on 05-12-2016.
 */
public class Combination {
    public String getShirtPath() {
        return shirtPath;
    }

    public void setShirtPath(String shirtPath) {
        this.shirtPath = shirtPath;
    }

    public String getTrouserPath() {
        return trouserPath;
    }

    public void setTrouserPath(String trouserPath) {
        this.trouserPath = trouserPath;
    }

    private String shirtPath;
    private String trouserPath;

    @Override
    public String toString() {
        return "Combination{" +
                "shirtPath='" + shirtPath + '\'' +
                ", trouserPath='" + trouserPath + '\'' +
                '}';
    }
}
