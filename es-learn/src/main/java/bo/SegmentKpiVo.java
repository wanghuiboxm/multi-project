package bo;

import java.util.List;

/**
 * Created by wanghb on 2017/4/19.
 */
public class SegmentKpiVo {
    private Long zuid;
    private List<Kpi> segmetKpis;
    private List<String> x;
    private Kpi wholeKpi;

    @Override
    public String toString() {
        return "SegmentKpiVo{" +
                "zuid=" + zuid +
                ", segmetKpis=" + segmetKpis +
                ", wholeKpi=" + wholeKpi +
                '}';
    }

    public Long getZuid() {
        return zuid;
    }

    public void setZuid(Long zuid) {
        this.zuid = zuid;
    }

    public List<Kpi> getSegmetKpis() {
        return segmetKpis;
    }

    public void setSegmetKpis(List<Kpi> segmetKpis) {
        this.segmetKpis = segmetKpis;
    }

    public Kpi getWholeKpi() {
        return wholeKpi;
    }

    public void setWholeKpi(Kpi wholeKpi) {
        this.wholeKpi = wholeKpi;
    }
}
