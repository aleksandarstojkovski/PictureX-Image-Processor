package ch.picturex.events;

import ch.picturex.Severity;

public class EventLog {

    private String text;
    private Severity severity;

    public EventLog(String text, Severity severity){
        this.text=text;
        this.severity=severity;
    }

    public String getText() {
        return text;
    }

    public Severity getSeverity() {
        return severity;
    }

}
