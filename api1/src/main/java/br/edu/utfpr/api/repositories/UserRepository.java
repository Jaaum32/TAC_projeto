package br.edu.utfpr.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.utfpr.api.models.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    
}
