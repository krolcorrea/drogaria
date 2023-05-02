package com.generation.farmacia.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import com.generation.farmacia.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	
	List<Produto> findByPrecoBetween(@PathVariable BigDecimal inicio, BigDecimal fim);
	public List <Produto> findAllByNomeOrFabricante(String nome, String fabricante);
	public List <Produto> findAllByNomeAndFabricante(String nome, String fabricante);
	public List<Produto> findAllByNomeContainingIgnoreCase(@Param("nome")String nome);
}
