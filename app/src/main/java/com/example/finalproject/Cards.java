package com.example.finalproject;

public class Cards {
    private String company;
    private String salary;
    private String desc;

    public Cards(String company, String salary, String desc) {
        this.company = company;
        this.salary = salary;
        this.desc = desc;
    }

    public String getCompany() {
        return company;
    }

    public String getSalary() {
        return salary;
    }

    public String getDesc() {
        return desc;
    }
}
