package pt.isel.si.entity;

public class AccessInfo {
    public String access_token;
    public String token_type;
    public String id_token;
    public int expires_in;

    public AccessInfo(String access_token, String token_type, String id_token, int expires_in) {
        this.access_token = access_token;
        this.token_type = token_type;
        this.id_token = id_token;
        this.expires_in = expires_in;
    }
}
