package com.dakare.streamlabs.domain;

import com.dakare.streamlabs.config.properties.StreamlabsAlertConfigurationProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "alerts", uniqueConstraints = @UniqueConstraint(columnNames = "command"))
@Accessors(chain = true)
@EntityListeners({AuditingEntityListener.class})
public class ImageAlert {

  @Id
  @GeneratedValue
  private long id;
  @Column(nullable = false)
  @CreatedDate
  private Date createdDate;
  private int delaySec;
  private int price;
  private int priceSubs;
  @Column(nullable = false)
  private String command;
  @Column(length = 5000)
  private String imageUrl;
  @Column(length = 5000)
  private String soundUrl;
  private int durationMs;
  private boolean subsOnly;
  @Column(length = 5000)
  private String message;
  @Column(length = 5000)
  private String userMessage;
  private String textColor;
  private boolean enabled = true;
  @JsonIgnore
  @OneToMany(fetch = FetchType.EAGER, mappedBy = "imageAlert")
  private List<AlertUsage> usages = Collections.emptyList();
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StreamlabsAlertConfigurationProperties.Type type = StreamlabsAlertConfigurationProperties.Type.FOLLOW;

  public int getUsageCount() {
    return usages == null ? 0 : usages.size();
  }
}
