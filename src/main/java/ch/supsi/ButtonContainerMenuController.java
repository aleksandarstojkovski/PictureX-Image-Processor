package ch.supsi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class ButtonContainerMenuController {

    @FXML
    public Button zoomInButton;
    @FXML
    public Button zoomOutButton;
    @FXML
    public Button bNButton;
    @FXML
    public Button rotateSXButton;
    @FXML
    public Button rotateDXButton;
    @FXML
    public Button shareButton;

    @FXML
    public void initialize() {
        zoomOutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            System.out.println("zoomOut");
        });
        bNButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e->{
            System.out.println("BN");
        });
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/buttonContainerMenu.fxml"));
//        try{
//            Parent root2 = loader.load();
//        }catch (Exception e){
//
//        }
       // ButtonContainerMenuController dac = loader.getController();
    }
//    @FXML
//    public void handleZoomInButton(ActionEvent event){
//        System.out.println(event);
//
//    }
}
