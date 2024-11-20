package br.edu.utfpr.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.api.models.Animal;

public interface AnimalRepository extends JpaRepository<Animal, Integer>{

}
