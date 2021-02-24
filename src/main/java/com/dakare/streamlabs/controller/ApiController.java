package com.dakare.streamlabs.controller;

import com.dakare.streamlabs.config.properties.BotConfigurationProperties;
import com.dakare.streamlabs.config.properties.StreamlabsAlertConfigurationProperties;
import com.dakare.streamlabs.domain.ImageAlert;
import com.dakare.streamlabs.repository.ImageAlertRepository;
import com.dakare.streamlabs.service.StreamlabsAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private StreamlabsAlertConfigurationProperties streamlabsAlertConfigurationProperties;

    @Autowired
    private BotConfigurationProperties botConfigurationProperties;

    @Autowired
    private ImageAlertRepository imageAlertRepository;

    @Autowired
    private StreamlabsAlertService streamlabsAlertService;

    @GetMapping("/alert/default")
    public ImageAlert getDefaultConfig() {
        return new ImageAlert()
                .setDelaySec(streamlabsAlertConfigurationProperties.getDefaultDelaySec())
                .setDurationMs(streamlabsAlertConfigurationProperties.getDefaultDurationMs())
                .setMessage(streamlabsAlertConfigurationProperties.getDefaultMessage())
                .setUserMessage(streamlabsAlertConfigurationProperties.getDefaultUserMessage())
                .setPrice(streamlabsAlertConfigurationProperties.getDefaultPrice())
                .setPriceSubs(streamlabsAlertConfigurationProperties.getDefaultSubscriberPrice())
                .setSoundUrl(streamlabsAlertConfigurationProperties.getDefaultSoundUrl())
                .setTextColor(streamlabsAlertConfigurationProperties.getDefaultColor())
                .setSubsOnly(false);
    }

    @PostMapping("/alert/save")
    public void saveAlert(@RequestBody ImageAlert imageAlert) {
        imageAlertRepository.save(imageAlert);
    }

    @GetMapping("/alert")
    public List<ImageAlert> getAlertsList() {
        return imageAlertRepository.findAll(Sort.by("createdDate").descending());
    }

    @GetMapping("/alert/{id}")
    public ImageAlert getAlert(@PathVariable long id) {
        return imageAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image alert doesn't exists with id " + id));
    }

    @GetMapping("/alert/enabled")
    public int getEnabledAlertsCount() {
        return imageAlertRepository.countByEnabledTrue();
    }

    @GetMapping(value = "/connected-channel", produces = "text/plain")
    public String getConnectedChannel() {
        return botConfigurationProperties.getChannel();
    }

    @PostMapping("/alert/show/{id}")
    public void showAlert(@PathVariable long id) {
        ImageAlert imageAlert = imageAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image alert doesn't exists with id " + id));
        if (!streamlabsAlertService.showImageAlert(imageAlert)) {
            throw new RuntimeException("Failed to send alert to Streamlabs");
        }
    }

    @DeleteMapping("/alert/{id}")
    public void deleteAlert(@PathVariable long id) {
        imageAlertRepository.deleteById(id);
    }

    @PostMapping("/alert/enable/{id}")
    public void enableAlert(@PathVariable long id) {
        ImageAlert imageAlert = imageAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image alert doesn't exists with id " + id));
        imageAlert.setEnabled(true);
        imageAlertRepository.save(imageAlert);
    }

    @PostMapping("/alert/disable/{id}")
    public void disabledAlert(@PathVariable long id) {
        ImageAlert imageAlert = imageAlertRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image alert doesn't exists with id " + id));
        imageAlert.setEnabled(false);
        imageAlertRepository.save(imageAlert);
    }
}
