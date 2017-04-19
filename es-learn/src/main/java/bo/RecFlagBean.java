package bo;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by wanghb on 2017/4/18.
 */
public class RecFlagBean {
    private String room_id;
    private String level;
    private long end_time;
    private long gift_id;
    private long type;
    private long uuid;
    private String zuid;
    private long beg_time;
    private long section_id;
    private long appid;
    private long ac_ts;
    private long gift_worth;
    private String style;
    private long view_time;
    private long position;
    private long channel_id;
    private long ts;

    public RecFlagBean(){}

    public RecFlagBean(String room_id, String level,
                       long end_time, long gift_id,
                       long type, long uuid,
                       String zuid, long beg_time,
                       long section_id, long appid,
                       long ac_ts, long gift_worth,
                       String style, long view_time,
                       long position, long channel_id, long ts) {
        this.room_id = room_id;
        this.level = level;
        this.end_time = end_time;
        this.gift_id = gift_id;
        this.type = type;
        this.uuid = uuid;
        this.zuid = zuid;
        this.beg_time = beg_time;
        this.section_id = section_id;
        this.appid = appid;
        this.ac_ts = ac_ts;
        this.gift_worth = gift_worth;
        this.style = style;
        this.view_time = view_time;
        this.position = position;
        this.channel_id = channel_id;
        this.ts = ts;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getGift_id() {
        return gift_id;
    }

    public void setGift_id(long gift_id) {
        this.gift_id = gift_id;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getZuid() {
        return zuid;
    }

    public void setZuid(String zuid) {
        this.zuid = zuid;
    }

    public long getBeg_time() {
        return beg_time;
    }

    public void setBeg_time(long beg_time) {
        this.beg_time = beg_time;
    }

    public long getSection_id() {
        return section_id;
    }

    public void setSection_id(long section_id) {
        this.section_id = section_id;
    }

    public long getAppid() {
        return appid;
    }

    public void setAppid(long appid) {
        this.appid = appid;
    }

    public long getAc_ts() {
        return ac_ts;
    }

    public void setAc_ts(long ac_ts) {
        this.ac_ts = ac_ts;
    }

    public long getGift_worth() {
        return gift_worth;
    }

    public void setGift_worth(long gift_worth) {
        this.gift_worth = gift_worth;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public long getView_time() {
        return view_time;
    }

    public void setView_time(long view_time) {
        this.view_time = view_time;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(long channel_id) {
        this.channel_id = channel_id;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public static void main(String[] args) {
        RecFlagBean bean = new RecFlagBean();
        System.out.println(JSONObject.toJSONString(bean));
    }
}
