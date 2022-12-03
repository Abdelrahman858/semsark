package project.semsark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.semsark.model.entity.Orders;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long> {
}
