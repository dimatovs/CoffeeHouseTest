package Events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonAutoDetect
public abstract class OrderEvent {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private int orderId;
    @JsonIgnore
    private String type;
    public final String created = LocalDateTime.now().toString();
    public final UUID employeeId = UUID.randomUUID();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCreated() {
        return created;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public String getType() {
        return "none";
    }

    public void setType(String type) {
        this.type = type;
    }

}
