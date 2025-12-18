package travel.letstrip.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Utility class for extracting common web-related information
 * from {@link HttpServletRequest}.
 *
 * <p>This class provides helper methods for:</p>
 * <ul>
 *   <li>Detecting client device type</li>
 *   <li>Resolving client IP address</li>
 *   <li>Reading request headers</li>
 *   <li>Safely reading request body</li>
 * </ul>
 *
 * <p><b>Note:</b> This class is stateless and should not store
 * {@link HttpServletRequest} as a field.</p>
 */
@Component
public final class WebUtils {

    private static final List<String> DEVICES = List.of(
            "Android", "iPhone", "iPad", "Windows", "Macintosh", "Linux"
    );

    private WebUtils() {
        // utility class
    }

    /**
     * Detects client device type based on the {@code User-Agent} header.
     *
     * @param request HTTP servlet request
     * @return detected device name or {@code "Unknown"}
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);

        if (userAgent == null || userAgent.isBlank()) {
            return "Unknown";
        }

        return DEVICES.stream()
                .filter(userAgent::contains)
                .findFirst()
                .orElse("Unknown");
    }

    /**
     * Resolves the client IP address.
     *
     * <p>If the {@code X-Forwarded-For} header is present,
     * the first IP in the list is returned. Otherwise,
     * {@link HttpServletRequest#getRemoteAddr()} is used.</p>
     *
     * @param request HTTP servlet request
     * @return client IP address
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String header = request.getHeader("X-Forwarded-For");

        if (header != null && !header.isBlank()) {
            return header.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    /**
     * Returns the {@code Host} header value.
     *
     * @param request HTTP servlet request
     * @return host header or {@code null}
     */
    public static String getHost(HttpServletRequest request) {
        return request.getHeader("Host");
    }

    /**
     * Returns the {@code User-Agent} header value.
     *
     * @param request HTTP servlet request
     * @return user agent string or {@code null}
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * Reads the request body as a UTF-8 string.
     *
     * <p><b>Warning:</b> The request input stream can be read only once.
     * Use this method carefully, preferably in filters or controllers
     * designed for body access.</p>
     *
     * @param request HTTP servlet request
     * @return request body as string
     * @throws IOException if reading fails
     */
    public static String getBody(HttpServletRequest request) throws IOException {
        return new String(
                request.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );
    }
}
