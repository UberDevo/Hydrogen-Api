package rip.vaxp.bridgeapi.repository;

import rip.vaxp.bridgeapi.models.ServerGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ServerGroupRepository extends MongoRepository<ServerGroup, String> {
}
