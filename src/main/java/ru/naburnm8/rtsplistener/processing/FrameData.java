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

    public FrameData(long timestamp, byte[] frame) {
        this.timestamp = timestamp;
        this.frame = frame;
    }
}
