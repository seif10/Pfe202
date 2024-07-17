package org.example.monitoringag.Repository;

import org.example.monitoringag.Entity.SystemMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystmMetricsRepository extends JpaRepository<SystemMetrics,Long> {
}
