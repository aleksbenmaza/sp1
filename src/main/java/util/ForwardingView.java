package util;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by alexandremasanes on 19/07/2017.
 */
public class ForwardingView extends AbstractUrlBasedView {

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
        httpServletRequest.setAttribute("action", getUrl());

        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            httpServletRequest.setAttribute((String) entry.getKey(), entry.getValue());
        }
        httpServletResponse.setStatus(httpStatus.value());
        httpServletRequest.getRequestDispatcher(getUrl()).forward(httpServletRequest, httpServletResponse);
    }
}