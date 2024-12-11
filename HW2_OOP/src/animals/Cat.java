package animals;

class Cat extends AbstractAnimal {
    public Cat(int id, double height) {
        super(id, height);
    }

    @Override
    public String makeSound() {
        return "Meow";
    }
}
