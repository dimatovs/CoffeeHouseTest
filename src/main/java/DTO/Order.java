package DTO;

import Events.*;

import java.util.LinkedList;

public class Order {

    private int orderId;
    private String status = "none";
    private String clientName;
    private int clientId;
    private String cancelReason;
    private LinkedList<OrderEvent> listOfEvents;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public LinkedList<OrderEvent> getListOfEvents() {
        return listOfEvents;
    }

    public void setListOfEvents(LinkedList<OrderEvent> listOfEvents) {
        this.listOfEvents = listOfEvents;
    }

    public void apply(LinkedList<OrderEvent> listOfEvents) {
        this.setListOfEvents(listOfEvents);
        for(OrderEvent event : listOfEvents) {
            if (event.getType().equals("registered")) {
                applyRegistered((OrderRegistered) event);
            } else if (event.getType().equals("canceled")) {
                applyCanceled((OrderCanceled) event);
            } else if (event.getType().equals("issued")) {
                applyIssued((OrderIssued) event);
            } else if (event.getType().equals("inProgress")) {
                applyInProgress((OrderInProgress) event);
            } else if (event.getType().equals("ready")) {
                applyReady((OrderIsReady) event);
            }
        }
    }

    public void applyRegistered(OrderRegistered event) {
        this.setOrderId(event.getOrderId());
        this.setStatus(event.getType());
        this.setClientName(event.getClientName());
        this.setClientId(event.getClientId());
    }

    public void applyInProgress(OrderInProgress event) {
        this.setStatus(event.getType());
    }

    public void applyReady(OrderIsReady event) {
        this.setStatus(event.getType());
    }

    public void applyIssued(OrderIssued event) {
        this.setStatus(event.getType());
    }

    public void applyCanceled(OrderCanceled event) {
        this.setStatus(event.getType());
        this.setCancelReason(event.getCancellationReason());
    }
}
