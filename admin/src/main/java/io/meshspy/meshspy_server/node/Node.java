package io.meshspy.meshspy_server.node;

public class Node {
    private String id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    // true if this node represents a local client connected via USB
    private boolean client;

    public Node() {
    }

    public Node(String id, String name, String address, Double latitude, Double longitude) {
        this(id, name, address, latitude, longitude, false);
    }

    public Node(String id, String name, String address, Double latitude, Double longitude, boolean client) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.client = client;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isClient() {
        return client;
    }

    public void setClient(boolean client) {
        this.client = client;
    }
}
