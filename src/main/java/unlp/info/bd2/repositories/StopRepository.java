package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import unlp.info.bd2.model.Stop;

public interface StopRepository {
    Optional<Stop> findByName(String name);
    Stop save(Stop stop);
    List<Stop> findByNameStartingWith(String prefix);
}