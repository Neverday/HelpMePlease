package com.helpmeplease.helpmeplease.Adapter;

public class Check {
    private String Name,Phone,Category,Location,Text,Email,thumbnailUrl,Datenotify;


    public Check() {
    }

    public Check(String Name, String Type, String Phone, String Category, String Location, String Text,
                 String Id_user, String Email, String thumbnailUrl,String Datenotify) {
        this.Name = Name;

        this.thumbnailUrl = thumbnailUrl;
        this.Phone = Phone;
        this.Category = Category;
        this.Location = Location;
        this.Text = Text;
        this.Datenotify = Datenotify;
        this.Email = Email;

    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }
    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }
    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }


    public String getDatenotify() {
        return Datenotify;
    }

    public void setDatenotify(String Datenotify) {
        this.Datenotify = Datenotify;
    }


}