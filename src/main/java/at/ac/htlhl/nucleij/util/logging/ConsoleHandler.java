package at.ac.htlhl.nucleij.util.logging;


import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * The aim of this class is to permit to specify the output for the console
 * handler of the java.util.logging system.
 *
 * This console handler is also able to choose the right ouput (System.err
 * or System.out) depending on the message level.
 *
 * @author  S.Chassande-Barrioz:
 *          https://javalibs.com/artifact/org.ow2.monolog/monolog-wrapper-javalog?className=org.objectweb.util.monolog.wrapper.javaLog.ConsoleHandler
 *          License: LGPL
 * @version 13. August 2006
 */
public class ConsoleHandler extends Handler
{
    private Writer writer;

    private static PrintStream orginalOut = System.out;
    private static PrintStream orginalErr = System.err;

    public ConsoleHandler()
    {
        super();

        setOutput(orginalOut);
    }

    /**
     * Assign the Outputstream by calling a protected method from the super
     * class.
     */
    public synchronized void setOutput(OutputStream out)
            throws SecurityException
    {
        if (out == null)
        {
            throw new NullPointerException();
        }
        flush();
        String encoding = getEncoding();
        if (encoding == null)
        {
            writer = new OutputStreamWriter(out);
        }
        else
        {
            try
            {
                writer = new OutputStreamWriter(out, encoding);
            }
            catch (UnsupportedEncodingException ex)
            {
                // This shouldn't happen.  The setEncoding method
                // should have validated that the encoding is OK.
                throw new Error("Unexpected exception " + ex);
            }
        }
    }

    @Override
    public void publish(LogRecord record)
    {
        if (!isLoggable(record))
        {
            return;
        }

        String msg;
        try
        {
            msg = getFormatter().format(record);
        }
        catch (Exception ex)
        {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.FORMAT_FAILURE);
            return;
        }

        if (record.getLevel() == Level.SEVERE)
        {
            orginalOut.flush();
            setOutput(orginalErr);
        }
        else
        {
            setOutput(orginalOut);
        }

        try
        {
            writer.write(msg);
        }
        catch (Exception ex)
        {
            // We don't want to throw an exception here, but we
            // report the exception to any registered ErrorManager.
            reportError(null, ex, ErrorManager.WRITE_FAILURE);
        }
        flush();
        orginalOut.flush();
        orginalErr.flush();
    }

    @Override
    public void flush()
    {
        if (writer != null)
        {
            try
            {
                writer.flush();
            }
            catch (Exception ex)
            {
                // We don't want to throw an exception here, but we
                // report the exception to any registered ErrorManager.
                reportError(null, ex, ErrorManager.FLUSH_FAILURE);
            }
        }
    }

    @Override
    public void close() throws SecurityException
    {
        flush();
    }
}
