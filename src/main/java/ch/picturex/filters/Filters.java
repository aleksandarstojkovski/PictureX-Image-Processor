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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")

public class Filters {

    private static Model model = Model.getInstance();
    private static List<ArrayList<ThumbnailContainer>> selectionHistory = new ArrayList<>();
    private static AtomicInteger count = new AtomicInteger(0);
    private static long size;
    private static boolean success=true;
    private static double progressCount;

    public static void apply(ArrayList<ThumbnailContainer> thumbnailContainers, String filterName, Map<String, Object> parameters) {
        if ((thumbnailContainers.size()==1 && thumbnailContainers.get(0).getImageWrapper().getSizeInKBytes()<1000) || filterName.equals("Zoom")){
            applyWithoutDialog(thumbnailContainers,filterName,parameters);
        } else {
            applyWithDialog(thumbnailContainers,filterName,parameters);
        }
    }

    private static void applyWithoutDialog(ArrayList<ThumbnailContainer> thumbnailContainers, String filterName, Map<String, Object> parameters){
        saveSelection(thumbnailContainers);
        for (ThumbnailContainer tc : thumbnailContainers) {
            success=true;
                try {
                    Class<IFilter> cls;
                    cls = (Class<IFilter>) Class.forName("ch.picturex.filters." + filterName);
                    Constructor<IFilter> constructor = cls.getConstructor();
                    IFilter instanceOfIFilter = constructor.newInstance();
                    Method method = cls.getMethod("apply", ThumbnailContainer.class, Map.class);
                    method.invoke(instanceOfIFilter, tc, parameters);
                } catch (Exception e){
                    success=false;
                }
                if (success){
                    if (thumbnailContainers.size() == 1) {
                        model.publish(new EventImageChanged(thumbnailContainers.get(0)));
                    }
                    model.publish(new EventLog("Filter " + filterName + " applied on image: " + tc.getImageWrapper().getName(), Severity.INFO));
                }else {
                    Notifications.create()
                            .title(model.getResourceBundle().getString("notify.notSupportedFormat.title"))
                            .text(model.getResourceBundle().getString("notify.notSupportedFormat.text"))
                            .showWarning();
                    model.publish(new EventLog("Unable to apply filter " + filterName + " to image: " + tc.getImageWrapper().getName(), Severity.ERROR));
                }
        }
    }

    private static void applyWithDialog(ArrayList<ThumbnailContainer> thumbnailContainers, String filterName, Map<String, Object> parameters){
        ExecutorService executorService = model.getExecutorService();
        saveSelection(thumbnailContainers);
        size = thumbnailContainers.size()*2;
        count.set(0);
        Alert progressAlert = displayProgressDialog(filterName, FXApp.primaryStage);
        ProgressBar tempPro = (ProgressBar) progressAlert.getGraphic();
        Platform.runLater(()->tempPro.setProgress(0));
        for (ThumbnailContainer tc : thumbnailContainers) {
            success=true;
            executorService.execute(() -> {
                try {
                    Class<IFilter> cls;
                    cls = (Class<IFilter>) Class.forName("ch.picturex.filters." + filterName);
                    Constructor<IFilter> constructor = cls.getConstructor();
                    IFilter instanceOfIFilter = constructor.newInstance();
                    Method method = cls.getMethod("apply", ThumbnailContainer.class, Map.class);
                    progressCount = ((double) count.incrementAndGet() / size);
                    Platform.runLater(() -> tempPro.setProgress(progressCount));
                    method.invoke(instanceOfIFilter, tc, parameters);
                    progressCount = ((double) count.incrementAndGet() / size);
                    Platform.runLater(() -> tempPro.setProgress(progressCount));
                    if (count.get() >= size)
                        Platform.runLater(() -> forcefullyHideDialog(progressAlert));
                } catch (Exception e){
                    success=false;
                }
                if (success){
                    if (thumbnailContainers.size() == 1) {
                        model.publish(new EventImageChanged(thumbnailContainers.get(0)));
                    }
                    model.publish(new EventLog("Filter " + filterName + " applied on image: " + tc.getImageWrapper().getName(), Severity.INFO));
                }else {
                    Platform.runLater(() -> forcefullyHideDialog(progressAlert));
                    Platform.runLater(()-> Notifications.create()
                            .title(model.getResourceBundle().getString("notify.notSupportedFormat.title"))
                            .text(model.getResourceBundle().getString("notify.notSupportedFormat.text"))
                            .showWarning());
                    model.publish(new EventLog("Unable to apply filter " + filterName + " to image: " + tc.getImageWrapper().getName(), Severity.ERROR));
                }
            });
        }
    }

    private static void saveSelection(List<ThumbnailContainer> thumbnailContainers){
        if (thumbnailContainers.size()>0)
            selectionHistory.add(new ArrayList<>(thumbnailContainers));
    }

    public static void undo(){
        if (selectionHistory.size() > 0) {
            ExecutorService executorService = model.getExecutorService();
            List<ThumbnailContainer> lastSelection = selectionHistory.get(selectionHistory.size() - 1);
            size = lastSelection.size();
            count.set(0);
            Alert progressAlert = displayProgressDialog(null, FXApp.primaryStage);
            ProgressBar tempPro = (ProgressBar) progressAlert.getGraphic();
            Platform.runLater(()->tempPro.setProgress(0));

            for (ThumbnailContainer tc : lastSelection) {
                executorService.execute(()-> {
                    try {
                        progressCount = ((double) count.incrementAndGet() / size);
                        Platform.runLater(() -> tempPro.setProgress(progressCount));
                        tc.getImageWrapper().undo();
                        if (count.get() == size)
                            Platform.runLater(() -> forcefullyHideDialog(progressAlert));
                    } catch (Exception e){
                        Platform.runLater(() -> forcefullyHideDialog(progressAlert));
                    }
                });
            }
            model.shutdownExecutorService();
            model.publish(new EventImageChanged(lastSelection.get(0)));
            selectionHistory.remove(selectionHistory.size() - 1);

        }
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
