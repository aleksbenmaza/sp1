package core.web.model.domaintransfer.customer.sinister;

import core.business.model.mapping.sinister.PlainSinister;
import core.business.model.mapping.sinister.PlainSinister.Type;
import core.web.model.domaintransfer.Presentation;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class PlainSinisterDTO extends SinisterDTO {

    public static class TypeDTO extends Presentation<Type> {

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
