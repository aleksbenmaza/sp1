package org.aaa.core.web.api.model.input.databinding;

import com.google.gson.annotations.SerializedName;
import org.aaa.core.web.app.model.Command;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by alexandremasanes on 31/03/2017.
 */
public class ContractSubmission implements Command {

    @SerializedName("insurance_id")
    private Long insuranceId;

    @SerializedName("model_id")
    private Long modelId;

    @SerializedName("vin_number")
    private String vinNumber;

    @SerializedName("registration_number")
    private String registrationNumber;

    @SerializedName("purchase_date")
    private Date purchaseDate;

    @SerializedName("registration_document")
    private byte[] registrationDocument;

    @Override
    public boolean isEmpty() {
        return insuranceId          == null &&
               modelId              == null &&
              (vinNumber            == null || vinNumber.isEmpty()) &&
              (registrationNumber   == null || registrationNumber.isEmpty()) &&
               purchaseDate         == null &&
              (registrationDocument == null || registrationDocument.length == 0)
        ;
    }

    public Long getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(Long insuranceId) {
        this.insuranceId = insuranceId;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getVinNumber() {
        return vinNumber;
    }

    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public byte[] getRegistrationDocument() {
        return registrationDocument;
    }

    public void setRegistrationDocument(byte[] registrationDocument) {
       this.registrationDocument = registrationDocument;
    }

    @Override
    public String toString() {
        return "ContractSubmission{" +
                "insuranceId=" + insuranceId +
                ", modelId=" + modelId +
                ", vinNumber='" + vinNumber + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", registrationDocument=" + Arrays.toString(registrationDocument) +
                '}';
    }
}
