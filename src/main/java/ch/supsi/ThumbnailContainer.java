package ch.supsi;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class ThumbnailContainer extends VBox{

    private final ImageWrapper imageWrapper;

    ThumbnailContainer(ImageWrapper imageWrapper){
        // call super constructor
        super();
        this.imageWrapper = imageWrapper;
        // set style
        this.setMaxSize(110,110);
        this.setAlignment(Pos.CENTER);

        // set content
        this.getChildren().add(imageWrapper.getThumbnailImageView());
        this.getChildren().add(new Label(imageWrapper.getName()));

        // set tooltip
        Tooltip.install(this, new Tooltip(imageWrapper.getTooltipString()));

    }

    public ImageWrapper getImageWrapper(){ return imageWrapper; }

}
