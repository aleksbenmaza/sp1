package org.aaa.core.web.api.model.input.databinding.sinister;

import org.aaa.core.web.app.model.Command;
import com.google.gson.annotations.SerializedName;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * Created by alexandremasanes on 21/06/2017.
 */

public abstract class SinisterSubmission implements Serializable {

    private static final long serialVersionUID = -7277777698253199398L;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date date;

    protected Time time;

    protected String comment;

    @SerializedName("report_document")
    protected byte[] reportDocument;

    public boolean isEmpty() {
        return date == null && time == null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public byte[] getReportDocument() {
        return reportDocument;
    }

    public void setReportDocument(byte[] reportDocument) {
        this.reportDocument = reportDocument;
    }
}
