package com.devsouzx.adotapet.exception;

/**
 * Exceção personalizada para erros relacionados ao processo de adoção.
 *
 * <p>Esta exceção é lançada em situações como:</p>
 * <ul>
 *   <li>Tentativa de adotar um pet indisponível</li>
 *   <li>Limite de solicitações pendentes excedido</li>
 *   <li>Pet já possui solicitação pendente</li>
 *   <li>Tentativa de cancelar uma solicitação já respondida</li>
 *   <li>Avaliação com nota inválida</li>
 *   <li>Remoção de pet com solicitação pendente</li>
 * </ul>
 *
 *
 * <p>Esta classe estende {@link Exception} para exigir tratamento
 * obrigatório nos métodos que a utilizam.</p>
 *
 * @author Equipe Adoção de Pets
 * @version 1.0
 * @since 2026
 * @see Exception
 */
public class AdocaoException extends Exception {

    /**
     * Construtor que cria uma exceção com uma mensagem descritiva.
     *
     * @param mensagem Descrição detalhada do erro ocorrido
     */
    public AdocaoException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor que cria uma exceção com mensagem e causa.
     *
     * @param mensagem Descrição detalhada do erro ocorrido
     * @param causa Exceção original que causou este erro
     */
    public AdocaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}