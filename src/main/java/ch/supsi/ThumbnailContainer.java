package ch.supsi;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class ThumbnailContainer extends VBox{

    private final ImageView imageView;
    private final ImageWrapper imageWrapper;

    ThumbnailContainer(ImageWrapper imageWrapper){
        // call super constructor
        super();
        this.imageWrapper = imageWrapper;
        // set style
        this.setMaxSize(110,110);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-border-color:transparent;\n" + "-fx-border-width: 2;\n");

        // set content
        imageView=new ImageView(imageWrapper.getThumbnail());
        this.getChildren().add(imageView);
        this.getChildren().add(new Label(imageWrapper.getName()));

        // set tooltip
        Tooltip.install(this, new Tooltip(imageWrapper.getTooltipString()));

    }

    public ImageView getImageView() {
        return imageView;
    }
    public ImageWrapper getImageWrapper(){ return imageWrapper; }

}
