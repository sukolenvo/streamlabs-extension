package com.dakare.streamlabs.repository;

import com.dakare.streamlabs.domain.AlertUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertUsageRepository extends JpaRepository<AlertUsage, Long> {
}
