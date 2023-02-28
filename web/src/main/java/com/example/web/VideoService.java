package com.example.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.*;

@Service
@RequiredArgsConstructor
public class VideoService {
    private static final String FORMAT = "classpath:video/%s.mp4";
    private static final String FORMAT_2 = "video/%s.mp4";
    public static final String VIDEO_CONTENT = "video/mp4";
    public static final String BYTES = "bytes";
    public static final int BYTE_RANGE = 1024;

    private final ResourceLoader resourceLoader;
    private final ClassLoader classLoader = getClass().getClassLoader();

    @SneakyThrows
    public Resource getVideoResource(String title) {
        return this.resourceLoader.getResource(String.format(FORMAT, title));
    }

    @SneakyThrows
    public Resource getVideoResource(String title, boolean format) {
        return this.resourceLoader.getResource(String.format("classpath:video/%s", title));
    }

    @SneakyThrows
    public InputStream getVideoStream(String title) {
        RTSPControl rtspControl = new RTSPControl("wowzaec2demo.streamlock.net", 554, "/vod/mp4:BigBuckBunny_115k.mov");
        rtspControl.RTSPOptions();
        rtspControl.RTSPDescribe();
        while (rtspControl.RTSPSetup() > 0);
        rtspControl.RTSPPlay();
        InputStream stream = new ByteArrayInputStream(rtspControl.buf);
        OutputStream outputStream = new ByteArrayOutputStream();
        Resource resource = new ByteArrayResource(rtspControl.buf);
        File file = new File("video.mp4");
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(rtspControl.buf);
        return null;
//        return classLoader.getResourceAsStream(String.format(FORMAT_2, title));
    }

    @SneakyThrows
    public ResponseEntity<byte[]> getVideoByteArray(String title, String range) {
        long rangeStart = 0;
        long rangeEnd = BYTE_RANGE;
        long fileSize;
        String[] ranges;
        fileSize = getFileSize(title);
        if (range != null) {
            ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = fileSize - 1;
            }
        }

        if (fileSize < rangeEnd) {
            rangeEnd = fileSize - 1;
        }
        byte[] buffer = new byte[BYTE_RANGE];
        try (InputStream stream = classLoader.getResourceAsStream(String.format(FORMAT_2, title))) {
            stream.readNBytes(buffer, (int) rangeStart, (int) rangeEnd);
        }
        String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(CONTENT_TYPE, VIDEO_CONTENT)
                .header(ACCEPT_RANGES, BYTES)
                .header(CONTENT_LENGTH, contentLength)
                .header(CONTENT_RANGE, BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                .body(buffer);
    }

    @SneakyThrows
    public Long getFileSize(String title) {
        return Files.size(Paths.get(classLoader.getResource(String.format(FORMAT_2, title)).toURI()));
    }
}
