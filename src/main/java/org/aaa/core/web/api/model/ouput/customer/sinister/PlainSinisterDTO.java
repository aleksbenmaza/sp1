package org.aaa.core.web.api.model.ouput.customer.sinister;

import org.aaa.core.business.mapping.entity.Coverage;
import org.aaa.core.business.mapping.entity.sinister.PlainSinister;
import org.aaa.core.business.mapping.entity.PlainSinisterType;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class PlainSinisterDTO extends SinisterDTO {

    public static class TypeDTO extends DTO<PlainSinisterType> {

        private long id;

        private String code;

        private String name;

        private HashMap<Long, Coverage> insuranceIdsCovergages;

        public TypeDTO(PlainSinisterType type) {
            super(type);
            id   = type.getId();
            code = type.getCode();
            name = type.getName();
        }
    }

    @SerializedName("type_name")
    private String typeName;

    @SerializedName("type")
    private TypeDTO type;

    public PlainSinisterDTO(PlainSinister sinister) {
        super(sinister);
        typeName = sinister.getType().getName();
        type = new TypeDTO(sinister.getType());
    }
}