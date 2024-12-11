package management;

import animals.Animal;

public interface zooObserver {
    void onSupervisorChanged(Animal animal, Supervisor oldSupervisor, Supervisor newSupervisor);
}
