package rip.vaxp.bridgeapi.repository;

import rip.vaxp.bridgeapi.models.Player;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {

    Player findByUuid(String uuid);

}
