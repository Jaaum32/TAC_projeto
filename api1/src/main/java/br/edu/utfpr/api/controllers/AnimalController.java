package br.edu.utfpr.api.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.api.models.Animal;
import br.edu.utfpr.api.repositories.AnimalRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/animal")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @PostMapping
    public Animal create(@RequestBody @Valid Animal animal) {
        animalRepository.save(animal);
        System.out.println(animal);
        return animal;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        var animalOpt = animalRepository.findById(id);

        return animalOpt.isPresent()
                ? ResponseEntity.ok(animalOpt.get())
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<Animal>> getAll(
            @PageableDefault(page = 0, size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(206).body(animalRepository.findAll(pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> put(@PathVariable int id, @Valid @RequestBody Animal dto) {
        var animalOpt = animalRepository.findById(id);

        if (animalOpt.isPresent()) {
            var animal = animalOpt.get();
            BeanUtils.copyProperties(dto, animal, "id", "reproductionHistory", "healthHistory");
            try {
                animalRepository.save(animal);
                return ResponseEntity.ok().body(animal);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao salvar: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        var animalOpt = animalRepository.findById(id);

        if (animalOpt.isPresent()) {
            try {
                animalRepository.delete(animalOpt.get());
                return ResponseEntity.ok().build();
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao deletar: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
