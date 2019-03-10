package ch.supsi;

import ij.ImageJ;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.stage.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private File chosenDirectory;
    private List<File> listOfFiles;
    private List<Image> listOfImages;
    private List<Image> listOfThubnails;

    @FXML
    private TextField browseTextField;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    ImageView imageView;

    @FXML
    public void initialize() {
        listOfImages = new ArrayList<>();
        listOfFiles = new ArrayList<>();
        listOfThubnails = new ArrayList<>();
        browseTextField.setText("chose directory...");
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(5));
        tilePane.setVgap(4);
        tilePane.setHgap(4);
        tilePane.setAlignment(Pos.TOP_LEFT);
    }

    @FXML
    public void handleBrowseButton(ActionEvent event){

        // directory choser di Windows
        final DirectoryChooser dirChoser = new DirectoryChooser();

        // Stega principale
        Stage stage = (Stage)mainAnchorPane.getScene().getWindow();

        // Mostra dirchoser di Windows
        chosenDirectory = dirChoser.showDialog(stage);
        if (chosenDirectory != null){
            browseTextField.setText(chosenDirectory.getAbsolutePath());
            popolateTilePane();
        }
    }

    public void popolateTilePane(){
        cleanImages();
        populateListOfImagesAndFiles();
        createThubnails();
        displayThubnails();
    }

    void cleanImages(){
        tilePane.getChildren().removeAll();
        listOfThubnails.clear();
        listOfFiles.clear();
        listOfImages.clear();
    }

    public void populateListOfImagesAndFiles() {
        for (File f : chosenDirectory.listFiles()) {
            String[] extensions = {".jpg",".png",".jpeg"};
            for (String extension : extensions) {
                if(f.getName().endsWith(extension)){
                    //System.out.println("Its an image");
                    Image img = new Image(f.toURI().toString());
                    listOfImages.add(img);
                    break;
                }
            }
            listOfFiles.add(f);
        }
    }

    public void createThubnails(){
        for (File file : listOfFiles) {
            Image thubnail = new Image(file.toURI().toString(),
                    100, // requested width
                    100, // requested height
                    false, // preserve ratio
                    false, // smooth rescaling
                    true // load in background
            );
            listOfThubnails.add(thubnail);
        }
    }

    public void displayThubnails(){
        for (Image img : listOfThubnails){
            ImageView imgView = new ImageView(img);
            tilePane.getChildren().addAll(imgView);
            scrollPane.setContent(tilePane);
        }
    }

}