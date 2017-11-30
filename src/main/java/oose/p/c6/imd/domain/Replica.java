package oose.p.c6.imd.domain;

public class Replica extends Model{
    private int exhibitInfoId;
    private int price;
    private String sprite;
    private int type;

    public Replica(int id, int exhibitInfoId, int price, String sprite, int type) {
        super(id);
        this.exhibitInfoId = exhibitInfoId;
        this.price = price;
        this.sprite = sprite;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
