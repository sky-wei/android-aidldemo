package com.sky.android.aidldemo.event;

/**
 * Created by sky on 16-10-10.
 */

public class DemoEvent {

    public static final int BIND_SUCCESS = 0x0001;
    public static final int BIND_FAILURE = 0x0002;
    public static final int NOTIFICATION = 0x0003;

    private int eventId;
    private Object data;

    public DemoEvent(int eventId) {
        this(eventId, null);
    }

    public DemoEvent(int eventId, Object data) {
        this.eventId = eventId;
        this.data = data;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DemoEvent{" +
                "eventId=" + eventId +
                ", data=" + data +
                '}';
    }
}
