package ch.picturex.events;

import ch.picturex.model.Direction;

public class EventFilterZoom {

    private Direction direction;

    public EventFilterZoom(Direction direction){
        this.direction=direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
