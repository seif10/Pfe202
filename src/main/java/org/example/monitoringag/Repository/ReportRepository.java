package org.example.monitoringag.Repository;


import org.example.monitoringag.Entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
