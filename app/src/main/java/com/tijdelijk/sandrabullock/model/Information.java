package com.tijdelijk.sandrabullock.model;

public class Information {
    private String Tittle;
    private String Description;
    private String Image;
    private String Youtube;

    public Information() {
    }

    public Information(String tittle, String description, String image, String youtube) {
        Tittle = tittle;
        Description = description;
        Image = image;
        Youtube = youtube;
    }

    public String getYoutube() { return Youtube; }

    public String getTittle() {
        return Tittle;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }
}
