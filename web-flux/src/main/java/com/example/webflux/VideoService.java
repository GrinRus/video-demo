package com.example.webflux;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class VideoService {
    private static final String FORMAT = "classpath:video/%s.mp4";

    private final ResourceLoader resourceLoader;

    public Mono<Resource> getVideo(String title) {
        return Mono.fromSupplier(() -> this.resourceLoader.getResource(String.format(FORMAT, title)));
    }

    public Mono<ByteArrayOutputStream> getVideo2(String title) {
        RTSPControl rtspControl = new RTSPControl("wowzaec2demo.streamlock.net", 554, "/vod/mp4:BigBuckBunny_115k.mov");
        rtspControl.RTSPOptions();
        rtspControl.RTSPDescribe();
        while (rtspControl.RTSPSetup() > 0);
        rtspControl.RTSPPlay();
        ByteArrayInputStream stream = new ByteArrayInputStream(rtspControl.buf);
        return Mono.fromSupplier(() -> rtspControl.outputStream);
    }
}
