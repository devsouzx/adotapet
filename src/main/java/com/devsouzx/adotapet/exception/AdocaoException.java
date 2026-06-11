package com.devsouzx.adotapet.exception;

public class AdocaoException extends Exception {

    public AdocaoException(String mensagem) {
        super(mensagem);
    }

    public AdocaoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
