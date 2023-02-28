package com.example.webflux;

import io.netty.handler.codec.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService service;

    @GetMapping(value = "video/{title}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return service.getVideo(title);
    }

    @GetMapping(value = "video3/{title}", produces = "video/mp4")
    public Mono<ByteArrayOutputStream> getVideo3(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return service.getVideo2(title);
    }

}
