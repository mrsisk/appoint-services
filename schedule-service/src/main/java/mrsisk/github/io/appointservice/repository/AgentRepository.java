package mrsisk.github.io.appointservice.repository;

import mrsisk.github.io.appointservice.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
}
