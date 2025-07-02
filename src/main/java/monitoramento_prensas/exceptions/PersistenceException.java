package monitoramento_prensas.exceptions;

/**
 * Exception para erro de persistencia de dados.
 *
 * @author Carlos Vieira
 * @since 01/07/2025
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PersistenceException(String msg) {
        super(msg);
    }
}
