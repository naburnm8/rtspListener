package ru.naburnm8.rtsplistener.processing;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class RtspVideoProcessor {
    private final FrameSender frameSender;

    public RtspVideoProcessor(FrameSender frameSender) {
        this.frameSender = frameSender;
    }

    public void processStream(String rtspUrl, int frameIntervalMs, int frameCount) {
        new Thread(() -> {
            IMediaReader mediaReader = ToolFactory.makeReader(rtspUrl);
            mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

            final long[] lastFrameTime = {0};
            final int[] frameCounter = {0};
            long startTime = System.currentTimeMillis();

            mediaReader.addListener(new MediaListenerAdapter() {
                @Override
                public void onVideoPicture(IVideoPictureEvent event) {
                    if (frameCounter[0] >= frameCount) {
                        mediaReader.close();
                        return;
                    }

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastFrameTime[0] >= frameIntervalMs) {
                        lastFrameTime[0] = currentTime;
                        BufferedImage bufferedImage = event.getImage();
                        byte[] frameBytes = convertBufferedImageToBytes(bufferedImage);
                        FrameData frameData = new FrameData(currentTime - startTime, frameBytes, rtspUrl);
                        frameSender.sendFrame(frameData);
                        frameCounter[0]++;
                    }
                }
            });

            while (mediaReader.readPacket() == null && frameCounter[0] < frameCount) {

            }

            mediaReader.close();
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