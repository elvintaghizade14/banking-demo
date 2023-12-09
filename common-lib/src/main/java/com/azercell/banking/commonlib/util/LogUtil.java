package com.azercell.banking.commonlib.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogUtil {

    public static String getParams(final String[] paramNames, final Object[] paramValues) {
        return IntStream.range(0, paramNames.length)
                .mapToObj(i -> paramNames[i] + "=" + paramValues[i])
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static Map<String, Object> getParamsAsMap(final String[] paramNames, final Object[] paramValues) {
        final Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            params.put(paramNames[i], paramValues[i]);
        }
        return params;
    }

    public static String toShortString(Object obj) {
        return toShortString(obj, 255);
    }

    public static String toShortString(Object obj, int length) {
        if (obj == null) {
            return null;
        }
        final String str = obj.toString();
        int minLength = Math.max(length, 100);
        return Optional.of(str)
                .filter(s -> s.length() > minLength)
                .map(s -> s.substring(0, 20) + " ... " + s.substring(str.length() - 20))
                .orElse(str);
    }

    public static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        final String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null || contextPath.isBlank()) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        log.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\t{}://localhost:{}{}\n\t"
                        + "External: \t{}://{}:{}{}\n\t"
                        + "Profile(s): \t{}"
                        + "\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

}
