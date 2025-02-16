package ru.naburnm8.rtsplistener.processing;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ProcessedData {
    private Long Id;
    private long timestamp;
    private String name;
    private double value1;
    private double value2;
    private double value3;
    private double value4;

    public ProcessedData(Long id, long timestamp, String name, double value1, double value2, double value3, double value4) {
        Id = id;
        this.timestamp = timestamp;
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }
}
