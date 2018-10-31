package bgu.spl181.net.impl.Objects;

public class MovieInJson {

    //fields
    String id;
    String name;
    String price;
    String[] bannedCountries;
    String availableAmount;
    String totalAmount;

    //was required after the change of int to string in the json
    //constructor
    public MovieInJson(MovieInDB movie) {
        this.id = String.valueOf(movie.id);
        this.name = String.valueOf(movie.name);
        this.price = String.valueOf(movie.price);
        this.bannedCountries = movie.bannedCountries;
        this.availableAmount = String.valueOf(movie.availableAmount);
        this.totalAmount = String.valueOf(movie.totalAmount);
    }

    //getters and setters
    public String getId() {
        return (id);
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return (price);
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String[] getBannedCountries() {
        return bannedCountries;
    }
    public void setBannedCountries(String[] bannedCountries) {
        this.bannedCountries = bannedCountries;
    }
    public String getAvailableAmount() {
        return availableAmount;
    }
    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }
    public String getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


}