package ch.supsi;

import DragResizer.DragResizer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiAppController{
    @FXML               //non funziona
    private VBox LVBox; //non funziona
    public TreeView<Path> treeItemView;
    private static final String ROOT_FOLDER = "c:/";

    public void initialize() throws Exception {

        TreeItem<Path> treeItem = new TreeItem<>(Paths.get( ROOT_FOLDER));
        treeItem.setExpanded(false);

        // create tree structure
        createTree(treeItem);
        treeItemView.setRoot(treeItem);
        treeItemView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handle(newValue));
        //DragResizer.makeResizable(LVBox); //non funziona
        DragResizer.makeResizable(treeItemView);
    }

    public void handle(Object newValue){
        //System.out.println(newValue);

        TreeItem<Path> tree = (TreeItem<Path>)newValue;
        System.out.println(tree.isExpanded());
        /*if(Files.isDirectory((Path)tree.getValue())&&tree.isExpanded()){

            tree.setExpanded(false);
            Node folderIconClosed = new ImageView(new Image(getClass().getResourceAsStream("/icons/folder_Closed_12x12.png")));
            tree.setGraphic(folderIconClosed);
        }
        else */if (Files.isDirectory(tree.getValue())&&Files.isReadable(tree.getValue())) {
           /* Node folderIconOpen = new ImageView(new Image(getClass().getResourceAsStream("/icons/folder_Open_12x12.png")));
            tree.setGraphic(folderIconOpen);*/
            try {
                createTree(tree);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void createTree(TreeItem<Path> rootItem) throws IOException {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) {

            for (Path path : directoryStream) {

                TreeItem<Path> newItem = new TreeItem<>(path);
                Node folderIconClosed = new ImageView(new Image(getClass().getResourceAsStream("/icons/folder_Closed_12x12.png")));

                newItem.setExpanded(true);
                if (Files.isDirectory(path)){
                   newItem.setGraphic(folderIconClosed);
                }


                rootItem.getChildren().add(newItem);

                /**if (Files.isDirectory(path)&&Files.isReadable(path)) {
                    createTree(newItem);
                }*/
            }
        }
    }

}
