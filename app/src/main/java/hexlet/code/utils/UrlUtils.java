package hexlet.code.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class UrlUtils {

    public static String normalizeUrl(String input) throws URISyntaxException {
        URI uri = new URI(input).normalize();

        if (uri.getScheme() == null) {
            throw new URISyntaxException(input, "Missing URL scheme (http/https)");
        }

        try {
            URL url = uri.toURL();
            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            return port == -1
                    ? String.format("%s://%s", protocol, host)
                    : String.format("%s://%s:%d", protocol, host, port);

        } catch (MalformedURLException e) {
            throw new URISyntaxException(input, "Invalid URL: " + e.getMessage());
        }
    }

    private UrlUtils() {
    }
}
