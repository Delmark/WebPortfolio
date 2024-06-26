package com.delmark.portfoilo.models.portfolio;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity(name = "projects")
@Table(name = "projects")
@Accessors(chain = true)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Projects {
    @Id
    @SequenceGenerator(name = "proj_id_seq", sequenceName = "proj_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proj_id_seq")
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Column(name = "project_name")
    private String projectName;


    @Column(name = "project_description")
    private String projectDesc;


    @Column(name = "project_link")
    private String projectLink;

}
