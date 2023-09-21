package Events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class OrderInProgress extends OrderEvent {

    @Override
    public String getType() {
        return "inProgress";
    }

}
