package at.ac.htlhl.nucleij.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author WIH
 * @version 2016-11-02
 */
public class ConsoleOutFormatter extends Formatter {
    /**
     * Format the given LogRecord:
     * "TYPE: MESSAGE (PACKAGE CLASS METHOD)"
     *
     * @param record the log record to be formatted.
     * @return a formatted log record
     */

    @Override
    public synchronized String format(LogRecord record) {
        StringBuffer stringBuffer = new StringBuffer();

        // Type: (only one character: S W I ... )
        stringBuffer.append(record.getLevel().getName().charAt(0) + ": ");

        // Message
        stringBuffer.append(formatMessage(record));

        stringBuffer.append(" (");

        // Package and class
        if (record.getSourceClassName() != null) {
            stringBuffer.append(record.getSourceClassName());
        } else {
            stringBuffer.append(record.getLoggerName());
        }

        // Method
        if (record.getSourceMethodName() != null) {
            stringBuffer.append(" ");
            stringBuffer.append(record.getSourceMethodName());
        }
        stringBuffer.append(")\n");

        // Format exception
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            stringBuffer.append(sw.toString());
        }

        return stringBuffer.toString();
    }
}
