package com.visao.exemplocrud.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import com.visao.exemplocrud.models.Pessoa;
import com.visao.exemplocrud.repositories.PessoaRepository;

@RestController
@RequestMapping(path = "/api/v1/pessoa")
public class PessaoResource {

	private PessoaRepository pessoaRepository;

	private PessaoResource(PessoaRepository pessoaRepository) {
		this.pessoaRepository = pessoaRepository;
	}

	@PostMapping
	public ResponseEntity<Pessoa> save(@RequestBody Pessoa pessoa) {
		try {	
			pessoaRepository.save(pessoa);
			URI location = getUri(pessoa.getId());
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private URI getUri(int id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(id).toUri();
	}

	@GetMapping
	public ResponseEntity<List<Pessoa>> retornaTodasAsPessoas() {
		List<Pessoa> pessoa = pessoaRepository.findAll();
		return pessoa.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pessoa);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<Pessoa>> retornaPessoaPeloID(@PathVariable Integer id) {
		Optional<Pessoa> pessoa;
		try {
			pessoa = pessoaRepository.findById(id);
			return new ResponseEntity<Optional<Pessoa>>(pessoa, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Optional<Pessoa>>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/sexo/{sexo}")
	public ResponseEntity<List<Pessoa>> retornaPessoaPeloSexo(@PathVariable String sexo) {
		List<Pessoa> pessoa = pessoaRepository.findBySexo(sexo.toUpperCase());
		return pessoa.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pessoa);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Optional<Pessoa>> excluirPessoaPeloID(@PathVariable Integer id) {
		try {
			pessoaRepository.deleteById(id);
			return new ResponseEntity<Optional<Pessoa>>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Optional<Pessoa>>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Integer id, @RequestBody Pessoa pessoaAtualizada) {

		Assert.notNull(id, "Não foi possivel atualizar o registro");

		return pessoaRepository.findById(id).map(pessoa -> {
			pessoa.setNome(pessoaAtualizada.getNome());
			pessoa.setIdade(pessoaAtualizada.getIdade());
			pessoa.setSexo(pessoaAtualizada.getSexo());

			Pessoa pessoaUpdate = pessoaRepository.save(pessoa);
			return ResponseEntity.ok().body(pessoaUpdate);
		}).orElse(ResponseEntity.notFound().build());
	}

}