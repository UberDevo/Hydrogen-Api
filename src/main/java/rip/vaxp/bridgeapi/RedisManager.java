package rip.vaxp.bridgeapi;

import lombok.Getter;
import redis.clients.jedis.Jedis;

public class RedisManager {

    @Getter
    private Jedis jedis;

    public boolean init() {
        jedis = new Jedis(WebApi.getSettingsManager().getSettings().get("redis-host"));
        jedis.connect();
        System.out.println("Connected to redis");
        return true;
    }

}
