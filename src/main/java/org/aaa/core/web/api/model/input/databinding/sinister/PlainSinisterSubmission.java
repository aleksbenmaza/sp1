package org.aaa.core.web.api.model.input.databinding.sinister;

/**
 * Created by alexandremasanes on 21/06/2017.
 */
public class PlainSinisterSubmission extends SinisterSubmission {

    private Long typeId;

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && typeId == null;
    }
}
