package rip.vaxp.bridgeapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
@Data @AllArgsConstructor @NoArgsConstructor @Document(collection = "punishments")
public class Punishment {
    @Id private String id;
    @Indexed private String uuid;
    @Indexed private String userIp;
    @Indexed private String publicReason;
    @Indexed private String privateReason;
    @Indexed private String type;
    @Indexed private String actorType;
    @Indexed private String actorName;
    @Indexed private String addedBy;
    @Indexed private String addedByIp;
    @Indexed private long addedAt;
    @Indexed private long expiresAt;
    @Indexed private String removedBy = null;
    @Indexed private String removedByIp = null;
    @Indexed private long removedAt = 0;
    @Indexed private String removalReason = null;
    @Indexed private Map<String, String> metadata;

    public Punishment(String uuid, String userIp, String publicReason, String privateReason, String type, String actorType, String actorName, String addedBy, String addedByIp, long addedAt, long expiresAt, Map<String, String> metadata){
        this.uuid = uuid;
        this.userIp = userIp;
        this.publicReason = publicReason;
        this.privateReason = privateReason;
        this.type = type;
        this.actorType = actorType;
        this.actorName = actorName;
        this.addedBy = addedBy;
        this.addedByIp = addedByIp;
        this.addedAt = addedAt;
        this.expiresAt = expiresAt;
        this.metadata = metadata;
    }

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        json.put("id", uuid);
        json.put("publicReason", publicReason);
        json.put("privateReason", privateReason);
        json.put("type", type);
        json.put("actorType", actorType);
        json.put("actorName", actorName);
        json.put("expiresAt", expiresAt == -1 ? 0 : expiresAt * 1000);
        System.out.println(expiresAt * 1000 + " " + System.currentTimeMillis());
        json.put("addedAt", addedAt);

        if(addedBy != null)
            json.put("addedBy", addedBy);

        if(removedAt != 0) {
            json.put("removedBy", removedBy);
            json.put("removedAt", removedAt);
            json.put("removalReason", removalReason);
        }

        return json;
    }

}
