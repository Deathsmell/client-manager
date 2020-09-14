package by.deathsmell.clientmanager.repository;

import by.deathsmell.clientmanager.domain.Cashbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashboxRepository extends JpaRepository<Cashbox, Integer> {
}
