package org.aaa.core.web.common.helper;

import org.aaa.core.business.mapping.Entity;
import org.aaa.core.business.mapping.Make;
import org.aaa.util.ImportSources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by alexandremasanes on 24/08/2017.
 */

@Component
public class ImportSourcesGetter {

    @Value("${importSource.path}")
    private String importSourcePath;

    @Autowired
    private XMLParser xmlParser;

    private ImportSources importSources;


    @PostConstruct
    protected void init() throws Exception {
        importSources = xmlParser.convertFromXMLToObject(ImportSources.class, importSourcePath);
    }

    public ImportSources.DomainObject.Source[] get(Class<? extends Entity> domainObjectClass) {
        System.out.println(importSources);
        for(ImportSources.DomainObject domainObject : importSources.getDomainObjects())
            if (domainObject.getSubjectClass() == domainObjectClass)
                return domainObject.getSources();
        return null;
    }

    public ImportSources.DomainObject.Source[] getForMake() {
        return get(Make.class);
    }
}