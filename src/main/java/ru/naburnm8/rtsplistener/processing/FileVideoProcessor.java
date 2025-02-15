package ru.naburnm8.rtsplistener.processing;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class FileVideoProcessor {
    private final FrameSender frameSender;

    public FileVideoProcessor(FrameSender frameSender) {
        this.frameSender = frameSender;
    }

    public void processStream(String filePath, int frameIntervalMs, int frameCount) {
        new Thread(() -> {
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(filePath);

            try {
                System.out.println("Starting grabber for file: " + filePath);
                grabber.start();
                System.out.println("Grabber started successfully.");

                Java2DFrameConverter converter = new Java2DFrameConverter();
                long lastFrameTime = 0;

                Frame frame;
                int frameCounter = 0;
                long startTime = System.currentTimeMillis();
                while ((frame = grabber.grab()) != null) {
                    if (frame.image == null){
                        continue;
                    }
                    if (frameCounter >= frameCount){
                        break;
                    }
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastFrameTime >= frameIntervalMs) {
                        lastFrameTime = currentTime;
                        BufferedImage bufferedImage = converter.getBufferedImage(frame);
                        byte[] frameBytes = convertBufferedImageToBytes(bufferedImage);
                        FrameData frameData = new FrameData(currentTime - startTime, frameBytes);
                        frameSender.sendFrame(frameData);
                    }
                    frameCounter++;
                }
            } catch (Exception e) {
                System.err.println("Error processing video file: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    grabber.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
