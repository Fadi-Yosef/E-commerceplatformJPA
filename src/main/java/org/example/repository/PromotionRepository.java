package org.example.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Optional<Promotion> findByCode(String code);

    @Query("""
            select p
            from Promotion p
            where p.startDate <= :date
              and (p.endDate is null or p.endDate >= :date)
            """)
    List<Promotion> findActiveOn(@Param("date") LocalDate date);

    List<Promotion> findByStartDateAfter(LocalDate date);

    List<Promotion> findByEndDateBefore(LocalDate date);

    List<Promotion> findByEndDateIsNull();

    default List<Promotion> findActiveToday() {
        return findActiveOn(LocalDate.now());
    }
}
