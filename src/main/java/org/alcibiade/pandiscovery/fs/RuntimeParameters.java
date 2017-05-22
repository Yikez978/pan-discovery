package org.alcibiade.pandiscovery.fs;

import org.springframework.stereotype.Component;

/**
 * Parameters related to the current execution.
 */
@Component
public class RuntimeParameters {
    private boolean verbose = false;

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }
}
