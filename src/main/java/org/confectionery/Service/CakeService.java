package org.confectionery.Service;

import org.confectionery.Domain.Cake;
import org.confectionery.Repository.Repository;

import java.util.List;

public class CakeService {
    private final Repository<Cake> cakeRepository;

    public CakeService(Repository<Cake> cakeRepository) {
        this.cakeRepository = cakeRepository;
    }

    public void addCake(Cake cake) {
        cakeRepository.create(cake);
    }

    public Cake findCakeById(int id) {
        return cakeRepository.get(id);
    }

    public List<Cake> getAllCakes() {
        return cakeRepository.getAll();
    }

    public void updateCake(Cake cake) {
        cakeRepository.update(cake);
    }

    public void deleteCake(int id) {
        cakeRepository.delete(id);
    }

}
