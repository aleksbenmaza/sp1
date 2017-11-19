package org.aaa.core.web.common.business.logic.vehicle;

import org.aaa.core.business.mapping.entity.Make;
import org.aaa.core.business.mapping.entity.Model;
import org.aaa.core.business.mapping.entity.Year;
import org.aaa.core.web.common.business.logic.BaseService;
import org.aaa.core.web.common.model.foreign.output.MakeDTO;
import org.aaa.core.web.common.model.foreign.output.MakesDTO;
import org.aaa.core.web.common.model.foreign.output.ModelDTO;
import org.aaa.core.web.common.model.foreign.output.YearDTO;
import org.aaa.core.web.common.model.foreign.output.edmunds.MakeDTOImpl;
import org.aaa.core.web.common.model.foreign.output.edmunds.MakesDTOImpl;
import org.aaa.util.ImportSources;

import org.apache.http.client.utils.URIBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.net.URISyntaxException;
import java.util.List;

import static org.aaa.orm.entity.identifiable.IdentifiableById.NULL_ID;

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

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void importNewMakes() throws URISyntaxException {
        RestTemplate restTemplate;
        String path;
        MultiValueMap<String, String> uriParams;
        ResponseEntity<MakeDTOImpl> response;
        HttpStatus httpStatus;
        ImportSources.DomainObject.Source.Key key;
        ImportSources.DomainObject.Source.URI uri;
        MakesDTO makesDTO;
        Model existingModel;
        Year year;
        Year toBeAddedYear;
        Make make;


        restTemplate = new RestTemplate();

        for(ImportSources.DomainObject.Source importSource : importSources) {
            final URIBuilder uriBuilder;

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
                for(MakeDTO makeDTO : makesDTO.getMakeDTOs())
                    if(!dao.hasMake(makeDTO.getName())) {
                        dao.save(makeDTO.toEntity());
                        System.out.println("Saved make which name is " + makeDTO.getName());
                    } else {
                        for(ModelDTO modelDTO : makeDTO.getModelDTOs()) {
                            if ((existingModel = dao.findModel(modelDTO.getName())) == null) {
                                dao.save(modelDTO.toEntity());
                                System.out.println("Saved model which name is " + modelDTO.getName());
                            } else
                                outer: for(YearDTO yearDTO : modelDTO.getYearDTOs()) {
                                    if(!dao.hasYear(yearDTO.getValue())) {
                                        dao.save(year = yearDTO.toEntity());
                                        System.out.println("Saved year which value is " + yearDTO.getValue());
                                    } else
                                        year = dao.findYear(yearDTO.getValue());

                                    for(Year existingYear : existingModel.getYears())
                                        if (year.equals(existingYear)) {
                                            System.out.println("continue outer");
                                            continue outer;
                                        }

                                    existingModel.addYear(year);
                                    dao.save(year);
                                    System.out.println("Added year wich value is " + yearDTO.getValue() + " to model which name is " + modelDTO.getName());
                                }
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
}