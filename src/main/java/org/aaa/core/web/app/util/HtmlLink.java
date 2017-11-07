package org.aaa.core.web.app.util;

/**
 * Created by alexandremasanes on 20/02/2017.
 */
public final class HtmlLink {

    private String url;
    private String name;

    public HtmlLink(){

    }

    public HtmlLink(String url, String name){
        this.url  = url;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return "<a href='"+url+"'>"+name+"</a>";
    }
}
