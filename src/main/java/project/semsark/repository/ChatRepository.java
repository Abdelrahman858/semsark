package project.semsark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.semsark.model.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

}
