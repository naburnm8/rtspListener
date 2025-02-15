package ru.naburnm8.rtsplistener.processing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class FrameData {
    private long timestamp;
    private byte[] frame;
    private String name;

    public FrameData(long timestamp, byte[] frame, String name) {
        this.timestamp = timestamp;
        this.frame = frame;
        this.name = name;
    }
}
