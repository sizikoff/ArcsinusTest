package com.testapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class People implements Serializable {
    public String name;
    public String height;
    public String mass;

    @SerializedName("birth_year")
    public String birthYear;
    public String gender;

    @SerializedName("hair_color")
    public String hairColor;

    @SerializedName("skin_color")
    public String skinColor;

    @Override
    public String toString(){
        return name + "\n"
                + "Birth Year: " + birthYear +"\n"
                + "Gender: " + gender + "\n"
                + "Hair color: " + hairColor +"\n"
                + "Height: " + height + "\n"
                + "Mass: " + mass + "\n"
                + "Skin color: " + skinColor + " \n";
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}