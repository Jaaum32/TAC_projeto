package br.edu.utfpr.api.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.api.dto.ReproductionDTO;
import br.edu.utfpr.api.model.Reproduction;
import br.edu.utfpr.api.repository.ReproductionRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reproduction")
public class ReproductionController {

    @Autowired
    private ReproductionRepository reproductionRepository;

    @PostMapping
    public Reproduction create(@RequestBody @Valid ReproductionDTO dto) {
        var reproduction = new Reproduction();
        BeanUtils.copyProperties(dto, reproduction);
        return reproductionRepository.save(reproduction);
    }

    

    @GetMapping("{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        var reproductionOpt = reproductionRepository.findById(id);

        return reproductionOpt.isPresent()
                ? ResponseEntity.ok(reproductionOpt.get())
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<Reproduction>> getAll(
            @PageableDefault(page = 0, size = 5, sort = "inseminationDate", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(206).body(reproductionRepository.findAll(pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> put(@PathVariable int id, @Valid @RequestBody ReproductionDTO dto) {
        var reproductionOpt = reproductionRepository.findById(id);

        if (reproductionOpt.isPresent()) {
            var reproduction = reproductionOpt.get();
            BeanUtils.copyProperties(dto, reproduction, "id", "animal");

            var reproductionToUpdate = new Reproduction();
            BeanUtils.copyProperties(dto, reproductionToUpdate);
            reproductionToUpdate.setId(reproductionOpt.get().getId());
            try {
                return ResponseEntity.ok().body(reproductionRepository.save(reproductionToUpdate));
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao salvar: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        var reproductionOpt = reproductionRepository.findById(id);

        if (reproductionOpt.isPresent()) {
            try {
                reproductionRepository.delete(reproductionOpt.get());
                return ResponseEntity.ok().build();
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao deletar: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
