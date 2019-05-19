package ch.picturex;

import de.muspellheim.eventbus.EventBus;

public class SingleEventBus {

    private static EventBus bus = null;

    private SingleEventBus(){}

    public static EventBus getInstance(){
        if (bus == null){
            bus = new EventBus();
        }
        return bus;
    }

}
