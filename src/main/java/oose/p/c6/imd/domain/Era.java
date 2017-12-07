package oose.p.c6.imd.domain;

public class Era extends Model {
    private String name;

    public Era(int id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
