package Controller;

import CustomNode.MyConfirmationButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by aalexx on 12/27/15.
 *
 * The contest time updating page.
 *
 * The unit of 'duration' is minute.
 *
 * The ending time on the gui will be calculated automatically.
 */
public class TimeUpdateController implements Initializable{
    @FXML private MyConfirmationButton confirm;
    @FXML private Text endTime;
    @FXML private TextField duration;
    @FXML private Text startTime;
    private String dateTimeFormat = "HH:mm:ss";
    private long startUnixTime = 0;
    private long durationUnixTime = 0;
    private long endTimeUnixTime = 0;

    public void setStartTime(long startTime) {
        this.startUnixTime = startTime;
        String formattedDate = transformDateFormat(startTime);
        this.startTime.setText(formattedDate);
    }

    public String transformDateFormat(long time) {
        Date date = new Date(time * 1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void setDateTimeFormat (String newFormat) {
        this.dateTimeFormat = newFormat;
    }

    public void setDuration(long duration) {
        this.durationUnixTime = duration * 60;
        this.duration.setText(Long.toString(duration));
    }

    public void setEndTime(String endTime) {
        this.endTime.setText(endTime);
    }

    public int getDuration() {
        return Integer.parseInt(duration.getText());
    }

    public void setOnConfirmAction(EventHandler action) {
        confirm.setOkAction(action);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // limit the duration input
        duration.textProperty().addListener((observable, oldValue, newValue) -> {
            // only number or empty
            if (! newValue.matches("\\d*")) {
                duration.setText(oldValue);
            } else {
                if (newValue.equals(""))
                    durationUnixTime = 0;
                else
                    durationUnixTime = Long.parseLong(newValue) * 60;
                endTimeUnixTime = startUnixTime + durationUnixTime;
                setEndTime(transformDateFormat(endTimeUnixTime));
            }
        });
    }
}
