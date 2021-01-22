package rip.vaxp.bridgeapi.utils;

import rip.vaxp.bridgeapi.WebApi;
import rip.vaxp.bridgeapi.models.Player;
import rip.vaxp.bridgeapi.models.Punishment;
import rip.vaxp.bridgeapi.repository.*;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rip.vaxp.bridgeapi.repository.PlayerRepository;
import rip.vaxp.bridgeapi.repository.PrefixGrantRepository;
import rip.vaxp.bridgeapi.repository.PunishmentRepository;
import rip.vaxp.bridgeapi.repository.RankGrantRepository;

import java.util.ArrayList;
import java.util.List;
@Service
public class PlayerUtil {

    @Autowired private PlayerRepository playerRepository;
    @Autowired private PunishmentRepository punishmentRepository;
    @Autowired private RankGrantRepository rankGrantRepository;
    @Autowired private PrefixGrantRepository prefixGrantRepository;

    public JSONObject getPlayerByUUID(String uuid, String username){
        Player player = playerRepository.findByUuid(uuid);

        if(player == null){
            player = new Player();
            player.setUuid(uuid);
            player.setUsername(username);
            player.setLastSeenAt(System.currentTimeMillis() / 1000);

            playerRepository.save(player);
        }

        JSONObject json = player.toJSON();

        punishmentRepository.findByUuid(uuid).forEach(punishment -> {
            JSONObject access = new JSONObject();

            if(punishment.getRemovedAt() == 0 && punishment.getType().equals("BLACKLIST")){
                access.put("allowed", false);
                access.put("message", WebApi.getSettingsManager().getSettings().get("blacklist-message"));
            } else if(activePunishment(punishment) && punishment.getType().equals("BAN")){
                access.put("allowed", false);
                access.put("message", WebApi.getSettingsManager().getSettings().get("ban-message"));
            } else if(activePunishment(punishment) && punishment.getType().equals("MUTE")){
                json.put("mute", punishment.toJSON());
            }

            if(!access.isEmpty())
                json.put("access", access);
        });

        JSONObject scopeRanks = new JSONObject();
        List<String> ranks = new ArrayList<>();
        rankGrantRepository.findByUuid(uuid).forEach(rankGrant -> {
            if(rankGrant.getRemovedAt() == 0) {
                scopeRanks.put(rankGrant.getRank(), rankGrant.getScopes());
                ranks.add(rankGrant.getRank());
            }
        });
        json.put("scopeRanks", scopeRanks);

        // Add the default rank if they don't have a rank already
        if(ranks.size() == 0)
            ranks.add("default");

        json.put("ranks", ranks);

        List<String> prefixes = new ArrayList<>();
        prefixGrantRepository.findByUuid(uuid).forEach(prefixGrant -> {
            if(prefixGrant.getRemovedAt() == 0)
                prefixes.add(prefixGrant.getPrefix());
        });
        json.put("prefixes", prefixes);

        return json;
    }

    public void logIp(String uuid, String ip){
        Player player = playerRepository.findByUuid(uuid);

        if(!player.getIpLog().contains(ip))
            player.getIpLog().add(ip);

        playerRepository.save(player);
    }

    public void setOnline(String uuid, String servername){
        Player player = playerRepository.findByUuid(uuid);
        player.setOnline(true);
        player.setLastSeenOn(servername);
        playerRepository.save(player);
    }

    private boolean activePunishment(Punishment punishment){
        // unbanned
        if(punishment.getRemovedAt() != 0)
            return false;

        // perm ban
        if(punishment.getExpiresAt() == -1)
            return true;

        // tempban
        return System.currentTimeMillis() / 1000 < punishment.getExpiresAt();
    }
}
