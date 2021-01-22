package rip.vaxp.bridgeapi.repository;

import rip.vaxp.bridgeapi.models.Prefix;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixRepository extends MongoRepository<Prefix, String> {

}
