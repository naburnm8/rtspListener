package ru.naburnm8.rtsplistener.processing;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

@Service
public class VideoProcessor {
    private final FrameSender frameSender;

    public VideoProcessor(FrameSender frameSender) {
        this.frameSender = frameSender;
    }

    public void processStream(String rtspUrl, int frameIntervalMs, int frameCount) {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(rtspUrl);
        Java2DFrameConverter converter = new Java2DFrameConverter();

        try {
            System.out.println("Before starting grabber");
            grabber.setOption("stimeout", "500");
            grabber.start(); // Start the grabber
            long lastFrameTime = 0;
            Frame frame;
            int i = 0;
            System.out.println("Starting grabber");
            while ((frame = grabber.grab()) != null) {
                System.out.println("Grabbed!");
                if (i > frameCount) {
                    break;
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastFrameTime >= frameIntervalMs) {
                    lastFrameTime = currentTime;

                    BufferedImage bufferedImage = converter.getBufferedImage(frame);

                    byte[] frameBytes = convertBufferedImageToBytes(bufferedImage);

                    FrameData frameData = new FrameData(currentTime, frameBytes);

                    sendFrame(frameData);
                    i++;
                    System.out.println("Moving on");
                }
                System.out.println("Processing finished");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                grabber.stop(); // Stop the grabber
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendFrame(FrameData frameData) {
        frameSender.sendFrame(frameData);
    }

    private byte[] convertBufferedImageToBytes(BufferedImage bufferedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
}
