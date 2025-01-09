package core;

import animals.AbstractAnimal;
import animals.Animal;
import management.Supervisor;
import management.zooObserver;

import java.util.*;

class zoo {
    private final Map<Integer, Animal> animals;
    private final Map<Integer, Supervisor> supervisors;
    private final Map<Integer, Set<Animal>> supervisorAnimals;
    private final Map<String, Set<Animal>> supervisorNameAnimals;
    private final TreeSet<Animal> animalsByHeight;
    private final Map<Class<? extends Animal>, Set<Animal>> animalsByType;
    private final Set<Animal> soundMakingAnimals;
    private final List<zooObserver> observers;

    public zoo() {
        this(new ArrayList<>());
    }

    public zoo(List<Animal> initialAnimals) {
        animals = new HashMap<>();
        supervisors = new HashMap<>();
        supervisorAnimals = new HashMap<>();
        supervisorNameAnimals = new HashMap<>();
        animalsByHeight = new TreeSet<>(Comparator.comparingDouble(Animal::getHeight));
        animalsByType = new HashMap<>();
        soundMakingAnimals = new HashSet<>();
        observers = new ArrayList<>();

        for (Animal animal : initialAnimals) {
            addAnimal(animal);
        }
    }

    public void addAnimal(Animal animal) {
        animals.put(animal.getId(), animal);
        animalsByHeight.add(animal);
        animalsByType.computeIfAbsent(animal.getClass(), k -> new HashSet<>()).add(animal);
        if (animal.canMakeSound()) {
            soundMakingAnimals.add(animal);
        }
    }

    public Animal getAnimalById(Integer id) {
        return animals.get(id);
    }

    public void removeAnimal(Integer id) {
        Animal animal = animals.remove(id);
        if (animal != null) {
            animalsByHeight.remove(animal);
            animalsByType.get(animal.getClass()).remove(animal);
            if (animal.canMakeSound()) {
                soundMakingAnimals.remove(animal);
            }
            Supervisor supervisor = animal.getSupervisor();
            if (supervisor != null) {
                supervisorAnimals.get(supervisor.getId()).remove(animal);
                supervisorNameAnimals.get(supervisor.getName()).remove(animal);
            }
        }
    }

    public void assignSupervisor(Integer animalId, Supervisor newSupervisor) {
        Animal animal = animals.get(animalId);
        if (animal != null) {
            Supervisor oldSupervisor = animal.getSupervisor();
            animal.setSupervisor(newSupervisor);

            if (oldSupervisor != null) {
                supervisorAnimals.get(oldSupervisor.getId()).remove(animal);
                supervisorNameAnimals.get(oldSupervisor.getName()).remove(animal);
            }

            supervisors.put(newSupervisor.getId(), newSupervisor);
            supervisorAnimals.computeIfAbsent(newSupervisor.getId(), k -> new HashSet<>()).add(animal);
            supervisorNameAnimals.computeIfAbsent(newSupervisor.getName(), k -> new HashSet<>()).add(animal);

            notifyObservers(animal, oldSupervisor, newSupervisor);
        }
    }

    public Set<Animal> getAnimalsBySupervisorId(Integer supervisorId) {
        return supervisorAnimals.getOrDefault(supervisorId, new HashSet<>());
    }

    public Set<Animal> getAnimalsBySupervisorName(String supervisorName) {
        return supervisorNameAnimals.getOrDefault(supervisorName, new HashSet<>());
    }

    public List<Animal> getAnimalsAboveHeight(double height) {
        return new ArrayList<>(animalsByHeight.tailSet(new AbstractAnimal(0, height) {
            @Override
            public String makeSound() {
                return null;
            }
        }, false));
    }

    public Set<Animal> getAnimalsThatCanMakeSound() {
        return new HashSet<>(soundMakingAnimals);
    }

    public Set<Animal> getAnimalsByType(Class<? extends Animal> type) {
        return new HashSet<>(animalsByType.getOrDefault(type, new HashSet<>()));
    }

    public void addObserver(zooObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(zooObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(Animal animal, Supervisor oldSupervisor, Supervisor newSupervisor) {
        for (zooObserver observer : observers) {
            observer.onSupervisorChanged(animal, oldSupervisor, newSupervisor);
        }
    }
}

