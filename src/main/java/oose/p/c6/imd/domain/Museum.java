package oose.p.c6.imd.domain;

public class Museum extends Model {
    private String name;
    private String site;
    private String region;
    private String qrCode;

    public Museum(int id, String name, String site, String region) {
        super(id);
        this.name = name;
        this.site = site;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() {
        return qrCode;
    }
}
