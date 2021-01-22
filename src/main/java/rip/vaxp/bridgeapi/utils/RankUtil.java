package rip.vaxp.bridgeapi.utils;

import rip.vaxp.bridgeapi.repository.RankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RankUtil {

    @Autowired private RankRepository rankRepository;

    public List<String> getRankPermissions(String rank){
        return rankRepository.findByRankid(rank).getPermissions();
    }

}
