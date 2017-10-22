package org.aaa.util;

import org.springframework.http.HttpStatus;

/**
 * Created by alexandremasanes on 28/02/2017.
 */
public final class RedirectView extends org.springframework.web.servlet.view.RedirectView {

    private HttpStatus httpStatus;

    public RedirectView() {
        super("", true);
    }

    public RedirectView(String url){
        this(url, HttpStatus.FOUND);
    }

    public RedirectView(String url, HttpStatus httpStatus){
        super(url);
        setStatusCode(httpStatus);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
