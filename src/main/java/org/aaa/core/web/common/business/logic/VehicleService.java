package org.aaa.core.web.common.business.logic;

import org.aaa.core.business.mapping.Make;
import org.aaa.core.business.mapping.Model;
import org.aaa.core.business.mapping.ModelAndYear;
import org.aaa.core.web.common.model.extern.output.ToEntitiesConvertible;
import org.aaa.core.web.common.model.extern.output.edmunds.MakeDTO;
import org.aaa.core.web.common.model.extern.output.edmunds.MakesDTO;
import org.aaa.util.ImportSources;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.List;

import static org.aaa.core.business.mapping.IdentifiableById.NULL_ID;

/**
 * Created by alexandremasanes on 04/04/2017.
 */
@Service
public class VehicleService extends BaseService {

    @Value("vehicleService.documentsDir.idCard")
    private String registrationDocumentDir;


    @Value("#{@importSourcesGetter.forMake}")
    private ImportSources.DomainObject.Source[] importSources;

    public String getRegistrationDocumentDir() {
        return registrationDocumentDir;
    }

    public List<Make> getMakesByName(String name) {
        return dao.searchMakes(name);
    }

    public List<Model> getModelsByNameAndMakeId(String name, long id) {
        return id == NULL_ID ? dao.searchModels(name) : dao.searchModels(name, id);
    }

    @Scheduled(cron = "0 0 * * * *")
    protected void importMakes() throws URISyntaxException {
        RestTemplate restTemplate;
        String path;
        MultiValueMap<String, String> uriParams;
        ResponseEntity<MakeDTO> response;
        HttpStatus httpStatus;
        ImportSources.DomainObject.Source.Key key;
        ImportSources.DomainObject.Source.URI uri;
        MakesDTO makesDTO;
        Model existingModel;


        restTemplate = new RestTemplate();

        for(ImportSources.DomainObject.Source importSource : importSources) {
            final URIBuilder uriBuilder;
            System.out.println(importSource.getName());
            System.out.println(importSource.getUri().getPath());
            uri = importSource.getUri();

            key = importSource.getKey();

            uriBuilder = new URIBuilder(uri.getPath()).setHost(uri.getHost()).setScheme("http" + (uri.isSecured() ? "s" : "") );

            uri.getParams().forEach(
                    (k, v) -> uriBuilder.addParameter(k, (String) v)
            );

            if(key.getMode() == ImportSources.DomainObject.Source.Key.Mode.IN_URI)
                uriBuilder.addParameter(key.getName(), key.getValue());


            try {
                makesDTO = (MakesDTO) restTemplate.getForEntity(uriBuilder.build(), importSource.getOutputClass()).getBody();
                for(Make make : makesDTO.toEntities())
                    if(!dao.hasMake(make.getName()))
                        dao.save(make);
                    else
                        for(Model model : make.getModels()) {
                            if((existingModel = dao.findModel(model.getName())) == null)
                                dao.save(model);
                            else
                                outer:
                                for(ModelAndYear modelAndYear : model.getModelsAndYears()) {
                                    for (ModelAndYear existingModelAndYear : existingModel.getModelsAndYears())
                                        if (modelAndYear.getYear() == existingModelAndYear.getYear())
                                            continue outer;
                                    new ModelAndYear(existingModel, modelAndYear.getYear());
                                    dao.save(existingModel);
                                }

                        }
            } catch(HttpClientErrorException e) {
                System.err.println(
                        "Failed to import data : \n\t headers : " +
                            e.getResponseHeaders() +
                        "\n Status : " +
                                e.getStatusText()
                );
            }
        }
    }

    @PostConstruct
    protected void init() throws URISyntaxException {
        System.out.println("importMakes()");
        importMakes();
    }
}