package com.tijdelijk.sandrabullock.model;

public class Information {
    private String tittle;
    private String discription;
    private String image;

    public Information() {
    }

    public Information(String tittle, String discription, String image) {
        this.tittle = tittle;
        this.discription = discription;
        this.image = image;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
