package com.segurosbolivar.sanyuschedulingapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Configuration class to enable scheduling for the application.
 *
 * This class enables Spring's scheduling capabilities in the application, allowing the scheduling of tasks and jobs at specified intervals.
 */
@EnableScheduling
@Configuration
public class ScheduleConfiguration {
}