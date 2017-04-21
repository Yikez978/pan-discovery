package org.alcibiade.pandiscovery.scan;

import org.springframework.stereotype.Component;

/**
 * Strip text contents
 */
@Component
public class TextStripper {

    public String strip(String text) {
        return text.replaceAll("[^\\d]+", "");
    }
}
