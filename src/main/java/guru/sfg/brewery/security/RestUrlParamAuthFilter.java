package guru.sfg.brewery.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class RestUrlParamAuthFilter extends AbstractRestAuthFilter {

    public RestUrlParamAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    protected String getUsername(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("apiKey");
    }

    @Override
    protected String getPassword(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getParameter("apiSecret");
    }
}
