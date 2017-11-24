package oose.p.c6.imd.domain;

public class Replica extends Model{
    private int exhibitInfoId;
    private int price;
    private String sprite;
    private String type;
    private int position;

    public Replica(int id, int exhibitInfoId, int price, String sprite, String type, int position) {
        super(id);
        this.exhibitInfoId = exhibitInfoId;
        this.price = price;
        this.sprite = sprite;
        this.type = type;
        this.position = position;
    }

    public int getExhibitInfoId() {
        return exhibitInfoId;
    }

    public void setExhibitInfoId(int exhibitInfoId) {
        this.exhibitInfoId = exhibitInfoId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
