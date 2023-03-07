/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.chen.sdeven.sparrow.commons.base.commons.exception;


import com.google.common.base.Throwables;
import com.sun.xml.internal.ws.api.model.CheckedException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.annotation.Nullable;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A tool class for exception handling.
 * <p>Checked and Uncheked({@link ExecutionException}) and Wrap conversions.
 * <p>Helper functions for printing Exception.
 * <p>StackTrace performance optimization related, try to use static exceptions
 * to avoid exception generation to obtain StackTrace, and the consumption of printing StackTrace
 * @author sdeven
 */
public abstract class ExceptionUtil {

    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        while ((cause = throwable.getCause()) != null) {
            throwable = cause;
        }
        return throwable;
    }

    public static List<Throwable> getCausalChain(Throwable throwable) {
        List<Throwable> causes = new ArrayList<Throwable>(4);
        while (throwable != null) {
            causes.add(throwable);
            throwable = throwable.getCause();
        }
        return Collections.unmodifiableList(causes);
    }

    public static String getAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter(50);
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("nested exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }
    private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];

    /**
     * Checked/Uncheked and Wrap(Case:ExecutionException) conversions
     * Convert {@link CheckedException} to {@link RuntimeException} and re-throw it,
     * which reduces the definition of  {@link CheckedException} in the function signature.
     * <p>
     *  {@link CheckedException} will be wrapped with {@link UndeclaredThrowableException},
     *  {@link RuntimeException} and Error will not be transformed.
     * <p>
     * from Commons Lange 3.5 ExceptionUtils.
     * <p>
     * Although unchecked() already throws an exception directly,
     * it still defines the return value to make it easier to cheat Sonar, so this function also changes the return value a bit
     * <p>
     * Case:
     * <p>
     * <pre>
     * try{ ... }catch(Exception e){ throw unchecked(t); }
     * </pre>
     *
     * @see ExceptionUtil(Throwable)
     */
    public static RuntimeException unchecked(Throwable t) {
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        }
        if (t instanceof Error) {
            throw (Error) t;
        }

        throw new UncheckedException(t);
    }

    /**
     * If it is a well-known wrapper class, get the real exception from cause. Other exceptions remain the same.
     * <p>
     * The {@link ExecutionException} used in Future is the same as the {@link InvocationTargetException} defined in Reflection,
     * the real exception is encapsulated in Cause
     * <p>
     * Front unchecked() uses the same  {@link UncheckedException}.
     * <p>
     * from Quasar and Tomcat's {@link ExceptionUtils}
     */
    public static Throwable unwrap(Throwable t) {
        if (t instanceof java.util.concurrent.ExecutionException
                || t instanceof java.lang.reflect.InvocationTargetException || t instanceof UncheckedException) {
            return t.getCause();
        }

        return t;
    }

    /**
     * Combining the effects of unchecked and unwrap
     */
    public static RuntimeException uncheckedAndWrap(Throwable t) {

        Throwable unwrapped = unwrap(t);
        if (unwrapped instanceof RuntimeException) {
            throw (RuntimeException) unwrapped;
        }
        if (unwrapped instanceof Error) {
            throw (Error) unwrapped;
        }

        throw new UncheckedException(unwrapped);
    }

    /**
     * Customize a {@link CheckedException} wrapper.
     * <p>
     * When Message/Cause is returned, the inner Exception message is returned.
     */
    public static class UncheckedException extends RuntimeException {

        private static final long serialVersionUID = 4140223302171577501L;

        public UncheckedException(Throwable cause) {
            super(cause);
        }

        @Override
        public String getMessage() {
            return super.getCause().getMessage();
        }
    }

    // Output content

    /**
     * Convert StackTrace[] to String for use in places other than Logger or e.printStackTrace().
     * @see Throwables#getStackTraceAsString(Throwable)
     */
    public static String stackTraceText(Throwable t) {
        return Throwables.getStackTraceAsString(t);
    }

    /**
     * Determine if the exception is caused by some underlying exception.
     */
    @SuppressWarnings("unchecked")
    public static boolean isCausedBy(Throwable t, Class<? extends Exception>... causeExceptionClasses) {
        Throwable cause = t;

        while (cause != null) {
            for (Class<? extends Exception> causeClass : causeExceptionClasses) {
                if (causeClass.isInstance(cause)) {
                    return true;
                }
            }
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * Assembling short exception class names (exception information)
     * Short class name compared to Throwable.toString()
     * @see ExceptionUtil(Throwable)
     */
    public static String toStringWithShortName(@Nullable Throwable t) {
        return ExceptionUtils.getMessage(t);
    }

    /**
     * Putting together short exception class names (Short exception class name for RootCause: Exception Info -> Exception Info)
     */
    public static String toStringWithRootCause(@Nullable Throwable t) {
        if (t == null) {
            return StringUtils.EMPTY;
        }

        final String clsName = ClassUtils.getShortClassName(t, null);
        final String message = StringUtils.defaultString(t.getMessage());
        Throwable cause = getRootCause(t);

        StringBuilder sb = new StringBuilder(128).append(clsName).append(": ").append(message);
        if (cause != t) {
            sb.append("; <---").append(toStringWithShortName(cause));
        }

        return sb.toString();
    }

    // StackTrace Performance optimization processing methods //

    /**
     * from Netty,Set StackTrace for static exception.
     * <p>
     * For some known and frequently thrown exceptions,
     * there is no need to create an exception class each time and generate a full StackTrace.
     * Static declared exceptions can be used in this case.
     * <p>
     * If the exception may be thrown in more than one place,
     * use this function to set the name of the class and method to be thrown.
     * <p>
     * Note: private static RuntimeException TIMEOUT_EXCEPTION = ExceptionUtil.setStackTrace(new RuntimeException("Timeout"),MyClass.class, "mymethod");
     *
     */
    public static <T extends Throwable> T setStackTrace(T exception, Class<?> throwClass, String throwClazz) {
        exception.setStackTrace(
                new StackTraceElement[]{new StackTraceElement(throwClass.getName(), throwClazz, null, -1)});
        return exception;
    }

    /**
     * Clear the StackTrace. Assuming the StackTrace has been generated, printing it out can be quite costly.
     * <p>
     * If you cannot control the generation of StackTrace and its print side (e.g. logger),
     * you can use this method to brute force the Trace.
     * <p>
     * But the Cause chain still cannot be cleared, only the {@link StackTrace} of each Cause can be cleared.
     */
    public static <T extends Throwable> T clearStackTrace(T exception) {
        Throwable cause = exception;
        while (cause != null) {
            cause.setStackTrace(EMPTY_STACK_TRACE);
            cause = cause.getCause();
        }
        return exception;
    }

    /**
     * For cases where exception information needs to be changed,
     * clone() can be used to clone from a previously defined static exception
     * without going through the constructor (thus avoiding the need to get a StackTrace)
     * and set the new exception information.
     * <p>Note: private static CloneableException TIMEOUT_EXCEPTION = new CloneableException("Timeout") .setStackTrace(My.class,"hello");
     * throw TIMEOUT_EXCEPTION.clone("Timeout for 40ms");
     */
    public static class CloneableException extends Exception implements Cloneable {

        private static final long serialVersionUID = -6270471689928560417L;
        protected String message;

        public CloneableException() {
            super((Throwable) null);
        }

        public CloneableException(String message) {
            super((Throwable) null);
            this.message = message;
        }

        public CloneableException(String message, Throwable cause) {
            super(cause);
            this.message = message;
        }

        @Override
        public CloneableException clone() {
            try {
                return (CloneableException) super.clone();
            } catch (CloneNotSupportedException e) {// NOSONAR
                return null;
            }
        }

        @Override
        public String getMessage() {
            return message;
        }

        /**
         * Convenience function, used when defining static exceptions
         */
        public CloneableException setStackTrace(Class<?> throwClazz, String throwMethod) {
            ExceptionUtil.setStackTrace(this, throwClazz, throwMethod);
            return this;
        }

        /**
         * Handy function to clone and reset Message
         */
        public CloneableException clone(String message) {
            CloneableException newException = this.clone();
            newException.setMessage(message);
            return newException;
        }

        /**
         * Convenience functions, reset A Message
         */
        public CloneableException setMessage(String message) {
            this.message = message;
            return this;
        }
    }

    /**
     * For cases where exception information needs to be changed,
     * clone() can be used to clone from a previously
     * defined static exception without going through the constructor
     * (thus avoiding the need to get a StackTrace) and set the new exception information.
     *
     * @see CloneableException
     */
    public static class CloneableRuntimeException extends RuntimeException implements Cloneable {

        private static final long serialVersionUID = 3984796576627959400L;

        protected String message;

        public CloneableRuntimeException() {
            super((Throwable) null);
        }

        public CloneableRuntimeException(String message) {
            super((Throwable) null);
            this.message = message;
        }

        public CloneableRuntimeException(String message, Throwable cause) {
            super(cause);
            this.message = message;
        }

        @Override
        public CloneableRuntimeException clone() {
            try {
                return (CloneableRuntimeException) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        @Override
        public String getMessage() {
            return message;
        }

        /**
         * Convenience function, used when defining static exceptions
         */
        public CloneableRuntimeException setStackTrace(Class<?> throwClazz, String throwMethod) {
            ExceptionUtil.setStackTrace(this, throwClazz, throwMethod);
            return this;
        }

        /**
         * Convenience function, clone and reset Message
         */
        public CloneableRuntimeException clone(String message) {
            CloneableRuntimeException newException = this.clone();
            newException.setMessage(message);
            return newException;
        }

        /**
         * Convenience function, Reset Message
         */
        public CloneableRuntimeException setMessage(String message) {
            this.message = message;
            return this;
        }
    }

}
