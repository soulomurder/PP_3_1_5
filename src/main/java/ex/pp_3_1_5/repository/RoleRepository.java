package ex.pp_3_1_5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ex.pp_3_1_5.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
