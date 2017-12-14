package oose.p.c6.imd.domain;

public class Replica extends Model{
    private int exhibitId;
    private int price;
    private String sprite;
    private int type;
    private Exhibit exhibit;
    private int position;

    public Replica(int id, int exhibitId, int price, String sprite, int type) {
        super(id);
        this.exhibitId = exhibitId;
        this.price = price;
        this.sprite = sprite;
        this.type = type;
    }

    public Replica(int id, Exhibit exhibit, int price, String sprite, int type, int position) {
        super(id);
        this.exhibit = exhibit;
        this.price = price;
        this.sprite = sprite;
        this.type = type;
        this.position = position;
        this.exhibit = exhibit;
    }

    /*
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
    }*/

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

    public Exhibit getExhibit() {
        return exhibit;
    }

    public void setExhibit(Exhibit exhibit) {
        this.exhibit = exhibit;
    }
}
