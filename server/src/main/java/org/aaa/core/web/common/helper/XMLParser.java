package org.aaa.core.web.common.helper;

/**
 * Created by alexandremasanes on 25/08/2017.
 */

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.aaa.util.ImportSources;
import org.springframework.stereotype.Component;

@Component("xmlParser")
public class XMLParser {

    @SuppressWarnings("unchecked")
    public <T> T convertFromXMLToObject(Class<T> clazz, String xmlfile) throws JAXBException {

        File file;
        JAXBContext jaxbContext;
        T t;

        file = new File(xmlfile);
        jaxbContext = JAXBContext.newInstance(clazz);
        t = (T)jaxbContext.createUnmarshaller().unmarshal(file);

        return t;
    }
}
