package Controller.EventHandler;


import Controller.Client;
import org.json.JSONObject;

/**
 * Created by tenyoku on 2015/12/24.
 */
public abstract class EventHandler<T> {
    EventHandler<? super T> nextHandler;

    public EventHandler(EventHandler<? super T> nextHandler) {
        this.nextHandler = nextHandler;
    }

    public void doNext(T client, JSONObject msg) {
        if (nextHandler != null)
            nextHandler.handle(client, msg);
    }

    public abstract void handle(T client, JSONObject msg);
}
