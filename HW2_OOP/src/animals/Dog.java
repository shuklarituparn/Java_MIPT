package animals;

class Dog extends AbstractAnimal {
    public Dog(int id, double height) {
        super(id, height);
    }

    @Override
    public String makeSound() {
        return "Woof";
    }
}
