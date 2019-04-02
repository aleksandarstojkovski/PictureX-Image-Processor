package ch.supsi;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.sun.javafx.geom.AreaOp;
import ij.plugin.frame.Fitter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;

public class Controller {
    private Preferences lastFilePath;
    private ArrayList<VBox> vBoxSelected = new ArrayList<>();
    private ArrayList<VBox> vBoxALL = new ArrayList<>();
    private ArrayList<File> imageFileListSelected = new ArrayList<>();
    private File chosenDirectory;
    private List<ImageWrapper> listOfImages;
    private long lastTime = 1;

    @FXML
    private Label browseTextField;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane bottonPane;

    @FXML
    private Label numberOfFilesLabel;

    @FXML
    private Label totalSizeLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private SplitPane orizontalSplitPane;

    @FXML
    private ImageView imageViewPreview;

    @FXML
    private Pane previewImageAnchorPane;

    @FXML
    private HBox hBoxOut;

    @FXML
    private GridPane previewPanel;

    @FXML
    private Pane panePreview;

    @FXML
    private TableView tableView;

    @FXML
    private TextField globingTextField;

    @FXML
    public void initialize() {
        // init list of images
        listOfImages = new ArrayList<>();

        // default text on browse button
        browseTextField.setText("Chose a directory...");

        // tilePane used inside the scroll pane
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(5));
        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.setAlignment(Pos.TOP_LEFT);

