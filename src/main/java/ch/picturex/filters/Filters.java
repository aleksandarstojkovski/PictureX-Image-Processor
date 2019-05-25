package ch.picturex.filters;

import ch.picturex.*;
import ch.picturex.events.EventImageChanged;
import ch.picturex.events.EventLog;
import ch.picturex.model.Severity;
import ch.picturex.model.ThumbnailContainer;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class Filters {

    private static Model model = Model.getInstance();
    private static List<ArrayList<ThumbnailContainer>> selectionHistory = new ArrayList<>();
    private static boolean success;
    static AtomicInteger count = new AtomicInteger(0);

    @SuppressWarnings("unchecked")

    public static void apply(ArrayList<ThumbnailContainer> thumbnailContainers, String filterName, Map<String, Object> parameters) {
        success = true;
        saveSelection(thumbnailContainers);
        Alert progressAlert = displayProgressDialog(filterName, FXApp.primaryStage);
        long size = thumbnailContainers.size();
        count.set(0);
        ProgressBar tempPro = (ProgressBar) progressAlert.getGraphic();
        for (ThumbnailContainer tc : thumbnailContainers) {

            model.getExecutorService().execute(() -> {
                Class<IFilter> cls;
                try {
                    cls = (Class<IFilter>) Class.forName("ch.picturex.filters." + filterName);
                    Constructor<IFilter> constructor = cls.getConstructor();
                    IFilter instanceOfIFilter = constructor.newInstance();
                    Method method = cls.getMethod("apply", ThumbnailContainer.class, Map.class);
                    method.invoke(instanceOfIFilter, tc, parameters);
                } catch (ClassNotFoundException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                    success = false;
                }
                if (success) {
                    if (thumbnailContainers.size() == 1)
                        model.publish(new EventImageChanged(thumbnailContainers.get(0)));
                    model.publish(new EventLog("Filter " + filterName + " applied on image: " + tc.getImageWrapper().getName(), Severity.INFO));
                } else {
                    Notifications.create()
                            .title(model.getResourceBundle().getString("notify.notSupportedFormat.title"))
                            .text(model.getResourceBundle().getString("notify.notSupportedFormat.text"))
                            .showWarning();
                    model.publish(new EventLog("Unable to apply filter " + filterName + " to image: " + tc.getImageWrapper().getName(), Severity.ERROR));
                }
                synchronized (tempPro) {
                    count.incrementAndGet();
                    final float progressCount = ((float) count.get() / size);
                    Platform.runLater(() -> tempPro.setProgress(progressCount));
                    if (count.get() >= thumbnailContainers.size())
                    Platform.runLater(() -> forcefullyHideDialog(progressAlert));
                }
            });
        }
    }

    private static void saveSelection(List<ThumbnailContainer> thumbnailContainers){
        if (thumbnailContainers.size()>0)
            selectionHistory.add(new ArrayList<>(thumbnailContainers));
    }

    public static void undo(){
        Alert progressAlert = displayProgressDialog(null, FXApp.primaryStage);
        model.getExecutorService().execute(() -> {
            try {
                if (selectionHistory.size() > 0) {
                    List<ThumbnailContainer> lastSelection = selectionHistory.get(selectionHistory.size() - 1);

                    long size = lastSelection.size();
                    long count = 0;
                    ProgressBar tempPro = (ProgressBar) progressAlert.getGraphic();

                    for (ThumbnailContainer tc : lastSelection) {
                        count++;
                        final float progressCount = ((float)count / size);
                        Platform.runLater(() ->tempPro.setProgress(progressCount));
                        tc.getImageWrapper().undo();
                    }
                    model.publish(new EventImageChanged(lastSelection.get(0)));
                    selectionHistory.remove(selectionHistory.size() - 1);
                }
                Platform.runLater(() -> forcefullyHideDialog(progressAlert));
            }
            catch (Exception e) {
                //Do what ever handling you need here....
                Platform.runLater(() -> forcefullyHideDialog(progressAlert));
            }
        });
    }

    public static void clearHistory(){
        selectionHistory.clear();
    }

    private static Alert displayProgressDialog(String message, Stage stage) {
        Alert progressAlert = new Alert(Alert.AlertType.NONE);
        //final ProgressIndicator progressBar = new ProgressIndicator();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.setPrefHeight(30);

        final Label progressLabel = new Label(message);
        progressAlert.setTitle(model.getResourceBundle().getString("alert.progressBar.title"));
        progressAlert.setGraphic(progressBar);
        progressAlert.setHeaderText(model.getResourceBundle().getString("alert.progressBar.text"));

        VBox vbox = new VBox(20, progressLabel, progressBar);
        vbox.setMaxWidth(Double.MAX_VALUE);
        vbox.setPrefSize(300, 100);
        progressAlert.getDialogPane().setContent(vbox);
        progressAlert.initModality(Modality.WINDOW_MODAL);
        progressAlert.initOwner(stage);
        progressAlert.show();
        return progressAlert;
    }
    private static void forcefullyHideDialog(javafx.scene.control.Dialog<?> dialog) {
        // for the dialog to be able to hide, we need a cancel button,
        // so lets put one in now and then immediately call hide, and then
        // remove the button again (if necessary).
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().add(ButtonType.CANCEL);
        dialog.hide();
        dialogPane.getButtonTypes().remove(ButtonType.CANCEL);
    }

}
