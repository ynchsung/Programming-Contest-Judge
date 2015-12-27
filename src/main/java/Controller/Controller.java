package Controller;

/**
 * Created by tenyoku on 2015/12/24.
 */
public class Controller {
    static public void main(String [] argv) {
        Core.start(new RoundRobinStrategy(), 7122);
    }
}