        // make scrollPane resizable
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tilePane);

        // set tableview
        tableView.setEditable(true);

        TableColumn<String,String> firstColumn = new TableColumn<>("type");
        firstColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        firstColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        TableColumn<String,String> secondColumn = new TableColumn<>("name");
        secondColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        secondColumn.prefWidthProperty().bind(tableView.widthProperty().divide(2));

        TableColumn<String,String> thirdColumn = new TableColumn<>("value");
        thirdColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        thirdColumn.prefWidthProperty().bind(tableView.widthProperty().divide(4));

        tableView.getColumns().addAll(firstColumn,secondColumn,thirdColumn);

        //aggiunta listener ad alla preview di sinistra
        setClickListenerImageViewPreview(imageViewPreview);

        setGlobingListener(globingTextField);

        //alla partenza se il programma è già stato usato fa partire tutto dall'ultimo path
        if(getLastFilePath() != null){
            chosenDirectory = getLastFilePath();
            browseTextField.setText(chosenDirectory.getAbsolutePath());
            directoryChosenAction(null);
        }

    }

    @FXML
    public void handleBrowseButton(ActionEvent event){

        // default Windows directory choser
        final DirectoryChooser dirChoser = new DirectoryChooser();

        // get main stage
        Stage stage = (Stage)mainAnchorPane.getScene().getWindow();

        // if program has been already opened, load previous directry
        if(getLastFilePath() != null){
            dirChoser.setInitialDirectory(getLastFilePath());
        }

        // display Windows directory choser
        chosenDirectory = dirChoser.showDialog(stage);

        if (chosenDirectory != null){
            browseTextField.setText(chosenDirectory.getAbsolutePath());
            directoryChosenAction(null);
        }

    }

    private void directoryChosenAction(String fileNamePart){
        setLastFilePath(chosenDirectory); //aggiorna ad ogni selezione il path nelle preferenze
        initUI();
        populateListOfFiles(fileNamePart);
        populateBottomPane();
        displayThumbnails();
    }

    private void initUI(){
        tilePane.getChildren().clear();
        listOfImages.clear();
        ImageWrapper.clear();
    }

    private void populateListOfFiles(String fileNamePart) {
        if (fileNamePart == null){
            String[] validExtensions = {".jpg",".png",".jpeg"};
            for (File f : Objects.requireNonNull(chosenDirectory.listFiles())) {
                if (f.isFile()) {
                    for (String extension : validExtensions) {
                        if (f.getName().toLowerCase().endsWith(extension)) {
                            listOfImages.add(new ImageWrapper(f));
                            break;
                        }
                    }
                }
            }
        }
        else{
            String[] validExtensions = {".jpg",".png",".jpeg"};
            for (File f : Objects.requireNonNull(chosenDirectory.listFiles())) {
                if (f.isFile()) {
                    for (String extension : validExtensions) {
                        if (f.getName().toLowerCase().endsWith(extension) && f.getName().toLowerCase().contains(fileNamePart.toLowerCase())) {
                            listOfImages.add(new ImageWrapper(f));
                            break;
                        }
                    }
                }
            }
        }

    }

    private void populateBottomPane(){
        numberOfFilesLabel.setText(listOfImages.size() + " elementi");
        if (ImageWrapper.getTotalSizeInMegaBytes() <= 1)
            totalSizeLabel.setText(ImageWrapper.getTotalSizeInBytes() + " Bytes");
        else
            totalSizeLabel.setText(ImageWrapper.getTotalSizeInMegaBytes() + " MB");
    }

    private void displayThumbnails(){

        for(ImageWrapper imgWrp : listOfImages){
            ImageView imgView = new ImageView(imgWrp.getThumbnail());
            VBox vbox = new VBox(imgView);
            vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, eventM -> { //aggiunta listener ad immagini
                long diff;
                boolean isdblClicked = false;
                final long currentTime = System.currentTimeMillis();
                if (eventM.isShiftDown() || eventM.isControlDown()){
                    vBoxSelected.add(vbox);
                    imageFileListSelected.add(imgWrp.getFile());
                    colorVBoxImageView();
                }
                else{
                    imageViewPreview.setImage(imgWrp.getOriginalImage());
                    imageViewPreview.fitWidthProperty().bind(previewPanel.widthProperty()); //make resizable imageViewPreview
                    imageViewPreview.fitHeightProperty().bind(previewPanel.heightProperty()); //make resizable imageViewPreview
                    if(currentTime!=0){//lastTime!=0 && creava bug al primo click
                        diff=currentTime-lastTime;

                        if(diff<=215) {
                            isdblClicked = true;
                            displayMetadata(imgWrp.getFile());
                            orizontalSplitPane.setDividerPosition(0, 1);
                        }
                        else {
                            isdblClicked = false;
                            displayMetadata(imgWrp.getFile());
                            vBoxSelected.clear();
                            vBoxSelected.add(vbox);
                            imageFileListSelected.clear();
                            imageFileListSelected.add(imgWrp.getFile());
                        }
                    }
                    lastTime=currentTime;
                    //System.out.println("IsDblClicked: "+isdblClicked); TODO: remove
                    //System.out.println(imgWrp.getName()); TODO: remove
                    colorVBoxImageView();
                }
                eventM.consume();
            });
            vBoxALL.add(vbox);
            vbox.setMaxSize(110,110);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color:transparent;\n" + "-fx-border-width: 2;\n");
            vbox.getChildren().add(new Label(imgWrp.getName()));
            Tooltip.install(vbox, new Tooltip(imgWrp.getTooltipString()));
            tilePane.getChildren().addAll(vbox);

        }
    }

    private void setClickListenerImageViewPreview(ImageView imageViewPreview) {//aggiunta listener ad immagini
        EventHandler<MouseEvent> myHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                long diff = 0;
                boolean isdblClicked = false;
                final long currentTime = System.currentTimeMillis();

                if(currentTime!=0){
                    diff=currentTime-lastTime;

                    if(diff<=215) {
                        isdblClicked = true;
                        double x = orizontalSplitPane.getDividerPositions()[0];
                        if(orizontalSplitPane.getDividerPositions()[0]>0.9){
                            orizontalSplitPane.setDividerPosition(0, 0.5);
                        }
                        else {
                            orizontalSplitPane.setDividerPosition(0, 1);
                        }

                    }
                    else {
                        isdblClicked = false;

                    }
                    //System.out.println("IsDblClicked: "+isdblClicked); TODO: remove

                }
                lastTime=currentTime;
                mouseEvent.consume();
            }
        };
        imageViewPreview.addEventHandler(MouseEvent.MOUSE_CLICKED, myHandler);
    }

    private void setGlobingListener (TextField globingTextField){
        globingTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //tilePane.getChildren().addAll(vBoxALL);
                ArrayList<VBox> assenti = new ArrayList<>();
                ArrayList<VBox> presenti = new ArrayList<>();
                for(VBox v : vBoxALL){
                    for(Node nodeIn:((VBox)v).getChildren()){
                        if(nodeIn instanceof ImageView){
                            Image ii = ((ImageView) nodeIn).getImage();
                            if(!ii.getUrl().toLowerCase().contains(t1.toLowerCase())){
                                assenti.add(v);
                            }
                        }
                    }
                }
                if(s.length() > t1.length()) {
                    for (VBox v : vBoxALL) {
                        for (Node nodeIn : ((VBox) v).getChildren()) {
                            if (nodeIn instanceof ImageView) {
                                Image ii = ((ImageView) nodeIn).getImage();
                                if (ii.getUrl().toLowerCase().contains(t1.toLowerCase())) {
                                   // assenti.remove(v);
                                    presenti.add(v);
                                }
                                //else
                            }
                        }
                    }
                    for(VBox v : presenti){
                        try{
                            tilePane.getChildren().add(v);
                        }catch (java.lang.IllegalArgumentException e){
                        }
                    }
                }

                tilePane.getChildren().removeAll(assenti);
                //directoryChosenAction(t1);
                //System.out.println(observableValue); TODO: remove
                System.out.println("s " + s);
                System.out.println("t1 " + t1);
            }
        });
    }

    private void colorVBoxImageView() {
        if (!vBoxSelected.isEmpty()){
            for (VBox im : vBoxALL){
                if (vBoxSelected.contains(im)){
                    im.setStyle("-fx-border-color: blue;\n" + "-fx-border-width: 2;\n");

                }
                else{
                    im.setStyle("-fx-border-color: transparent;"+ "-fx-border-width: 2;\n");
                }

            }
        }
    }

    private File getLastFilePath(){
        Preferences preference = Preferences.userNodeForPackage(Controller.class);
        String filePath = preference.get("filePath", null);
        if(filePath != null){
            return new File(filePath);
        }
        return null;
    }

    private void setLastFilePath(File file){
        Preferences preference = Preferences.userNodeForPackage(Controller.class);
        if (file != null) {
            preference.put("filePath", file.getPath());
        }
    }

    private void displayMetadata(File file){

        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException | IOException e) {
            Notifications.create()
                    .title("Warning")
                    .text("The filetype is not supported.")
                    .showWarning();
            return;
            //e.printStackTrace();
        }

        tableView.getItems().clear();

        assert metadata != null : "Unable to get image metadata: null";
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                MetadataWrapper mw = new MetadataWrapper(tag);
                tableView.getItems().add(mw);
            }
        }

    }



}