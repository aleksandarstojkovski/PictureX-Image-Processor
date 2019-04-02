package ch.supsi;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class ThumbnailContainer extends VBox{

    ImageView test;
    ThumbnailContainer(ImageWrapper imageWrapper){
        super(new ImageView(imageWrapper.getThumbnail()));
        test = (ImageView) this.getChildren().get(0);
        setMaxSize(110,110);
        setAlignment(Pos.CENTER);
        setStyle("-fx-border-color:transparent;\n" + "-fx-border-width: 2;\n");
        getChildren().add(new Label(imageWrapper.getName()));
        Tooltip.install(this, new Tooltip(imageWrapper.getTooltipString()));
    }

}
