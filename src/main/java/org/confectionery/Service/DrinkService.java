package org.confectionery.Service;


import org.confectionery.Domain.Drink;
import org.confectionery.Exception.EntityNotFoundException;
import org.confectionery.Repository.Repository;

import java.util.List;

public class DrinkService {
    private final Repository<Drink> drinkRepository;

    public DrinkService(Repository<Drink> drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    /**
     * Adds a new drink to the repository.
     *
     * @param drink the drink to be added
     */
    public void addDrink(Drink drink) {
        drinkRepository.create(drink);
    }

    /**
     * Finds a drink by its unique ID.
     *
     * @param id the ID of the drink
     * @return the drink if found
     * @throws EntityNotFoundException if the drink with the given ID does not exist
     */
    public Drink findDrinkById(int id) {
        return drinkRepository.get(id);
    }

    /**
     * Retrieves all drinks from the repository.
     *
     * @return a list of all drinks
     */
    public List<Drink> getAllDrinks() {
        return drinkRepository.getAll();
    }

    /**
     * Updates an existing drink in the repository.
     *
     * @param drink the drink with updated information
     */
    public void updateDrink(Drink drink) {
        drinkRepository.update(drink);
    }

    /**
     * Deletes a drink from the repository by its ID.
     *
     * @param id the ID of the drink to be deleted
     */
    public void deleteDrink(int id) {
        drinkRepository.delete(id);
    }

}

