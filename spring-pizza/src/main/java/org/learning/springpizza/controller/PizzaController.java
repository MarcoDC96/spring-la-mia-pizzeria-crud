package org.learning.springpizza.controller;

import jakarta.validation.Valid;
import org.learning.springpizza.model.Pizza;
import org.learning.springpizza.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizza")
public class PizzaController {
    @Autowired
    private PizzaRepository pizzaRepository;
    @GetMapping
    public String index(Model model){
        List<Pizza> pizzaList = pizzaRepository.findAll();
        model.addAttribute("pizzalist", pizzaList);
        return "pizzas/list";
    }

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model){
        Optional<Pizza> result = pizzaRepository.findById(id);
        if(result.isPresent()){
            Pizza pizza = result.get();
            model.addAttribute("pizza", pizza);
            return "pizzas/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id: " + id + " is not found");
        }
    }
    @GetMapping("/create")
    public String create(Model model){
        Pizza pizza = new Pizza();
        model.addAttribute("pizza", new Pizza());
        return "pizzas/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult){
    if(bindingResult.hasErrors()){
        return "pizzas/create";
    }
    Pizza savedPizza = pizzaRepository.save(formPizza);
    return "redirect:/pizza/show/" + savedPizza.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        Optional<Pizza> result = pizzaRepository.findById(id);
        if(result.isPresent()){
        model.addAttribute("pizza", result.get());
        return "pizzas/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id: " + id + " is not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult){
        {
            Optional<Pizza> result = pizzaRepository.findById(id);
            if (result.isPresent()){
                Pizza pizzaToEdit = result.get();
                if (bindingResult.hasErrors()){
                    return "/pizzas/edit";
                }
                formPizza.setPhoto(pizzaToEdit.getPhoto());
                Pizza savedPizza = pizzaRepository.save(formPizza);
                return "redirect:/pizza/show/" + id;
            }else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id" + id + "not found");
            }
        }

    }
}
