package io.meshspy.meshspy_server.request;

public class NodeRegistrationRequest {
    private String id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String model;
    private String firmware;
    private String longName;
    private String shortName;

    public NodeRegistrationRequest() {}

    public NodeRegistrationRequest(String id, String name, String address, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NodeRegistrationRequest(String id, String name, String address, Double latitude, Double longitude,
                                   String model, String firmware, String longName, String shortName) {
        this(id, name, address, latitude, longitude);
        this.model = model;
        this.firmware = firmware;
        this.longName = longName;
        this.shortName = shortName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getFirmware() { return firmware; }
    public void setFirmware(String firmware) { this.firmware = firmware; }

    public String getLongName() { return longName; }
    public void setLongName(String longName) { this.longName = longName; }

    public String getShortName() { return shortName; }
    public void setShortName(String shortName) { this.shortName = shortName; }
}
