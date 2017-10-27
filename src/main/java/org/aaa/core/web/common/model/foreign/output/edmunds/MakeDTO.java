package org.aaa.core.web.common.model.foreign.output.edmunds;

import com.google.gson.annotations.SerializedName;

import org.aaa.core.business.mapping.entity.Make;
import org.aaa.core.business.mapping.entity.Model;
import org.aaa.core.business.mapping.entity.Year;
import org.aaa.core.web.common.model.foreign.output.ToEntityConvertible;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by alexandremasanes on 25/08/2017.
 */
public class MakeDTO implements ToEntityConvertible<Make>, Serializable {

    public static class ModelDTO implements Serializable {

        public static class YearDTO implements Serializable {

            private long id;

            @SerializedName("year")
            private short value;

            public long getId() {
                return id;
            }

            public short getValue() {
                return value;
            }

            @Override
            public String toString() {
                return getClass().getName() + "{" +
                        "id=" + id +
                        ", value=" + value +
                        '}';
            }
        }

        private String id;

        private String name;

        private String niceName;

        @SerializedName("years")
        private YearDTO[] yearDTOs;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getNiceName() {
            return niceName;
        }

        public YearDTO[] getYearDTOs() {
            return yearDTOs;
        }

        @Override
        public String toString() {
            return getClass().getName() + "{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", niceName='" + niceName + '\'' +
                    ", years=" + Arrays.toString(yearDTOs) +
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
        Year year;

        make = new Make();

        make.setName(name);

        for(ModelDTO modelDTO : modelDTOs) {
            model = new Model(make);
            model.setName(modelDTO.getName());
            make.addModel(model);

            for (ModelDTO.YearDTO yearDTO : modelDTO.yearDTOs) {
                year = new Year();
                year.setValue(yearDTO.value);
                model.addYear(year);
            }
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
        return getClass().getName() + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", modelDTOs=" + Arrays.toString(modelDTOs) +
                '}';
    }
}