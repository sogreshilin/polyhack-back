package ru.nsu.fit.supernova.service.yc;

import java.io.InputStream;
import java.net.URL;

public interface YCService {
    URL uploadFile(long fileId,
                   InputStream fileStream);
}
