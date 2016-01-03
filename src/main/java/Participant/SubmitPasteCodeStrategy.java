package Participant;

/**
 * Created by aalexx on 1/3/16.
 */
public class SubmitPasteCodeStrategy extends SubmitStategy {
    public SubmitPasteCodeStrategy (SubmitController controller) {
        super(controller);
    }

    @Override
    public void onChange() {
        String text = controller.getTextArea();
        if (text.equals("")) {
            controller.setConfirmButtonDisable(true);
        } else {
            controller.setConfirmButtonDisable(false);
        }
    }

    @Override
    public String getCode() {
        return controller.getTextArea();
    }
}
