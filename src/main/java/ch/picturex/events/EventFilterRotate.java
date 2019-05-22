package ch.picturex.events;

import ch.picturex.model.Direction;

public class EventFilterRotate {

    private Direction direction;

    public EventFilterRotate(Direction direction){
        this.direction=direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
