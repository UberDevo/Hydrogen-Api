package rip.vaxp.bridgeapi.repository;

import rip.vaxp.bridgeapi.models.RankGrant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RankGrantRepository extends MongoRepository<RankGrant, String> {

    RankGrant findById(String id);
    List<RankGrant> findByUuid(String uuid);

}
