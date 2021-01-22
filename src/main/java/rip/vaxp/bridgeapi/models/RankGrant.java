package rip.vaxp.bridgeapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
@Data @AllArgsConstructor @NoArgsConstructor @Document(collection = "grants")
public class RankGrant {
    @Id private String id;
    @Indexed private String uuid;
    @Indexed private String reason;
    @Indexed private String rank;
    @Indexed private long expiresIn;
    @Indexed private long addedAt;
    @Indexed private String addedBy;
    @Indexed private String addedByIp;
    @Indexed private String removedBy;
    @Indexed private String removedByIp;
    @Indexed private long removedAt = 0;
    @Indexed private String removalReason;
    @Indexed private List<String> scopes;

    public RankGrant(String uuid, String reason, String rank, List<String> scopes, long expiresIn, long addedAt, String addedBy, String addedByIp){
        this.uuid = uuid;
        this.reason = reason;
        this.rank = rank;
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
        json.put("rank", rank);
        json.put("scopes", scopes);
        json.put("expiresAt", expiresIn == -1 ? 0 : expiresIn * 1000);
        json.put("addedAt", addedAt * 1000);

        if(addedBy != null && addedByIp != null) {
            json.put("addedBy", addedBy);
            json.put("addedByIp", addedByIp);
        }

        if(removedAt != 0){
            json.put("removedBy", removedBy);
            json.put("removedByIp", removedByIp);
            json.put("removalReason", removalReason);
            json.put("removedAt", removedAt * 1000);
        }

        return json;
    }

}
