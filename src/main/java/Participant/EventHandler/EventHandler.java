package Participant.EventHandler;

import org.json.JSONObject;

public abstract class EventHandler {
    EventHandler nextHandler;

    public EventHandler(EventHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void doNext(JSONObject msg) {
        if (nextHandler != null)
            nextHandler.handle(msg);
    }

    public abstract void handle(JSONObject msg);
}
