package ch.supsi;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiAppController{

    public TreeView<Path> treeItemView;
    private static final String ROOT_FOLDER = "c:/";

    public void initialize() throws Exception {

        TreeItem<Path> treeItem = new TreeItem<Path>(Paths.get( ROOT_FOLDER));
        treeItem.setExpanded(false);

        // create tree structure
        createTree(treeItem);
        treeItemView.setRoot(treeItem);
    }

    public static void createTree(TreeItem<Path> rootItem) throws IOException {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) {

            for (Path path : directoryStream) {

                TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);

                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)&&Files.isReadable(path)) {
                    createTree(newItem);
                }
            }
        }
    }
}
