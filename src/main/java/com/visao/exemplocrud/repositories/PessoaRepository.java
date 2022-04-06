package com.visao.exemplocrud.repositories;


import com.visao.exemplocrud.models.Pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

	List<Pessoa> findBySexo(String sexo);

}
