package com.dakare.streamlabs.service.command.listener;

import com.dakare.streamlabs.config.properties.ImageAlertConfigurationProperties;
import com.dakare.streamlabs.config.properties.StreamlabsAlertConfigurationProperties;
import com.dakare.streamlabs.domain.AlertUsage;
import com.dakare.streamlabs.domain.ImageAlert;
import com.dakare.streamlabs.repository.AlertUsageRepository;
import com.dakare.streamlabs.repository.ImageAlertRepository;
import com.dakare.streamlabs.service.StreamlabsAlertService;
import com.dakare.streamlabs.service.StreamlabsPointsService;
import com.dakare.streamlabs.service.command.ChatCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class ImageAlertListener {

  private final StreamlabsPointsService streamlabsPointsService;
  private final ImageAlertConfigurationProperties imageAlertConfigurationProperties;
  private final ImageAlertRepository imageAlertRepository;
  private final StreamlabsAlertService streamlabsAlertService;
  private final AlertUsageRepository alertUsageRepository;
  private final Object lock = new Object();
  private long nextAlert = 0;

  @EventListener
  public void onCommand(ChatCommand chatCommand) {
    if (!imageAlertConfigurationProperties.isEnabled()) {
      return;
    }
    synchronized (lock) {
      if (nextAlert > System.currentTimeMillis()) {
        log.info("Skip image alert request {} - too soon", chatCommand);
        return;
      }
      final Optional<ImageAlert> imageAlert = imageAlertRepository.findByCommand(chatCommand.getCommand());
      if (!imageAlert.isPresent()) {
        return;
      }
      if (!imageAlert.get().isEnabled()) {
        log.info("Skip image alert for {} as alert is disabled", chatCommand);
        return;
      }
      if (!haveEnoughPoints(chatCommand, imageAlert.get())) {
        log.info("Skip image alert for {} as user don't have enough loyalty points", chatCommand);
        return;
      }
      log.info("Showing image alert {}", chatCommand);
      if (streamlabsAlertService.showImageAlert(imageAlert.get())) {
        alertUsageRepository.save(new AlertUsage().setNickname(chatCommand.getFrom())
                .setImageAlert(imageAlert.get()));
      } else {
        log.warn("Failed to show image alert {}", chatCommand);
      }
      nextAlert = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(imageAlert.get().getDelaySec());
    }
  }

  private boolean haveEnoughPoints(ChatCommand command, ImageAlert imageAlert) {
    if (!imageAlertConfigurationProperties.isLoyaltyPoints()) {
      return true;
    }
    if (imageAlert.isSubsOnly() && !command.isSubscriber()) {
      log.info("Ignoring image alert request from {} as alert {} allowed only for subscribers",
              command.getFrom(), imageAlert.getCommand());
      return false;
    }
    final Optional<StreamlabsPointsService.StreamlabsUserPoints> userPoints = streamlabsPointsService.getUserPoints(command.getFrom());
    if (!userPoints.isPresent()) {
      return false;
    }
    int requiredPoints = command.isSubscriber() ? imageAlert.getPriceSubs() : imageAlert.getPrice();
    if (userPoints.get().getPoints() < requiredPoints) {
      log.info("Ignoring image alert request from {} for alert {} as user don't have enough points ({})",
              command.getFrom(), imageAlert.getCommand(), userPoints.get().getPoints());
      return false;
    }
    return streamlabsPointsService.subtractUserPoints(command.getFrom(), requiredPoints);
  }
}
