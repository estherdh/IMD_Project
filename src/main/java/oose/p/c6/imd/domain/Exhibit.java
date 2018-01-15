package oose.p.c6.imd.domain;

import java.util.List;

public class Exhibit extends Model {
    private String name;
    private String description;
    private String video;
    private List<String> images;
    private String year;
    private int eraId;
    private int museumId;
    private Era era;

    public Exhibit (int id, String name, String description, String video, List<String> images, String year, int eraId, int museumId){
        super(id);
        this.name = name;
        this.description = description;
        this.video = video;
        this.images = images;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
