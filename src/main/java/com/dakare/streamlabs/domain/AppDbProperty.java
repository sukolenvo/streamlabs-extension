package com.dakare.streamlabs.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "app_properties")
public class AppDbProperty {

    @Id
    private String key;
    @Lob
    @Column(nullable = false)
    private String value;
}
