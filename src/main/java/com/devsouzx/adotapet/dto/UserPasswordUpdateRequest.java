package com.devsouzx.adotapet.dto;

public record UserPasswordUpdateRequest(String senhaAtual, String novaSenha, String confirmarNovaSenha) {
}
