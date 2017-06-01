package org.alcibiade.pandiscovery.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * Rich console appender.
 */
public class CustomConsoleAppender extends AppenderBase<LoggingEvent> {

    private static final String ESC = "\033[";

    private boolean freshLine = true;
    private boolean isTerminal = false;

    public CustomConsoleAppender() {
        super();

        String terminal = System.getenv("TERM");

        if (terminal != null) {
            isTerminal = true;
        }
    }

    @Override
    protected void append(LoggingEvent event) {
        if (!freshLine) {
            System.out.print(ESC + "1K");
            System.out.print(ESC + "1G");
            freshLine = true;
        }

        if (event.getLevel() == Level.DEBUG && isTerminal) {
            System.out.print(event.getFormattedMessage());
            freshLine = false;
        } else {
            System.out.println(event.getFormattedMessage());
        }
    }
}
