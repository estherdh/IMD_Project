package oose.p.c6.imd.domain;

public class Replica extends Model{
    private int exhibitId;
    private int price;
    private String sprite;
    private int type;
    private int position;
    private int year;
    private String title;
    private String era;

    public Replica(int id, int exhibitId, int price, String sprite, int type) {
        super(id);
        this.exhibitId = exhibitId;
        this.price = price;
        this.sprite = sprite;
        this.type = type;
    }

    public Replica(int id, int exhibitId, int price, String sprite, int type, int position, int year, String title, String era) {
        super(id);
        this.exhibitId = exhibitId;
        this.price = price;
        this.sprite = sprite;
        this.type = type;
        this.position = position;
        this.year = year;
        this.title = title;
        this.era = era;
    }

    public int getExhibitId() {
        return exhibitId;
    }

    public void setExhibitId(int exhibitId) {
        this.exhibitId = exhibitId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEra() {
        return era;
    }

    public void setEra(String era) {
        this.era = era;
    }
}
