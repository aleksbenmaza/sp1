package org.aaa.core.web.app.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by alexandremasanes on 19/07/2017.
 */
public final class ForwardingView extends AbstractUrlBasedView {

    private HttpStatus httpStatus;

    public ForwardingView() {
        this("", HttpStatus.OK);
    }

    public ForwardingView(String uri) {
        super(uri);
    }

    public ForwardingView(String uri, HttpStatus httpStatus) {
        this(uri);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> map,
            HttpServletRequest  httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws Exception {
        String internalUrl;

        internalUrl = httpServletRequest.getServletPath() + getUrl();
        httpServletRequest.setAttribute("action", internalUrl);

        for (Map.Entry<String, Object> entry : map.entrySet())
            httpServletRequest.setAttribute(
                    entry.getKey(),
                    entry.getValue()
            );

        httpServletResponse.setStatus(httpStatus.value());
        httpServletRequest.getRequestDispatcher(internalUrl).forward(httpServletRequest, httpServletResponse);
    }
}