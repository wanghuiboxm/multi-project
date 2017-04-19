package bo;

import java.util.Date;

/**
 * Created by wanghb on 2017/4/19.
 */
public class Kpi {
    private Date beginTime; //指标范围
    private Date endTime; //指标范围
    private long exposure; //曝光
    private long click; //点击
    private double clickRate; //点击率
    private long ticket; //星票
    private long view; //观看
    private long focus; //关注

    @Override
    public String toString() {
        return "Kpi{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", exposure=" + exposure +
                ", click=" + click +
                ", clickRate=" + clickRate +
                ", ticket=" + ticket +
                ", view=" + view +
                ", focus=" + focus +
                '}';
    }

    public long getExposure() {
        return exposure;
    }

    public void setExposure(long exposure) {
        this.exposure = exposure;
    }

    public long getClick() {
        return click;
    }

    public void setClick(long click) {
        this.click = click;
    }

    public double getClickRate() {
        return clickRate;
    }

    public void setClickRate(double clickRate) {
        this.clickRate = clickRate;
    }

    public long getTicket() {
        return ticket;
    }

    public void setTicket(long ticket) {
        this.ticket = ticket;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public long getFocus() {
        return focus;
    }

    public void setFocus(long focus) {
        this.focus = focus;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
