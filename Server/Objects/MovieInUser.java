package bgu.spl181.net.impl.Objects;

public class MovieInUser {




    private int id;
    private String name;

    public MovieInUser(int id , String name){
        this.id = id;
        this.name = name;
    }

    //getters and setters
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MovieInUser) {
            MovieInUser mov = (MovieInUser) o;
            return name.equals(mov.getName());
        }
       return false;
    }
    @Override
    public int hashCode() {
        return name.hashCode() + id;
    }
}
