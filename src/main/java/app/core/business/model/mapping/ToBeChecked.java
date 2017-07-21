package app.core.business.model.mapping;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Created by alexandremasanes on 01/03/2017.
 */
@MappedSuperclass
public interface ToBeChecked {

    enum Status {

        VALID(0x7CFC00),

        AWAITING(0xFF7F50),

        INVALID(0xDC143C);

        private int color;

        Status(int color){
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    void setStatus(Status status);

    Status getStatus();

    static <T extends ToBeChecked> T requireNonNull(T toBeChecked) {
        return Objects.requireNonNull(toBeChecked);
    }
}
