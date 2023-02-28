package com.example.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService service;

//    @GetMapping(value = "video/resource/{title}", produces = "video/mp4")
    @GetMapping(value = "video/resource/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Resource getVideoResource(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return service.getVideoResource(title);
    }

    //    @GetMapping(value = "video/resource/{title}", produces = "video/mp4")
    @GetMapping(value = "video/resource1/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getVideoResource1(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(service.getVideoResource(title, true));
    }

    @GetMapping(value = "video/stream/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public StreamingResponseBody getVideoStream(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return outputStream -> outputStream.write(service.getVideoStream(title).readAllBytes());
    }

    @GetMapping(value = "video/stream1/{title}", produces = "video/mp4")
    public ResponseEntity<InputStreamResource> getVideoStream1(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .body(new InputStreamResource(service.getVideoStream(title)));
    }

    @GetMapping(value = "video/stream2/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<InputStream> getVideoStream2(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(service.getVideoStream(title));
    }

    @SneakyThrows
    @GetMapping(value = "video/byte/{title}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public byte[] getVideoByte(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return service.getVideoStream(title).readAllBytes();
    }

    @GetMapping(value = "video/byte_array/{title}", produces = "video/mp4")
    public ResponseEntity<byte[]> getVideoByteArray(@PathVariable String title, @RequestHeader(value = "Range", required = false) String range) {
        System.out.println(range);
        return service.getVideoByteArray(title, range);
    }
}
