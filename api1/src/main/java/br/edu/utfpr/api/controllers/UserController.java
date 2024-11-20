package br.edu.utfpr.api.controllers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.utfpr.api.dtos.UserDTO;
import br.edu.utfpr.api.models.User;
import br.edu.utfpr.api.repositories.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping
     public User create(@RequestBody User user) {
        userRepository.save(user);
        System.out.println(user);
        return user;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> get(@PathVariable int id) {
        var userOpt = userRepository.findById(id);

        return userOpt.isPresent()
                ? ResponseEntity.ok(userOpt.get())
                : ResponseEntity.notFound().build(); // status 404
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAll(
            @PageableDefault(page = 0, size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(206).body(userRepository.findAll(pageable));
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> put(@PathVariable int id, @Valid @RequestBody UserDTO dto) {
        var userOpt = userRepository.findById(id);

        if(userOpt.isPresent()){
            var person = userOpt.get();
            BeanUtils.copyProperties(dto, person);
            try{
                userRepository.save(person);
                return ResponseEntity.ok().body(person);
            }catch (Exception ex){
                return ResponseEntity.badRequest().body("Falha ao salvar: " + ex.getMessage());
            }
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable int id) {
        var userOpt = userRepository.findById(id);

        if(userOpt.isPresent()){
            try{
                userRepository.delete(userOpt.get());
                return ResponseEntity.ok().build();
            }catch (Exception ex){
                return ResponseEntity.badRequest().body("Falha ao deletar: " + ex.getMessage());
            }
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
