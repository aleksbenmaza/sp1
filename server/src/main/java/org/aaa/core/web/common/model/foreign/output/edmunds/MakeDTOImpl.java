package org.aaa.core.web.common.model.foreign.output.edmunds;

import com.google.gson.annotations.SerializedName;

import org.aaa.core.business.mapping.entity.Make;
import org.aaa.core.business.mapping.entity.Model;
import org.aaa.core.business.mapping.entity.Year;
import org.aaa.core.web.common.model.foreign.output.MakeDTO;
import org.aaa.core.web.common.model.foreign.output.ModelDTO;
import org.aaa.core.web.common.model.foreign.output.ToEntityConvertible;
import org.aaa.core.web.common.model.foreign.output.YearDTO;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by alexandremasanes on 25/08/2017.
 */
public class MakeDTOImpl implements MakeDTO, ToEntityConvertible<Make>, Serializable {

    public static class ModelDTOImpl implements ModelDTO, Serializable {

        @Override
        public Model toEntity() {
            return new Model(name, makeDTO.toEntity());
        }

        public static class YearDTOImpl implements YearDTO, Serializable {

            private long id;

            @SerializedName("year")
            private short value;

            public long getId() {
                return id;
            }

            @Override
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

            @Override
            public Year toEntity() {
                return new Year(value);
            }
        }

        private String id;

        private String name;

        private String niceName;

        private MakeDTO makeDTO;

        @SerializedName("years")
        private YearDTOImpl[] yearDTOs;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getNiceName() {
            return niceName;
        }

        @Override
        public YearDTOImpl[] getYearDTOs() {
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
    private ModelDTOImpl[] modelDTOs;

    @Override
    public Make toEntity() {
        Make make;
        Model model;
        Year year;

        make = new Make(name);

        make.setName(name);

        for(ModelDTOImpl modelDTO : modelDTOs) {
            model = new Model(modelDTO.name, make);
            make.addModel(model);

            for (ModelDTOImpl.YearDTOImpl yearDTO : modelDTO.yearDTOs) {
                year = new Year(yearDTO.value);
                model.addYear(year);
            }
        }
        return make;
    }

    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ModelDTOImpl[] getModelDTOs() {
        Arrays.stream(modelDTOs).forEach(modelDTO -> modelDTO.makeDTO = this);
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