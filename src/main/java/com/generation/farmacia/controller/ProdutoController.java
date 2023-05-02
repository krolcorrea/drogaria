package com.generation.farmacia.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll());

		// SELECT * from tb_postagens;
		//teste
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {

		return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByTitulo(@PathVariable String nome) {

		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));

		// SELECT * FROM tb_postagens WHERE nome LIKE "%nome%";
	}

	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
		return ResponseEntity.badRequest().build();
	}

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		if (produtoRepository.existsById(produto.getId())) {
			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.ok(produtoRepository.save(produto));
			else
				return ResponseEntity.badRequest().build();

		}

		return ResponseEntity.notFound().build();

	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);

		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		produtoRepository.deleteById(id);
	}

	@GetMapping("/preco_inicial/{preco_inicial}/preco_final/{preco_final}")
	public ResponseEntity<List<Produto>> getByPreco(@PathVariable BigDecimal preco_inicial,
			@PathVariable BigDecimal preco_final) {

		return ResponseEntity.ok(produtoRepository.findByPrecoBetween(preco_inicial, preco_final));
	}

	@GetMapping("/nome/{nome}/efabricante/{fabricante}")
	public ResponseEntity<List<Produto>> getByNomeEFabricante(@PathVariable String nome,
			@PathVariable String fabricante) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeAndFabricante(nome, fabricante));
	}

	@GetMapping("/nome/{nome}/oufabricante/{fabricante}")
	public ResponseEntity<List<Produto>> getByNomeOuFabricante(@PathVariable String nome,
			@PathVariable String fabricante) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeOrFabricante(nome, fabricante));
	}
}
