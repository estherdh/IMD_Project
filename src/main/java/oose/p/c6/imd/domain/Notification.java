package oose.p.c6.imd.domain;

public class Notification extends Model {
    private String time;
    private String text;
    private String read;

    public Notification(int id, String time, String text, String read) {
        super(id);
        this.time = time;
        this.text = text;
        this.read = read;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
