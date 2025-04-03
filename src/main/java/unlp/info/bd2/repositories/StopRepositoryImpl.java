package unlp.info.bd2.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import unlp.info.bd2.model.Stop;

@Repository
public class StopRepositoryImpl implements StopRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Stop> findByName(String name) {
        String query = "SELECT s FROM Stop s WHERE s.name = :name";
        return entityManager.createQuery(query, Stop.class)
                        .setParameter("name", name)
                        .getResultStream()
                        .findFirst();
    }

    @Override
    public Stop save(Stop stop) {
        entityManager.persist(stop);        
        
        return stop; 
    }

    @Override
    public List<Stop> findByNameStartingWith(String prefix) {
        String query = "SELECT s FROM Stop s WHERE s.name LIKE :prefix";
        return entityManager.createQuery(query, Stop.class)
                        .setParameter("prefix", prefix + "%")
                        .getResultList();
    }
}