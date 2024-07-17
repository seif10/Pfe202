package org.example.monitoringag.Entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String date;
    private String component;
    private String module;
    private String process;
    private String action;

    @Column(length=10000)
    private String details;
}
