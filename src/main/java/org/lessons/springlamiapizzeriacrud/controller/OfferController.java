package org.lessons.springlamiapizzeriacrud.controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.model.Offer;
import org.lessons.springlamiapizzeriacrud.model.Pizza;
import org.lessons.springlamiapizzeriacrud.repository.OfferRepository;
import org.lessons.springlamiapizzeriacrud.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Controller
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    PizzaRepository pizzaRepository;
    @Autowired
    OfferRepository offerRepository;

    @GetMapping("/create")
    public String create(
        Model model,
        @RequestParam("pizzaId") Integer pizzaId
    ) {
        Offer offer = new Offer(); //nuova istanza di Offer
        Optional<Pizza> pizza = pizzaRepository.findById(pizzaId); //cerco la pizza di riferimento con l'id che ho ricevuto tramite parametro
        if(pizza.isEmpty()) { //se l'id non è valido lancio errore 404
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + " not found");
        }
        offer.setPizza(pizza.get()); //la metto dentro alla nuova istanza di Offer
        model.addAttribute("offer", offer);
        return "offer/editor";
    }

    @PostMapping("/create")
    public String store(
        @Valid @ModelAttribute("offer") Offer formOffer,
        BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "offer/editor";
        }
        offerRepository.save(formOffer);
        return "redirect:/pizzas/" + formOffer.getPizza().getId();
    }

}
