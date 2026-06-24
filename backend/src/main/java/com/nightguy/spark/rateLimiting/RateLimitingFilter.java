package com.nightguy.spark.rateLimiting;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String clientIp = getClientIp(request);
        Bucket tokenBucket = rateLimitingService.resolveBucket(clientIp);

        var probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if(probe.isConsumed()){
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            var waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));

            String jsonResponse = """
                {
                    "status": %s,
                    "error": "Too Many Requests",
                    "message": "You have exhausted your API Request Quota",
                    "retryAfterSeconds": %s
                }
                """.formatted(HttpStatus.TOO_MANY_REQUESTS.value(), waitForRefill);

            response.getWriter().write(jsonResponse);
        }
    }

    private String getClientIp(HttpServletRequest request){
        //Check for X-Forwarded-For
        String xfHeader = request.getHeader("X-Forwarded-For");
        if(xfHeader==null || xfHeader.isEmpty()){
            return request.getRemoteAddr();
        }

        return xfHeader.split(",")[0].trim();
    }
}
