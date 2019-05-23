package ch.picturex.events;

public class EventFilterResize {
    int[] heightWidthPixel;

    public EventFilterResize(int[] heightWidthPixel){
        this.heightWidthPixel = heightWidthPixel;
    }

    public int[] getHeightWidthPixel() {
        return heightWidthPixel;
    }
}
