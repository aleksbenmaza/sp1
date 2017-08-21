package org.aaa.core.web.api.model.input.databinding.sinister;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class AccidentSubmission extends SinisterSubmission {

    @SerializedName("registration_number")
    private String registrationNumber;

    @SerializedName("model_id")
    private Integer modelId;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() &&
                registrationNumber == null ||
                registrationNumber.isEmpty();
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}