package instrumental.kiwi.security.aspect;

import instrumental.kiwi.exception.BackendException;
import instrumental.kiwi.security.annotation.WithRateLimitProtection;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static instrumental.kiwi.exception.MsgCode.OOPS_ERROR;

@Aspect
@Component
public class RateLimitAspect {

    public static final String ERROR_MESSAGE =
            """
            Too many requests at endpoint %s from IP %s! Please try again after %d milliseconds.
            """;

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Long>> requestCounts = new ConcurrentHashMap<>();

    @Value("${APP_RATE_LIMIT:10}")
    private int rateLimit;

    @Value("${APP_RATE_DURATION_IN_MS:1000}")
    private long rateDuration;

    @Before("@annotation(withRateLimitProtection)")
    public void rateLimit(WithRateLimitProtection withRateLimitProtection) {

        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();

        final String key = requestAttributes.getRequest().getRemoteAddr();
        final long currentTime = System.currentTimeMillis();

        requestCounts.putIfAbsent(key, new CopyOnWriteArrayList<>());
        CopyOnWriteArrayList<Long> timestamps = requestCounts.get(key);
        timestamps.add(currentTime);
        cleanUpRequestCounts(key, currentTime);

        if (timestamps.size() > rateLimit) {
            System.out.println(ERROR_MESSAGE);
            throw new BackendException(OOPS_ERROR);
        }
    }

    private void cleanUpRequestCounts(String key, long currentTime) {
        CopyOnWriteArrayList<Long> timestamps = requestCounts.get(key);
        timestamps.removeIf(timestamp -> currentTime - timestamp > rateDuration);
    }
}
