package com.webservice.softicamall;

public class CategoryModel {

    private String categoryIonLink, categoryName;

    public CategoryModel(String categoryIonLink, String categoryName) {
        this.categoryIonLink = categoryIonLink;
        this.categoryName = categoryName;
    }

    public String getCategoryIonLink() {
        return categoryIonLink;
    }

    public void setCategoryIonLink(String categoryIonLink) {
        this.categoryIonLink = categoryIonLink;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
