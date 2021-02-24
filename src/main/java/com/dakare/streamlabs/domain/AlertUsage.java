package com.dakare.streamlabs.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "alert_usage")
@EntityListeners({AuditingEntityListener.class})
public class AlertUsage {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    @CreatedDate
    private Date createdDate;
    @ManyToOne
    private ImageAlert imageAlert;
    @Column(nullable = false)
    private String nickname;
}
