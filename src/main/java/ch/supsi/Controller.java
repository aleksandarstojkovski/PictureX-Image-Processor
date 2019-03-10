package ch.supsi;

//import ij.ImageJ;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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

        // Inizializzo liste
        listOfImages = new ArrayList<>();
        listOfFiles = new ArrayList<>();
        listOfThubnails = new ArrayList<>();

        // Frase default sul textField di browse
        browseTextField.setText("Chose a directory...");

        // tilePane utilizzato all'interno dello scrollPane
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(5));
        tilePane.setVgap(5);
        tilePane.setHgap(5);
        tilePane.setAlignment(Pos.TOP_LEFT);

        // rende lo scrollPane resizable
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
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

    private void popolateTilePane(){
        cleanImages();
        populateListOfImagesAndFiles();
        createThubnails();
        displayThubnails();
    }

    void cleanImages(){
        tilePane.getChildren().clear();
        listOfThubnails.clear();
        listOfFiles.clear();
        listOfImages.clear();
    }

    private void populateListOfImagesAndFiles() {

        String[] extensions = {".jpg",".png",".jpeg",".tiff"};

        for (File f : chosenDirectory.listFiles()) {
            if (f.isFile()) {
                for (String extension : extensions) {
                    if (f.getName().endsWith(extension)) {
                        //System.out.println("Its an image");
                        Image img = new Image(f.toURI().toString());
                        listOfImages.add(img);
                        break;
                    }
                }
                listOfFiles.add(f);
            }
        }

    }

    private void createThubnails(){
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

    private void displayThubnails(){
        for (int i=0;i<listOfThubnails.size();i++){
            ImageView imgView = new ImageView(listOfThubnails.get(i));
            VBox vbox = new VBox(imgView);
            vbox.setMaxSize(100,100);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(new Label(listOfFiles.get(i).getName()));
            //vbox.setOnMouseClicked(e -> {vbox.setStyle("-fx-background-color: blue;");});
            tilePane.getChildren().addAll(vbox);
            scrollPane.setContent(tilePane);
        }
    }


}