package nanjing.jun.viewtest;

/**
 * Created by 王黎军 on 2017/7/11.
 */

public class TimeDurationDegree {
    private float startDegree;
    private float sweepDegree;

    public float getStartDegree() {
        return startDegree;
    }

    public void setStartDegree(float startDegree) {
        this.startDegree = startDegree;
    }

    public float getSweepDegree() {
        return sweepDegree;
    }

    public void setSweepDegree(float sweepDegree) {
        this.sweepDegree = sweepDegree;
    }
    public TimeDurationDegree() {

    }
    public TimeDurationDegree(float startDegree, float sweepDegree) {
        this.startDegree = startDegree;
        this.sweepDegree = sweepDegree;
    }
}
