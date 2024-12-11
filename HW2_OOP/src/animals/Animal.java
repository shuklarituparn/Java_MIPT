package animals;

import management.Supervisor;

public interface Animal {
    Integer getId();

    double getHeight();

    boolean canMakeSound();

    void setSupervisor(Supervisor supervisor);

    Supervisor getSupervisor();

    String makeSound();
}
