package ch.picturex.events;

public class EventZoom {

    private String direction;

    public EventZoom(String text){
        this.direction =text;
    }

    public String getDirection() {
        return direction;
    }
}
