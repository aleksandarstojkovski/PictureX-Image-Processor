package ch.supsi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private ArrayList<VBox> vBoxSelected = new ArrayList<>();
    private ArrayList<VBox> vBoxALL = new ArrayList<>();
    private File chosenDirectory;
    private List<ImageWrapper> listOfImages;
    private long lastTime = 0;

    @FXML
    private TextField browseTextField;

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
    private ImageView imageViewPreview;

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
    }

    @FXML
    public void handleBrowseButton(ActionEvent event){

        // default Windows directory choser
        final DirectoryChooser dirChoser = new DirectoryChooser();

        // get main stage
        Stage stage = (Stage)mainAnchorPane.getScene().getWindow();

        // display Windows directory choser
        chosenDirectory = dirChoser.showDialog(stage);
        if (chosenDirectory != null){
            browseTextField.setText(chosenDirectory.getAbsolutePath());
            directoryChosenAction();
        }

    }

    private void directoryChosenAction(){
        initUI();
        populateListOfFiles();
        populateBottomPane();
        displayThumbnails();
    }

    private void initUI(){
        tilePane.getChildren().clear();
        listOfImages.clear();
        ImageWrapper.clear();
    }

    private void populateListOfFiles() {
        String[] validExtensions = {".jpg",".png",".jpeg"};
        for (File f : chosenDirectory.listFiles()) {
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
            vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> { //aggiunta listener ad immagini
                long diff;
                boolean isdblClicked = false;
                final long currentTime = System.currentTimeMillis();

                imageViewPreview.setImage(imgWrp.getOriginalImage());
                if(lastTime!=0 && currentTime!=0){
                    diff=currentTime-lastTime;

                    if( diff<=215) {
                        isdblClicked = true;
                    }
                    else {
                        isdblClicked = false;
                        vBoxSelected.clear();
                        vBoxSelected.add(vbox);
                    }

                }
                lastTime=currentTime;
                System.out.println("IsDblClicked: "+isdblClicked);
                System.out.println(imgWrp.getName());
                colorVBoxImageView();
                event.consume();
            });
            vBoxALL.add(vbox);
            vbox.setMaxSize(100,100);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color:white;\n"+ "-fx-border-width: 2;\n");
            vbox.getChildren().add(new Label(imgWrp.getName()));
            Tooltip.install(vbox, new Tooltip(imgWrp.getTooltipString()));
            tilePane.getChildren().addAll(vbox);
        }
    }

    private void colorVBoxImageView() {
        //for (ImagelistOfThubnails
        if (!vBoxSelected.isEmpty()){
            for (VBox im : vBoxALL){
                if (vBoxSelected.contains(im)){
                    im.setStyle("-fx-border-color: blue;\n" + "-fx-border-width: 2;\n");

                }
                else{
                    im.setStyle("-fx-border-color: white;"+ "-fx-border-width: 2;\n");
                }

            }
        }
    }


}