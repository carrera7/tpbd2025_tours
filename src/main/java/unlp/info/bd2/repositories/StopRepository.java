package unlp.info.bd2.repositories;

import unlp.info.bd2.model.Stop;

import java.util.Optional;

public interface StopRepository {
    Optional<Stop> findByName(String name);
    Stop save(Stop stop);
}