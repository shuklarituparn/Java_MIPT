package animals;

class Hippo extends AbstractAnimal {
    public Hippo(int id, double height) {
        super(id, height);
    }

    @Override
    public String makeSound() {
        return "Grunt";
    }
}
