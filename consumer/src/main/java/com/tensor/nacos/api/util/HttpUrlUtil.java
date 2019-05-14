package com.tensor.nacos.api.util;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author liaochuntao
 */
@Slf4j
public class HttpUrlUtil {

    private static final String BASE_URL = "http://%s:%d%s";
    private static final String PROVIDER_URL = "http://hbase-api";
    private static final String PUBLISH_SOCKET = "ws://%s:%d/%s";

    static public String url(URI uri) {
        log.info(PROVIDER_URL + uri.getPath());
        return PROVIDER_URL + uri.getPath();
    }

    static public URI urlSocket(String path) {
        try {
            return new URI(String.format(PUBLISH_SOCKET, "127.0.0.1", 8008, path));
        } catch (URISyntaxException e) {
            return null;
        }
    }

    static public String url(String ip, int port, String path) {
        String url = String.format(BASE_URL, ip, port, path);
        log.info("url : {}", url);
        return url;
    }

    static public String url(Instance instance, String path) {
        return url(instance.getIp(), instance.getPort(), path);
    }

}
