package unlp.info.bd2.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import unlp.info.bd2.model.Stop;

import java.util.Optional;

@Repository
public class StopRepositoryImpl implements StopRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Stop> findByName(String name) {
        String query = "SELECT s FROM Stop s WHERE s.name = :name";
        Stop stop = entityManager.createQuery(query, Stop.class)
                                 .setParameter("name", name)
                                 .getResultStream()
                                 .findFirst()
                                 .orElse(null);
        return Optional.ofNullable(stop);
    }

    @Override
    public Stop save(Stop stop) {
        entityManager.persist(stop);        
        
        return stop; 
    }
}