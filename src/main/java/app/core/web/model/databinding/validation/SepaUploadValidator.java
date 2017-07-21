package app.core.web.model.databinding.validation;

import static app.core.web.model.databinding.validation.Validating.COMMON_MESSAGE_BASE_CODE;

import app.core.web.logic.helper.MessageHelper;
import app.core.web.model.databinding.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.net.URLConnection.guessContentTypeFromStream;
import static java.util.Arrays.asList;

/**
 * Created by alexandremasanes on 20/06/2017.
 */
@Component
public class SepaUploadValidator {

    @Value("${file.maxSize}")
    private long maxFileSize;

    @Autowired
    private MessageHelper messageHelper;


    public void validate(byte[] sepa, Appendable errorMessage) throws IOException {
        String ext;
        ByteArrayInputStream byteArrayInputStream;



        if(sepa.length > maxFileSize)
            errorMessage.append(messageHelper.getMessage(COMMON_MESSAGE_BASE_CODE + "tooLargeFile"));

        else {
            byteArrayInputStream = new ByteArrayInputStream(sepa);
            ext = guessContentTypeFromStream(byteArrayInputStream);
            if (ext.equals("pdf"))
                errorMessage.append(messageHelper.getMessage(COMMON_MESSAGE_BASE_CODE + "invalidFileExtension"));
        }

    }
}