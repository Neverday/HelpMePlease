package com.helpmeplease.helpmeplease.Adapter;

/**
 * Created by Phattarapong on 28-May-17.
 */

public class Police {
    private String Id,Name,Location;

    public Police() {
    }

    public Police(String Id, String Name, String Location) {
        this.Name = Name;

        this.Id = Id;
        this.Location = Location;


    }
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

}
