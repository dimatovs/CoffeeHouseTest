package Events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class OrderIsReady extends OrderEvent {

    @Override
    public String getType() {
        return "ready";
    }

}
