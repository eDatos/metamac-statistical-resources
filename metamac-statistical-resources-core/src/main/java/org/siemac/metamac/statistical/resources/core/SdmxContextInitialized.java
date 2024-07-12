package org.siemac.metamac.statistical.resources.core;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author Arte
 *         This listener run a check action about database state to find in_progress job. If there are jobs with this state then marks it with failed status.
 *         This listener runs when spring context is initialized (ContextRefreshedEvent)
 */
public class SdmxContextInitialized implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Not necessary to do tasks here yet
    }
}
