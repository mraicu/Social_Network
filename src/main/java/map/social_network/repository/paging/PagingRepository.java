package map.social_network.repository.paging;

import map.social_network.domain.Entity;
import map.social_network.domain.entities.User;
import map.social_network.repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
    Page<E> findAll(Pageable pageable, User user);

}
