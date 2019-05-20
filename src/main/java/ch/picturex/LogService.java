package ch.picturex;

import ch.picturex.events.EventLog;
import de.muspellheim.eventbus.EventBus;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

    private static final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Model model = Model.getInstance();
    private EventBus bus = SingleEventBus.getInstance();

    LogService(){
        bus.subscribe(EventLog.class, e->{log(e.getText(),e.getSeverity());});
    }

    private void log(String text, Severity severity){
        Date date = new Date();
        FileWriter fr;
        PrintWriter out = null;
        try {
            fr = new FileWriter(model.getChosenDirectory() + File.separator + "log.txt", true);
            BufferedWriter br = new BufferedWriter(fr);
            out = new PrintWriter(br);
            out.println("[" + sdf.format(date) + "]" + " " + severity + " : " + text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (out != null) {
            out.close();
        }
    }
}
