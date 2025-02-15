package ru.naburnm8.rtsplistener;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.naburnm8.rtsplistener.processing.FileVideoProcessor;
import ru.naburnm8.rtsplistener.processing.VideoProcessor;

@RestController
public class Controller {

    @Autowired
    private VideoProcessor videoProcessor;

    @Autowired
    private FileVideoProcessor fileVideoProcessor;

    @Autowired
    private Environment env;

    @GetMapping("/start")
    public String startStreamProcessing(@RequestParam String rtspUrl, @RequestParam int interval, @RequestParam int frameCount){
        System.out.println(rtspUrl);
        new Thread(() -> {videoProcessor.processStream(rtspUrl, interval, frameCount);}).start();
        return "OK";
    }

    @GetMapping("/start-for-file")
    public String startFileProcessing(@RequestParam String path, @RequestParam int interval, @RequestParam int frameCount){
        System.out.println(path);
        new Thread(() -> fileVideoProcessor.processStream(path, interval, frameCount)).start();
        return "OK";
    }

    @GetMapping("/conf")
    public String config(){
        return env.getProperty("secondaryServiceUrl");
    }
}
