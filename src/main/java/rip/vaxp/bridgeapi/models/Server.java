package rip.vaxp.bridgeapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
@Data @AllArgsConstructor @NoArgsConstructor
public class Server {
    private String id;
    private String displayName;
    private String serverGroup;
    private String serverIp;
    private long lastUpdatedAt;
    private double lastTps;

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("displayName", displayName);
        json.put("serverGroup", serverGroup);
        json.put("serverIp", serverIp);
        json.put("lastUpdatedAt", lastUpdatedAt);
        json.put("lastTps", lastTps);
        return json;
    }

}
