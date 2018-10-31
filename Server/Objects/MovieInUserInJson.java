package bgu.spl181.net.impl.Objects;

public class MovieInUserInJson {

    //was required after the change of int to string in the json
    private String id;
    private String name;

    public MovieInUserInJson(MovieInUser mov) {
        this.id = Integer.toString(mov.getId());
        this.name = mov.getName();
    }

    //getters and setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
