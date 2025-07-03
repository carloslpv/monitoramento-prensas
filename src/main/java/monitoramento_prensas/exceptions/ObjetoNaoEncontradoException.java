package monitoramento_prensas.exceptions;

/**
 * Exception para objetos que não foram encontrados.
 *
 * @author Carlos Vieira
 * @since 02/07/2025
 */
public class ObjetoNaoEncontradoException extends RuntimeException {

    public ObjetoNaoEncontradoException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ObjetoNaoEncontradoException(String msg) {
        super(msg);
    }
}
