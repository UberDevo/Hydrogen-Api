package rip.vaxp.bridgeapi.controllers;

import rip.vaxp.bridgeapi.WebApi;
import rip.vaxp.bridgeapi.models.Player;
import rip.vaxp.bridgeapi.repository.ChatFilterRepository;
import rip.vaxp.bridgeapi.repository.PlayerRepository;
import rip.vaxp.bridgeapi.repository.RankRepository;
import rip.vaxp.bridgeapi.repository.ServerGroupRepository;
import rip.vaxp.bridgeapi.utils.PlayerUtil;
import rip.vaxp.bridgeapi.utils.RankUtil;
import rip.vaxp.bridgeapi.utils.ServerUtil;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
public class ServerController {

    @Autowired private ServerGroupRepository serverGroupRepository;
    @Autowired private ChatFilterRepository chatFilterRepository;
    @Autowired private RankRepository rankRepository;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private PlayerUtil playerUtil;
    @Autowired private RankUtil rankUtil;

    @GetMapping(path = "/serverGroups")
    public ResponseEntity<List> getServerGroups(){
        List<JSONObject> serverGroups = new ArrayList<>();
        serverGroupRepository.findAll().forEach(serverGroup -> serverGroups.add(serverGroup.toJSON()));
        return new ResponseEntity<>(serverGroups, HttpStatus.OK);
    }

    @GetMapping(path = "/servers")
    public ResponseEntity<Set> getServers(){
        return new ResponseEntity<>(ServerUtil.getServersAsJSON(), HttpStatus.OK);
    }

    @PostMapping(path = "/servers/heartbeat")
    public ResponseEntity<JSONObject> serverHeartbeat(@RequestHeader(value = "MHQ-Authorization") String apiKey, @RequestBody Map<String, Object> body){
        ServerUtil.update(apiKey, body);

        Map<String, Map> onlinePlayers = (Map) body.get("players");

        JSONObject players = new JSONObject();

        onlinePlayers.entrySet().forEach((entry) -> {
            players.put(entry.getKey(), playerUtil.getPlayerByUUID(entry.getKey(), null));

            Player player = playerRepository.findByUuid(entry.getKey());
            player.setOnline(true);
            player.setLastSeenOn(apiKey);
            playerRepository.save(player);

            playerUtil.setOnline(entry.getKey(), apiKey);
        });

        List<Map<String, String>> events = (List) body.get("events");
        events.forEach(event -> {
            if(event.get("type").equals("leave")){
                Player player = playerRepository.findByUuid(event.get("user"));
                player.setOnline(false);
                playerRepository.save(player);
            }
        });

        boolean permissionsNeeded = Boolean.parseBoolean(body.get("permissionsNeeded").toString());
        JSONObject rankPermissions = new JSONObject();
        if(permissionsNeeded) {
            rankRepository.findAll().forEach(rank -> rankPermissions.put(rank.getRankid(), rank.getPermissions()));
        }

        JSONObject response = new JSONObject();
        response.put("players", players);
        response.put("permissions", rankPermissions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/chatFilter")
    public ResponseEntity<Set> getChatFilters(){
        return new ResponseEntity<>(new HashSet<>(chatFilterRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping(path = "/whoami")
    public ResponseEntity<JSONObject> getWhoAmI(@RequestHeader(value = "MHQ-Authorization") String apiKey){
        JSONObject response = new JSONObject();
        response.put("name", apiKey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/dumps/totp")
    public ResponseEntity<Set> getTotpDumps(){
        return new ResponseEntity<>(new HashSet<UUID>(), HttpStatus.OK);
    }

    @PostMapping(path = "/users/disposableLoginTokens")
    public ResponseEntity<JSONObject> getDisposableToken(@RequestBody Map<String, String> body){
        String uuid = body.get("user");
        String ip = body.get("userIp");

        JSONObject json = new JSONObject();

        Player player = playerRepository.findByUuid(uuid);
        if(player.getEmail() == null){
            json.put("success", false);
            json.put("message", "Your profile doesn't have an account.");
            return new ResponseEntity<>(json, HttpStatus.OK);
        }

        String token = UUID.randomUUID().toString();

        // just in case someone wanna write a front end for this
        WebApi.getRedisManager().getJedis().hset("disposableTokens", token, uuid);
        json.put("token", token);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
