package io.meshspy.meshspy_server.manage;

public class FirmwareUpdateRequest {
    private String version;
    private String url;
    private boolean build;

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public boolean isBuild() { return build; }
    public void setBuild(boolean build) { this.build = build; }
}
