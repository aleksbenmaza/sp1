package org.aaa.core.web.api.model.ouput.customer.sinister;

import org.aaa.core.business.mapping.sinister.PlainSinister;
import org.aaa.core.business.mapping.sinister.PlainSinister.Type;
import org.aaa.core.web.api.model.ouput.DTO;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class PlainSinisterDTO extends SinisterDTO {

    public static class TypeDTO extends DTO<Type> {

        private long id;

        private String code;

        private String name;

        public TypeDTO(Type type) {
            super(type);
            id   = type.getId();
            code = type.getCode();
            name = type.getName();
        }
    }

    @SerializedName("type_name")
    private String typeName;

    public PlainSinisterDTO(PlainSinister sinister) {
        super(sinister);
        typeName = sinister.getType().getName();
    }
}
