package Controller.Scoreboard;

public class ProblemScore {
    private int submit_num;
    private int acTime;
    private boolean ac;

    public int getSubmitNum() {
        return submit_num;
    }

    public int getAcTime() {
        return acTime;
    }

    public boolean getAc () {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public void setAcTime(int acTime) {
        this.acTime = acTime;
    }

    public void setSubmitNum(int submit_num) {
        this.submit_num = submit_num;
    }

    public void increaseSubmitNum () {
        this.submit_num ++;
    }

    public ProblemScore() {
        this.submit_num = 0;
        this.acTime = 0;
        this.ac = false;
    }
}