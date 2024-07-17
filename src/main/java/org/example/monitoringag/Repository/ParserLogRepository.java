package org.example.monitoringag.Repository;

import org.example.monitoringag.Entity.ParsedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParserLogRepository extends JpaRepository<ParsedLog,Long> {
    List<ParsedLog> findByDate(String date);
    List<ParsedLog> findByComponentContaining(String threadOrUser);

}
