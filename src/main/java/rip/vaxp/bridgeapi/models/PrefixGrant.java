package rip.vaxp.bridgeapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Data @AllArgsConstructor @NoArgsConstructor @Document(collection = "prefixgrants")
public class PrefixGrant{
    @Id private String id;
    @Indexed private String uuid;
    @Indexed private String reason;
    @Indexed private String prefix;
    @Indexed private long expiresIn;
    @Indexed private long addedAt;
    @Indexed private String addedBy;
    @Indexed private String addedByIp;
    @Indexed private String removedBy = null;
    @Indexed private String removedByIp = null;
    @Indexed private long removedAt = 0;
    @Indexed private String removalReason = null;
    @Indexed private List<String> scopes = new ArrayList<>();

    public PrefixGrant(String uuid, String reason, String prefix, List<String> scopes, long expiresIn, long addedAt, String addedBy, String addedByIp){
        this.uuid = uuid;
        this.reason = reason;
        this.prefix = prefix;
        this.scopes = scopes;
        this.expiresIn = expiresIn;
        this.addedAt = addedAt;
        this.addedBy = addedBy;
        this.addedByIp = addedByIp;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("uuid", uuid);
        json.put("reason", reason);
        json.put("prefix", prefix);
        json.put("scopes", scopes);
        json.put("expiresAt", expiresIn == -1 ? 0 : expiresIn * 1000);
        json.put("addedAt", addedAt);

        if(addedBy != null && addedByIp != null) {
            json.put("addedBy", addedBy);
            json.put("addedByIp", addedByIp);
        }

        if(removedAt != 0){
            json.put("removedBy", removedBy);
            json.put("removedByIp", removedByIp);
            json.put("removalReason", removalReason);
            json.put("removedAt", removedAt);
        }
        return json;
    }
}
