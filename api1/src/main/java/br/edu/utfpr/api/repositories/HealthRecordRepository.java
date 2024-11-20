package br.edu.utfpr.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.api.models.HealthRecord;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Integer>{

}
