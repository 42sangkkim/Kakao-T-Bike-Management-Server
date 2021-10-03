package Kakao_T_Bike_Management.Service;

public class Command {

    private int truckId;
    private int action;

    public Command(int truckId, int action) {
        this.truckId = truckId;
        this.action = action;
    }

    public int getTruckId() {
        return truckId;
    }

    public int getAction() {
        return action;
    }
}
