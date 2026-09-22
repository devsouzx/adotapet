package com.devsouzx.adotapet.dto.request;

public record UserPasswordUpdateRequest(String senhaAtual, String novaSenha, String confirmarNovaSenha) {
}
