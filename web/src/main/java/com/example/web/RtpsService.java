package com.example.web;

import lombok.SneakyThrows;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.gstreamer.Element;
import org.gstreamer.ElementFactory;
import org.gstreamer.Pipeline;
import org.gstreamer.swing.VideoComponent;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RtpsService {

    final Element source = ElementFactory.make("rtspsrc", "Source");
    VideoComponent videoComponent = new VideoComponent();
    Element videosink = videoComponent.getElement();
    Pipeline pipe = new Pipeline();

    {
        source.set("location", "rtsp://<user>:<pass>@<ip>/mpeg4/1/media.amp");
    }

    @SneakyThrows
    public InputStream getStream() {
        FFmpegFrameGrabber fFmpegFrameGrabber = FFmpegFrameGrabber.createDefault("rtsp://wowzaec2demo.streamlock.net:554/vod/mp4:BigBuckBunny_115k.mov");
        fFmpegFrameGrabber.grab();
        return null;
    }
}
