package DragResizer;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

/**
 * {@link DragResizer} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 * Add a search of the region parent to have the correct position in scene of the mouse,
 * without this position the start of isInDraggableZone() is never > of the max Width of
 * the region that you pass through the DragResizer.makeResizable().
 * <p>
 * Height and Width resizing is working (hopefully) properly
 *
 * <pre>
 * DragResizer.makeResizable(myAnchorPane);
 * </pre>
 *
 * @author Vinsub (modified from the original DragResizerXY created by Cannibalsticky modified from the original DragResizer created byAndyTill)
 *
 */
public class DragResizer {

    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private static final int RESIZE_MARGIN = 10;

    private final Region region;
    private static Region outScene;

    private double y;

    private double x;

    private boolean initMinHeight;

    private boolean initMinWidth;

    private boolean draggableZoneX, draggableZoneY;

    private boolean dragging;

    private DragResizer(Region aRegion) {
        region = aRegion;
    }

    public static void makeResizable(Region region) {
        final DragResizer resizer = new DragResizer(region);
        outScene = region;//.getParent().getParent();
        while(outScene.getParent()!=null){
            outScene = (Region)outScene.getParent();
        }

        outScene.setOnMouseMoved((event)-> {
               //System.out.println((event.getSceneX())); //debug position
               resizer.mouseOver(event);
        });
        outScene.setOnMousePressed((event)-> resizer.mousePressed(event));
        outScene.setOnMouseDragged((event)-> resizer.mouseDragged(event));
        outScene.setOnMouseReleased((event)-> resizer.mouseReleased(event));

        region.setOnMousePressed((event)-> resizer.mousePressed(event));
        region.setOnMouseDragged((event)-> resizer.mouseDragged(event));
        region.setOnMouseMoved((event)-> {
                //System.out.println((event.getSceneX())); //debug position
                resizer.mouseOver(event);
        });
        region.setOnMouseReleased(event -> resizer.mouseReleased(event));
    }

    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if (isInDraggableZone(event) || dragging) {
            if (draggableZoneY) {
                region.setCursor(Cursor.S_RESIZE);
                outScene.setCursor(Cursor.S_RESIZE);
            }

            if (draggableZoneX) {
                region.setCursor(Cursor.E_RESIZE);
                outScene.setCursor(Cursor.E_RESIZE);
            }

        } else {
            region.setCursor(Cursor.DEFAULT);
            outScene.setCursor(Cursor.DEFAULT);
        }
    }

//    protected boolean isInDraggableZone(MouseEvent event) {
//        return (event.getY() > (region.getHeight() - RESIZE_MARGIN) ||
//                event.getY() < (region.getHeight() + RESIZE_MARGIN));
//    }
    //had to use 2 variables for the controll, tried without, had unexpected behaviour (going big was ok, going small nope.)
    protected boolean isInDraggableZone(MouseEvent event) {
        draggableZoneY = (event.getSceneY() > (region.getHeight() - RESIZE_MARGIN));//||(event.getY() < (region.getHeight() + RESIZE_MARGIN));
        draggableZoneX = (event.getSceneX() > (region.getWidth() - RESIZE_MARGIN-10))&&(event.getSceneX() < (region.getWidth() + RESIZE_MARGIN));
        return (draggableZoneY || draggableZoneX);
    }

    protected void mouseDragged(MouseEvent event) {
        if (!dragging) {
            return;
        }

        if (draggableZoneY) {
            double mousey = event.getSceneX();

            double newHeight = region.getMinHeight() + (mousey - y);

            region.setMinHeight(newHeight);

            y = mousey;
        }

        if (draggableZoneX) {
            double mousex = event.getSceneX();

            double newWidth = region.getMinWidth() + (mousex - x);

            region.setMinWidth(newWidth);

            x = mousex;

        }

    }

    protected void mousePressed(MouseEvent event) {

        // ignore clicks outside of the draggable margin
        if (!isInDraggableZone(event)) {
            return;
        }

        dragging = true;

        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            initMinHeight = true;
        }

        y = event.getY();

        if (!initMinWidth) {
            region.setMinWidth(region.getWidth());
            initMinWidth = true;
        }

        x = event.getX();
    }
}