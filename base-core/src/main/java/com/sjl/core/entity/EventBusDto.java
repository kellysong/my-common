package com.sjl.core.entity;

/**
 * 事件传送dto
 *
 * @author Kelly
 * @version 1.0.0
 * @filename EventBusDto.java
 * @time 2018/3/6 15:24
 * @copyright(C) 2018 song
 */
public class EventBusDto<T> {
    private int position;//区别fragment

    /**
     * 事件状态码
     */
    private int eventCode;
    private T data;

    public EventBusDto(int eventCode) {
        this(eventCode, null);
    }

    /**
     * EventBusDto实例
     * @param eventCode 事件状态码，默认0
     * @param data 传输数据
     */
    public EventBusDto(int eventCode, T data) {
        this.eventCode = eventCode;
        this.data = data;
    }

    public EventBusDto(int position, int eventCode, T data) {
        this.position = position;
        this.eventCode = eventCode;
        this.data = data;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "EventBusDto{" +
                "position=" + position +
                ", eventCode=" + eventCode +
                ", data=" + data +
                '}';
    }

    public String toString2() {
        return "EventBusDto{" +
                "position=" + position +
                ", eventCode=" + eventCode +
                '}';
    }
}
