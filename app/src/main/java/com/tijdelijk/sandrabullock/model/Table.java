package com.tijdelijk.sandrabullock.model;

public class Table {

    private String Moviename;
    private String Role;
    private String Year;

    public Table() {
    }

    public Table(String moviename, String role, String year) {
        Moviename = moviename;
        Role = role;
        Year = year;
    }

    public String getMoviename() {
        return Moviename;
    }


    public String getRole() {
        return Role;
    }


    public String getYear() {
        return Year;
    }

}
