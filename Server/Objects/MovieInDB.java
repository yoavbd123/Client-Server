package bgu.spl181.net.impl.Objects;

import bgu.spl181.net.impl.bidi.BBServer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MovieInDB {

    //fields
    int id;
    String name;
    int price;
    String[] bannedCountries;
    int availableAmount;
    int totalAmount;


    //constructor
    public MovieInDB(String name, int price, String[] bannedCountries, int availableAmount, int totalAmount) {

        int highestID = 0;
        Collection<MovieInDB> movies = BBServer.get_movies().values();
        for (MovieInDB movie : movies){
            highestID = Math.max(movie.id,highestID);
        }
        this.id = highestID+1;
        this.name = name;
        this.price = price;
        this.bannedCountries = bannedCountries;
        this.availableAmount = availableAmount;
        this.totalAmount = totalAmount;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String[] getBannedCountries() {
        return bannedCountries;
    }
    public void setBannedCountries(String[] bannedCountries) {
        this.bannedCountries = bannedCountries;
    }
    public int getAvailableAmount() {
        return availableAmount;
    }
    public void setAvailableAmount(int availableAmount) {
        this.availableAmount = availableAmount;
    }
    public int getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }


}
