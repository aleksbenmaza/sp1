package org.aaa.core.web.common.model.extern.output.edmunds;

import com.google.gson.annotations.SerializedName;
import org.aaa.core.business.mapping.Make;
import org.aaa.core.business.mapping.Model;
import org.aaa.core.business.mapping.ModelAndYear;
import org.aaa.core.web.common.model.extern.output.ToEntityConvertible;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by alexandremasanes on 25/08/2017.
 */
public class MakeDTO implements ToEntityConvertible<Make>, Serializable {

    public static class ModelDTO implements Serializable {

        public static class Year implements Serializable {

            private long id;

            @SerializedName("year")
            private short value;

            public long getId() {
                return id;
            }

            public short getValue() {
                return value;
            }
        }

        private String id;

        private String name;

        private String niceName;

        private Year[] years;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getNiceName() {
            return niceName;
        }

        public Year[] getYears() {
            return years;
        }

        @Override
        public String toString() {
            return "ModelDTO{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", niceName='" + niceName + '\'' +
                    ", years=" + Arrays.toString(years) +
                    '}';
        }
    }

    private long id;

    private String name;

    @SerializedName("models")
    private ModelDTO[] modelDTOs;

    @Override
    public Make toEntity() {
        Make make;
        Model model;

        make = new Make();

        make.setName(name);

        for(ModelDTO modelDTO : modelDTOs) {
            model = new Model(make);
            model.setName(modelDTO.getName());
            for (ModelDTO.Year year : modelDTO.years)
                new ModelAndYear(model, year.value);
        }
        return make;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ModelDTO[] getModelDTOs() {
        return modelDTOs;
    }

    @Override
    public String toString() {
        return "MakeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", modelDTOs=" + Arrays.toString(modelDTOs) +
                '}';
    }
}
