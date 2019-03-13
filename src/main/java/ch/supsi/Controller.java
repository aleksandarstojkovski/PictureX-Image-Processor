package ch.supsi;

//import ij.ImageJ;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Controller {

    private long lastTime = 0;
    private ArrayList<VBox> vBoxSelected = new ArrayList<>();
    private ArrayList<VBox> vBoxALL = new ArrayList<>();
    private File chosenDirectory;
    private List<File> listOfFiles;
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
    public void initialize() {

        // Inizializzo liste
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
        populateListOfFiles();
        createThubnails();
        displayThubnails();
    }

    private void cleanImages(){
        tilePane.getChildren().clear();
        listOfThubnails.clear();
        listOfFiles.clear();
    }

    private void populateListOfFiles() {

        String[] extensions = {".jpg",".png",".jpeg"};

        for (File f : chosenDirectory.listFiles()) {
            if (f.isFile()) {
                for (String extension : extensions) {
                    if (f.getName().endsWith(extension)) {
                        listOfFiles.add(f);
                        break;
                    }
                }
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
            final int imageID = i;
            ImageView imgView = new ImageView(listOfThubnails.get(i));
            VBox vbox = new VBox(imgView);
            vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { //aggiunta listener ad immagini
                long diff;
                boolean isdblClicked = false;
                final long currentTime = System.currentTimeMillis();

                if(lastTime!=0 && currentTime!=0){
                    diff=currentTime-lastTime;

                    if( diff<=215)
                        isdblClicked=true;
                    else {
                        isdblClicked = false;
                        vBoxSelected.clear();
                        vBoxSelected.add(vbox);
                    }

                }

                lastTime=currentTime;

                System.out.println("IsDblClicked: "+isdblClicked);
                System.out.println(listOfFiles.get(imageID).getName());
                colorVBoxImageView();
                event.consume();
            });
            vBoxALL.add(vbox);
            vbox.setMaxSize(100,100);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(new Label(listOfFiles.get(i).getName()));
            //vbox.setOnMouseClicked(e -> {vbox.setStyle("-fx-background-color: blue;");});
            tilePane.getChildren().addAll(vbox);
            scrollPane.setContent(tilePane);

        }
    }

    private void colorVBoxImageView() {
        //for (ImagelistOfThubnails
        if (!vBoxSelected.isEmpty()){
            for (VBox im : vBoxALL){
                if (vBoxSelected.contains(im)){
                    im.setStyle("-fx-border-color: blue;\n"
                            + "-fx-border-insets: 5;\n"
                            + "-fx-border-width: 3;\n"
                            + "-fx-border-style: dashed;\n");

                }
                else{
                    im.setStyle("-fx-border-color:white;");
                }

            }
        }
    }


}