package org.example.monitoringag.Entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idMetrics;

    private float cpuUsagePercentage;

    private int totalMemory;
    private int usedlMemory;
    private int freeMemory;
}
