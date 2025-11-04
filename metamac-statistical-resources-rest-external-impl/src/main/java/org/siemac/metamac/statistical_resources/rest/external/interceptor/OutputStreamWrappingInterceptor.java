package org.siemac.metamac.statistical_resources.rest.external.interceptor;

import java.io.OutputStream;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 * CXF Interceptor that wraps the OutputStream to suppress ClientAbortException logging.
 * <h2>Problem Statement</h2>
 * When a client cancels a file download (CSV, TSV, XLSX, etc.) before completion,
 * the server attempts to continue writing to a closed connection. This generates a
 * {@code ClientAbortException} which causes massive stack traces in the logs, even
 * though it's a normal and expected situation.
 * <h2>Solution Approach</h2>
 * This interceptor runs in the {@link Phase#PRE_PROTOCOL} phase, which executes
 * BEFORE CXF writes any data to the client. It wraps the original OutputStream
 * with a custom wrapper ({@link ClientAbortSuppressingOutputStream}) that silently
 * catches {@code ClientAbortException} and prevents it from propagating.
 * <h2>How It Works</h2>
 * <ol>
 * <li>CXF creates an OutputStream connected to the HTTP client connection</li>
 * <li>This interceptor executes in PRE_PROTOCOL phase (very early in the pipeline)</li>
 * <li>It wraps the original OutputStream with {@link ClientAbortSuppressingOutputStream}</li>
 * <li>The wrapper delegates all operations to the original stream</li>
 * <li>If the client aborts, {@code write/flush/close} methods catch the exception</li>
 * <li>The wrapper marks itself as aborted and stops attempting further writes</li>
 * <li>No exception propagates to CXF, preventing error logs</li>
 * </ol>
 * <h2>Why PRE_PROTOCOL Phase?</h2>
 * <ul>
 * <li>Early enough: The OutputStream is already created by CXF</li>
 * <li>Not too early: We need the stream to exist before wrapping it</li>
 * <li>Before writing: Executes BEFORE any actual data is written</li>
 * <li>Before logging: Runs BEFORE CXF's error logging mechanisms</li>
 * </ul>
 * <h2>Configuration</h2>
 * Register this interceptor in your CXF configuration (Spring XML):
 * 
 * <pre>{@code
 * <bean id="outputStreamWrappingInterceptor" 
 *       class="org.siemac...OutputStreamWrappingInterceptor"/>
 * 
 * <cxf:bus>
 *     <cxf:outInterceptors>
 *         <ref bean="outputStreamWrappingInterceptor"/>
 *     </cxf:outInterceptors>
 * </cxf:bus>
 * }</pre>
 * 
 * <h2>Benefits</h2>
 * <ul>
 * <li>Catches exceptions at the source (where they're generated)</li>
 * <li>No changes needed to business logic</li>
 * <li>Transparent to the rest of the application</li>
 * <li>Handles all stream operations (write, flush, close)</li>
 * <li>Efficient: stops attempting writes after abort detected</li>
 * </ul>
 * 
 * @version 1.0
 * @see ClientAbortSuppressingOutputStream
 */
public class OutputStreamWrappingInterceptor extends AbstractPhaseInterceptor<Message> {

    public OutputStreamWrappingInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    /**
     * Intercepts the outbound message and wraps the OutputStream.
     * This method executes in the PRE_PROTOCOL phase, before CXF writes any data.
     * It retrieves the original OutputStream from the message, wraps it with
     * {@link ClientAbortSuppressingOutputStream}, and replaces it back in the message.
     * 
     * @param message the CXF message containing the OutputStream
     * @throws Fault if an error occurs during interception (not thrown in practice)
     */
    @Override
    public void handleMessage(Message message) throws Fault {
        // Get the OutputStream of the message
        OutputStream originalStream = message.getContent(OutputStream.class);

        if (originalStream != null && !(originalStream instanceof ClientAbortSuppressingOutputStream)) {

            // Wrap the stream
            ClientAbortSuppressingOutputStream wrappedStream = new ClientAbortSuppressingOutputStream(originalStream);

            // Replace the stream in the message
            message.setContent(OutputStream.class, wrappedStream);
        }
    }

    /**
     * OutputStream wrapper that suppresses ClientAbortException.
     * <p>
     * This class implements the Decorator pattern, wrapping the original
     * OutputStream and delegating all operations to it. When a {@code ClientAbortException}
     * occurs (indicating the client closed the connection), it's caught and suppressed
     * rather than being allowed to propagate.
     * </p>
     * <h3>Exception Detection</h3>
     * The wrapper detects ClientAbortException by:
     * <ul>
     * <li>Checking exception class name (handles classloader issues)</li>
     * <li>Checking exception message content</li>
     * <li>Traversing the cause chain for nested exceptions</li>
     * <li>Recognizing related errors (Broken pipe, Connection reset)</li>
     * </ul>
     * <h3>Behavior After Abort</h3>
     * Once an abort is detected:
     * <ul>
     * <li>The {@code aborted} flag is set to true</li>
     * <li>A DEBUG log message is recorded</li>
     * <li>All subsequent operations return immediately without attempting I/O</li>
     * <li>No exceptions are thrown to calling code</li>
     * </ul>
     * This prevents CXF from continuing to write to a closed connection and
     * generating cascading errors and log spam.
     */
    private static class ClientAbortSuppressingOutputStream extends OutputStream {

        private final OutputStream delegate;
        private boolean            aborted = false;

        public ClientAbortSuppressingOutputStream(OutputStream delegate) {
            this.delegate = delegate;
        }

        /**
         * Writes a single byte to the underlying stream.
         * If the client has aborted, this method returns immediately without
         * attempting to write. If a {@code ClientAbortException} occurs during
         * the write, it's caught, logged, and suppressed.
         * 
         * @param b the byte to write
         * @throws java.io.IOException if an I/O error occurs (other than ClientAbortException)
         */
        @Override
        public void write(int b) throws java.io.IOException {
            if (aborted)
                return;
            try {
                delegate.write(b);
            } catch (java.io.IOException e) {
                if (isClientAbortException(e)) {
                    aborted = true;
                } else {
                    throw e;
                }
            }
        }

        /**
         * Writes an array of bytes to the underlying stream.
         * 
         * @param b the byte array to write
         * @throws java.io.IOException if an I/O error occurs (other than ClientAbortException)
         */
        @Override
        public void write(byte[] b) throws java.io.IOException {
            if (aborted)
                return;
            try {
                delegate.write(b);
            } catch (java.io.IOException e) {
                if (isClientAbortException(e)) {
                    aborted = true;
                } else {
                    throw e;
                }
            }
        }

        /**
         * Writes a portion of a byte array to the underlying stream.
         * 
         * @param b the byte array
         * @param off the start offset in the data
         * @param len the number of bytes to write
         * @throws java.io.IOException if an I/O error occurs (other than ClientAbortException)
         */
        @Override
        public void write(byte[] b, int off, int len) throws java.io.IOException {
            if (aborted)
                return;
            try {
                delegate.write(b, off, len);
            } catch (java.io.IOException e) {
                if (isClientAbortException(e)) {
                    aborted = true;
                } else {
                    throw e;
                }
            }
        }

        /**
         * Flushes the underlying stream.
         * 
         * @throws java.io.IOException if an I/O error occurs (other than ClientAbortException)
         */
        @Override
        public void flush() throws java.io.IOException {
            if (aborted)
                return;
            try {
                delegate.flush();
            } catch (java.io.IOException e) {
                if (isClientAbortException(e)) {
                    aborted = true;
                } else {
                    throw e;
                }
            }
        }

        /**
         * Closes the underlying stream.
         * 
         * @throws java.io.IOException if an I/O error occurs (other than ClientAbortException)
         */
        @Override
        public void close() throws java.io.IOException {
            if (aborted)
                return;
            try {
                delegate.close();
            } catch (java.io.IOException e) {
                if (isClientAbortException(e)) {
                    aborted = true;
                } else {
                    throw e;
                }
            }
        }

        /**
         * Determines if an exception is a ClientAbortException or related error.
         * This method checks:
         * <ul>
         * <li>Exception class name (handles classloader issues)</li>
         * <li>Exception message content (handles internationalization)</li>
         * <li>The entire cause chain (handles wrapped exceptions)</li>
         * <li>Related network errors (Broken pipe, Connection reset)</li>
         * </ul>
         * The traversal is limited to prevent infinite loops in circular cause chains.
         * 
         * @param throwable the exception to check
         * @return true if this is a client abort exception, false otherwise
         */
        private boolean isClientAbortException(Throwable throwable) {
            int maxDepth = 20;
            int depth = 0;

            while (throwable != null && depth < maxDepth) {
                // Check by class name (handles classloader issues)
                String className = throwable.getClass().getName();
                if (className.contains("ClientAbortException") || className.contains("ClientAbort")) {
                    return true;
                }
                // Check by message content (handles various languages and similar errors)
                String message = throwable.getMessage();
                if (message != null) {
                    // English messages
                    if (message.contains("ClientAbortException") || message.contains("Broken pipe") || message.contains("Connection reset")) {
                        return true;
                    }
                    // Spanish messages
                    if (message.contains("Se ha anulado una conexión") || message.contains("conexión establecida")) {
                        return true;
                    }
                }

                throwable = throwable.getCause();
                depth++;
            }
            return false;
        }
    }
}
