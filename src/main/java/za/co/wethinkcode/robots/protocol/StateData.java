package za.co.wethinkcode.robots.protocol;

public class StateData {
    private int [] position;
    private String direction;
    private int shields;
    private int shots;
    private String status;

    public StateData() {}

    public StateData(int[] position, String direction, int shields, int shots, String status)  {
        this.position = position;
        this.direction = direction;
        this.shields = shields;
        this.shots = shots;
        this.status = status;
    }

    public int[] getPosition() {
        return position;
    }

    public String getDirection() {
        return direction;
    }

    public int getShots() {
        return shots;
    }

    public int getShields(){
        return shields;
    }

    public String getStatus() {
        return status;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setShields(int shields) {
        this.shields = shields;
    }

    public void setShots(int shots) {
        this.shots = shots;
    }

    public void setStatus(String status) {
      this.status = status;
    }
}


