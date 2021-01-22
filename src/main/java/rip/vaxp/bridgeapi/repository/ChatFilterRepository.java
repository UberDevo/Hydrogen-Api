package rip.vaxp.bridgeapi.repository;

import rip.vaxp.bridgeapi.models.ChatFilter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ChatFilterRepository extends MongoRepository<ChatFilter, String> {
}
