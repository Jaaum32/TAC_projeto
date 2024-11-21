package br.edu.utfpr.api.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.utfpr.api.dto.HealthRecordDTO;
import br.edu.utfpr.api.model.HealthRecord;
import br.edu.utfpr.api.repository.HealthRecordRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/healthrecord")
@Tag(name = "Health Record", description = "Authentication resource endpoints.")
public class HealthRecordController {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @PostMapping
    public HealthRecord create(@RequestBody @Valid HealthRecordDTO dto) {
        var healthRecord = new HealthRecord();
        BeanUtils.copyProperties(dto, healthRecord);
        return healthRecordRepository.save(healthRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        var healthRecordOpt = healthRecordRepository.findById(id);

        return healthRecordOpt.isPresent()
                ? ResponseEntity.ok(healthRecordOpt.get())
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<HealthRecord>> getAll(
            @PageableDefault(page = 0, size = 5, sort = "date", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(206).body(healthRecordRepository.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> put(@PathVariable int id, @Valid @RequestBody HealthRecordDTO dto) {
        var healthRecordOpt = healthRecordRepository.findById(id);

        if (healthRecordOpt.isPresent()) {
            var healthRecord = healthRecordOpt.get();
            BeanUtils.copyProperties(dto, healthRecord, "id", "animal");

            var healthRecordToUpdate = new HealthRecord();
            BeanUtils.copyProperties(dto, healthRecordToUpdate);
            healthRecordToUpdate.setId(healthRecordOpt.get().getId());
            try {
                return ResponseEntity.ok().body(healthRecordRepository.save(healthRecordToUpdate));
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao salvar: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        var healthRecordOpt = healthRecordRepository.findById(id);

        if (healthRecordOpt.isPresent()) {
            try {
                healthRecordRepository.delete(healthRecordOpt.get());
                return ResponseEntity.ok().build();
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao deletar: " + ex.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
