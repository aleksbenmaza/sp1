package org.aaa.core.web.api.model.input.databinding.sinister;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class AccidentSubmission extends SinisterSubmission {

    private static final long serialVersionUID = -43271725205046988L;

    @SerializedName("registration_number")
    private String registrationNumber;

    @SerializedName("vin_number")
    private String vinNumber;

    @SerializedName("nir_number")
    private String nirNumber;

    @SerializedName("model_id")
    private Integer modelId;

    private Short year;

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

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getNirNumber() {
        return nirNumber;
    }

    public void setNirNumber(String nirNumber) {
        this.nirNumber = nirNumber;
    }
}