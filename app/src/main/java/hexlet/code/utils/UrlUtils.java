package hexlet.code.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtils {

    public static String normalizeUrl(String input) throws URISyntaxException, MalformedURLException {
        var uri = new URI(input).normalize();
        var url = uri.toURL();

        var protocol = url.getProtocol();
        var host = url.getHost();
        var port = url.getPort();

        return port == -1
                ? String.format("%s://%s", protocol, host)
                : String.format("%s://%s:%d", protocol, host, port);
    }
}
