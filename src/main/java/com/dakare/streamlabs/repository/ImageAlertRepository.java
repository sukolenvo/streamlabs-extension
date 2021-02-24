package com.dakare.streamlabs.repository;

import com.dakare.streamlabs.domain.ImageAlert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageAlertRepository extends JpaRepository<ImageAlert, Long> {

    int countByEnabledTrue();

    Optional<ImageAlert> findByCommand(String commandKey);
}
