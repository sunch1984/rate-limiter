package com.self.ratelimiter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.google.common.util.concurrent.RateLimiter;

@Component
public class RequestThrottleFilter implements Filter {

	final RateLimiter rateLimiter = RateLimiter.create(100);

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		if (rateLimiter.tryAcquire(1)) {
			chain.doFilter(request, response);
		} else {
			httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			httpServletResponse.getWriter().write("Too many requests");
		}

	}
}
