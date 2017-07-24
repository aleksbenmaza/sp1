package core.web.model.databinding.command.sinister;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class AccidentSubmission extends SinisterSubmission {

    @SerializedName("registration_number")
    private String registrationNumber;

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
}