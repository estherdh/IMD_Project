package oose.p.c6.imd.domain;

public class Exhibit extends Model {
    private String name;
    private String description;
    private String video;
    private String image;
    private int year;
    public Exhibit (int id, String name, String description, String video, String image, int year){
        super(id);
        this.name = name;
        this.description = description;
        this.video = video;
        this.image = image;
        this.year = year;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}