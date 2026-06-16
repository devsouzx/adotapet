package com.devsouzx.adotapet;

import com.devsouzx.adotapet.dao.AbrigoDAO;
import com.devsouzx.adotapet.dao.AdotanteDAO;
import com.devsouzx.adotapet.dao.PetDAO;
import com.devsouzx.adotapet.model.Abrigo;
import com.devsouzx.adotapet.model.Adotante;
import com.devsouzx.adotapet.model.Pet;

import javax.swing.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        util.ConexaoBD.testarConexao();

        PetDAO petDAO = new PetDAO();
        AbrigoDAO abrigoDAO = new AbrigoDAO();
        AdotanteDAO adotanteDAO = new AdotanteDAO();

        while (true) {

            String opcao = JOptionPane.showInputDialog(
                    """
                    ===== ADOTAPET =====
                    
                    1 - Listar Pets
                    2 - Listar Pets Disponíveis
                    3 - Buscar Pet por ID
                    4 - Listar Abrigos
                    5 - Listar Adotantes
                    0 - Sair
                    
                    Escolha uma opção:
                    """
            );

            if (opcao == null || opcao.equals("0")) {
                break;
            }

            try {

                switch (opcao) {

                    case "1" -> {
                        List<Pet> pets = petDAO.listarTodos();

                        StringBuilder sb = new StringBuilder();

                        for (Pet pet : pets) {
                            sb.append("ID: ").append(pet.getId())
                                    .append("\nNome: ").append(pet.getNome())
                                    .append("\nEspécie: ").append(pet.getEspecie())
                                    .append("\nStatus: ").append(pet.getStatus())
                                    .append("\n----------------------\n");
                        }

                        JOptionPane.showMessageDialog(null,
                                sb.length() == 0 ? "Nenhum pet encontrado." : sb.toString());
                    }

                    case "2" -> {
                        List<Pet> pets = petDAO.listarDisponiveis();

                        StringBuilder sb = new StringBuilder();

                        for (Pet pet : pets) {
                            sb.append("ID: ").append(pet.getId())
                                    .append("\nNome: ").append(pet.getNome())
                                    .append("\nRaça: ").append(pet.getRaca())
                                    .append("\n----------------------\n");
                        }

                        JOptionPane.showMessageDialog(null,
                                sb.length() == 0 ? "Nenhum pet disponível." : sb.toString());
                    }

                    case "3" -> {
                        String idStr = JOptionPane.showInputDialog("Digite o ID do pet:");

                        int id = Integer.parseInt(idStr);

                        Pet pet = petDAO.buscarPorId(id);

                        if (pet != null) {
                            JOptionPane.showMessageDialog(null,
                                    "ID: " + pet.getId() +
                                            "\nNome: " + pet.getNome() +
                                            "\nEspécie: " + pet.getEspecie() +
                                            "\nRaça: " + pet.getRaca() +
                                            "\nStatus: " + pet.getStatus());
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Pet não encontrado.");
                        }
                    }

                    case "4" -> {
                        List<Abrigo> abrigos = abrigoDAO.listarTodos();

                        StringBuilder sb = new StringBuilder();

                        for (Abrigo abrigo : abrigos) {
                            sb.append("ID: ").append(abrigo.getId())
                                    .append("\nNome: ").append(abrigo.getNome())
                                    .append("\nEmail: ").append(abrigo.getEmail())
                                    .append("\n----------------------\n");
                        }

                        JOptionPane.showMessageDialog(null,
                                sb.length() == 0 ? "Nenhum abrigo encontrado." : sb.toString());
                    }

                    case "5" -> {
                        List<Adotante> adotantes = adotanteDAO.listarTodos();

                        StringBuilder sb = new StringBuilder();

                        for (Adotante adotante : adotantes) {
                            sb.append("ID: ").append(adotante.getId())
                                    .append("\nNome: ").append(adotante.getNome())
                                    .append("\nEmail: ").append(adotante.getEmail())
                                    .append("\n----------------------\n");
                        }

                        JOptionPane.showMessageDialog(null,
                                sb.length() == 0 ? "Nenhum adotante encontrado." : sb.toString());
                    }

                    default -> JOptionPane.showMessageDialog(null,
                            "Opção inválida!");
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erro: " + e.getMessage());
            }
        }

        util.ConexaoBD.fecharConexao();
    }
}
