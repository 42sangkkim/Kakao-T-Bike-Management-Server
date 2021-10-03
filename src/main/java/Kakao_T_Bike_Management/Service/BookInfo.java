package Kakao_T_Bike_Management.Service;

public class BookInfo {
    private int from, to, duration;

    public BookInfo(int from, int to, int duration) {
        this.from = from;
        this.to = to;
        this.duration = duration;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getDuration() {
        return duration;
    }
}
