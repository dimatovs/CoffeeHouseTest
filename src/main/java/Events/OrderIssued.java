package Events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class OrderIssued extends OrderEvent {


    @Override
    public String getType() {
        return "issued";
    }

}
