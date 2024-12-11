package animals;

import management.Supervisor;

public abstract class AbstractAnimal implements Animal {
    private final int id;
    private final double height;
    private Supervisor supervisor;

    public AbstractAnimal(int id, double height) {
        this.id = id;
        this.height = height;
    }

    @Override
    public Integer getId() {
        return (Integer) id;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public Supervisor getSupervisor() {
        return supervisor;
    }

    @Override
    public boolean canMakeSound() {
        return true;
    }
}
