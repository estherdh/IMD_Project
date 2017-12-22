package oose.p.c6.imd.domain;

public class Exhibit extends Model {
    private String name;
    private String description;
    private String video;
    private String image;
    private int year;
    private int eraId;
    private int museumId;
    private Era era;
    public Exhibit (int id, String name, String description, String video, String image, int year, int eraId, int museumId){
        super(id);
        this.name = name;
        this.description = description;
        this.video = video;
        this.image = image;
        this.year = year;
        this.museumId = museumId;
        this.eraId = eraId;
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

    public int getEraId() {
        return eraId;
    }

    public void setEraId(int eraId) {
        this.eraId = eraId;
    }

    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
    }

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }
}
