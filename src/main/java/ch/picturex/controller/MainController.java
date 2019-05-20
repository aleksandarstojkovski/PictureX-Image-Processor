package ch.picturex.controller;

import ch.picturex.*;
import ch.picturex.events.*;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import de.muspellheim.eventbus.EventBus;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class MainController {

    static ArrayList<ThumbnailContainer> selectedThumbnailContainers = new ArrayList<>();
    private ArrayList<ThumbnailContainer> allThumbnailContainers = new ArrayList<>();
    private List<ImageWrapper> listOfImageWrappers = new ArrayList<>();
    private long lastTime = 1;
    private static EventBus bus = SingleEventBus.getInstance();
    private TilePane tilePane;
    private Model model;

    @FXML
    public static AnchorPane mainAnchorPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private SplitPane orizontalSplitPane;
    @FXML
    private ImageView imageViewPreview;
    @FXML
    private GridPane previewPanel;
    @FXML
    private TableView tableView;
    @FXML
    private TextField globingTextField;

    @FXML
    public void initialize() {
        configureBus();

        model = Model.getInstance();

        // tilePane used inside the scroll pane
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(5));
        tilePane.setVgap(10);
        tilePane.setHgap(10);
        tilePane.setAlignment(Pos.TOP_LEFT);

        // make scrollPane resizable
        scrollPane.setContent(tilePane);

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
        if(model.getChosenDirectory() != null){
            directoryChosenAction();
        }

        imageViewPreview.fitWidthProperty().bind(previewPanel.widthProperty()); //make resizable imageViewPreview
        imageViewPreview.fitHeightProperty().bind(previewPanel.heightProperty()); //make resizable imageViewPreview
    }


    private void configureBus(){
        bus.subscribe(EventImageChanged.class, e -> {
            imageViewPreview.setImage(e.getThubnailContainer().getImageWrapper().getPreviewImageView());
            if(selectedThumbnailContainers.size()==1)displayMetadata(selectedThumbnailContainers.get(0).getImageWrapper().getFile()); //update exif table
        });
        bus.subscribe(EventBrowseButtonPressed.class, e->handleBrowseButton());
    }

    private void directoryChosenAction(){
        initUI();
        populateListOfFiles();
        bus.publish(new EventUpdateBottomToolBar(listOfImageWrappers, model.getChosenDirectory()));
        displayThumbnails();
    }

    private void initUI(){
        allThumbnailContainers.clear();
        tilePane.getChildren().clear();
        listOfImageWrappers.clear();
        ImageWrapper.clear();
    }

    private void populateListOfFiles() {
            String[] validExtensions = {".jpg",".png",".jpeg"};
            for (File f : Objects.requireNonNull(model.getChosenDirectory().listFiles())) {
                if (f.isFile()) {
                    for (String extension : validExtensions) {
                        if (f.getName().toLowerCase().endsWith(extension)) {
                            listOfImageWrappers.add(new ImageWrapper(f));
                            break;
                        }
                    }
                }
            }
    }

    private void displayThumbnails(){
        for(ImageWrapper imgWrp : listOfImageWrappers){
            ThumbnailContainer thumbnailContainer = new ThumbnailContainer(imgWrp);
            thumbnailContainer.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> { //aggiunta listener ad immagini
                long diff;
                boolean isDoubleClicked = false;
                final long currentTime = System.currentTimeMillis();
                if (mouseEvent.isShiftDown() || mouseEvent.isControlDown()){
                    selectedThumbnailContainers.add(thumbnailContainer);
                    colorVBoxImageView();
                }
                else{
                    imageViewPreview.setImage(imgWrp.getPreviewImageView());
                    if(currentTime!=0){//lastTime!=0 && creava bug al primo click
                        diff=currentTime-lastTime;

                        if(diff<=215) {
                            isDoubleClicked = true;
                            displayMetadata(imgWrp.getFile());
                            orizontalSplitPane.setDividerPosition(0, 1);
                            //buttonMenu.setVisible(true);
                        }
                        else {
                            isDoubleClicked = false;
                            displayMetadata(imgWrp.getFile());
                            selectedThumbnailContainers.clear();
                            selectedThumbnailContainers.add(thumbnailContainer);
                        }
                    }
                    lastTime=currentTime;
                    colorVBoxImageView();
                }
                mouseEvent.consume();
            });
            allThumbnailContainers.add(thumbnailContainer);
            tilePane.getChildren().add(thumbnailContainer);
        }
    }

    private void setClickListenerImageViewPreview(ImageView imageViewPreview) {//aggiunta listener ad immagini
        EventHandler<MouseEvent> myHandler = mouseEvent -> {
            long diff = 0;
            boolean isDoubleClicked = false;
            final long currentTime = System.currentTimeMillis();

            if(currentTime!=0){
                diff=currentTime-lastTime;
                if(diff<=215) {
                    isDoubleClicked = true;
                    double x = orizontalSplitPane.getDividerPositions()[0];
                    if(orizontalSplitPane.getDividerPositions()[0]>0.9){
                        orizontalSplitPane.setDividerPosition(0, 0.5);
                        //buttonMenu.setVisible(false);
                    }
                    else {
                        orizontalSplitPane.setDividerPosition(0, 1);
                        //buttonMenu.setVisible(true);
                    }
                }
                else {
                    isDoubleClicked = false;
                }
            }
            lastTime=currentTime;
            mouseEvent.consume();
        };
        imageViewPreview.addEventHandler(MouseEvent.MOUSE_CLICKED, myHandler);
    }

    private void setGlobingListener (TextField globingTextField){
        globingTextField.textProperty().addListener((observableValue, s, t1) -> {
            ArrayList<ThumbnailContainer> toBeRemovedTC = new ArrayList<>();
            ArrayList<ThumbnailContainer> toBeAddedTC = new ArrayList<>();
            for(ThumbnailContainer v : allThumbnailContainers){
                ImageWrapper iw = v.getImageWrapper();
                String filename = iw.getName().trim();
                if(!filename.toLowerCase().contains(t1.toLowerCase())){
                    toBeRemovedTC.add(v);
                }
            }
            if(s.length() > t1.length()) {
                for (ThumbnailContainer v : allThumbnailContainers) {
                    ImageWrapper iw = v.getImageWrapper();
                    String filename = iw.getName().trim();
                    if (filename.toLowerCase().contains(t1.toLowerCase())) {
                        toBeAddedTC.add(v);
                    }
                }
                for(ThumbnailContainer v : toBeAddedTC){
                    try{
                        tilePane.getChildren().add(v);
                    }catch (IllegalArgumentException ignored){}
                }
            }
            tilePane.getChildren().removeAll(toBeRemovedTC);
        });
    }

    private void colorVBoxImageView() {
        if (!selectedThumbnailContainers.isEmpty()){
            for (ThumbnailContainer thumbnailContainer : allThumbnailContainers){
                if (selectedThumbnailContainers.contains(thumbnailContainer)){
                    thumbnailContainer.setStyle("-fx-background-color: #CCE8FF;\n");
                }
                else{
                    thumbnailContainer.setStyle("-fx-background-color: transparent;");
                }
            }
        }
    }
    @FXML
    public void handleBrowseButton(){
        // default Windows directory choser
        final DirectoryChooser dirChoser = new DirectoryChooser();

        // get main stage
        Stage stage = (Stage)MainController.mainAnchorPane.getScene().getWindow();

        // if program has been already opened, load previous directry
        if(model.getChosenDirectory() != null){
            dirChoser.setInitialDirectory(model.getChosenDirectory());
        }

        // display Windows directory choser
        File test = dirChoser.showDialog(stage);
        bus.publish(new EventDirectoryChanged(test));

        if (model.getChosenDirectory() != null){
            bus.publish(new EventDirectoryChosen(model.getChosenDirectory()));
        }

    }

    private void displayMetadata(File file){
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
        } catch (ImageProcessingException | IOException e) {
            return;
        }
        tableView.getItems().clear();
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                MetadataWrapper mw = new MetadataWrapper(tag);
                tableView.getItems().add(mw);
            }
        }
    }

}