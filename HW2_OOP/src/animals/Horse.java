package animals;

class Horse extends AbstractAnimal {
    public Horse(int id, double height) {
        super(id, height);
    }

    @Override
    public String makeSound() {
        return "Neigh";
    }
}
