package Participant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by aalexx on 1/3/16.
 */
public class SubmitChooseFileStrategy extends SubmitStategy {
    public SubmitChooseFileStrategy (SubmitController controller) {
        super(controller);
    }
    @Override
    public void onChange() {
        if (controller.getChosenFile() == null)
            controller.setConfirmButtonDisable(true);
        else
            controller.setConfirmButtonDisable(false);
    }

    @Override
    public String getCode() {
        File file = controller.getChosenFile();
        StringBuilder fileData = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(file));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileData.toString();
    }
}
