package animals;

class Fish extends AbstractAnimal {
    public Fish(int id, double height) {
        super(id, height);
    }

    @Override
    public boolean canMakeSound() {
        return false;
    }

    @Override
    public String makeSound() {
        return "";
    }
}
