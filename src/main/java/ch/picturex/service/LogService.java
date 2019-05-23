package ch.picturex.service;

import ch.picturex.Model;
import ch.picturex.events.EventLog;
import ch.picturex.model.Severity;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogService {

    private PrintWriter out = null;
    private final DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Model model = Model.getInstance();

    public LogService(){
        configureBus();
    }

    private void configureBus(){
        model.subscribe(EventLog.class, e -> log(e.getText(), e.getSeverity()));
    }

    private void log(String text, Severity severity){
        Date date = new Date();
        if (out == null) {
            FileWriter fr = null;
            try {
                fr = new FileWriter(model.getChosenDirectory() + File.separator + "log.txt", true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert fr != null;
            BufferedWriter br = new BufferedWriter(fr);
            out = new PrintWriter(br);
        }
        out.println("[" + sdf.format(date) + "]" + " " + severity + " : " + text);
        out.flush();
    }

    public void close(){
        if (out != null) {
            out.close();
        }
    }

}
