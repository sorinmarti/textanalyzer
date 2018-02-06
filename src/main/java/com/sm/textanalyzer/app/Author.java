package com.sm.textanalyzer.app;

public class Author {

    private String id;
    private String name;
    private int ageMin;
    private int ageMax;
    private int gender;

    public Author(String name) {
        this.name = name;
        this.id = String.valueOf(hashCode());
    }

    public static Integer[] getGenderArray() {
        return new Integer[]{0,1,2,3};
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(int ageMax) {
        this.ageMax = ageMax;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return name.hashCode()+new Integer(ageMin).hashCode()+new Integer(ageMax).hashCode()+new Integer(gender).hashCode();
    }
}
