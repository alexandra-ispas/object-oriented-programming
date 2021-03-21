package entities.producers;

import entities.energyType.CreateEnergyType;
import entities.energyType.EnergyType;
import entities.distributors.Distributor;
import entities.producers.observer.Observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public final class Producer extends Observable {
    private final Map<Integer, List<Integer>> distributorsInEachMonth;
    private final List<Observer> observers = new ArrayList<>();
    private int id;
    private EnergyType energyType;
    private int maxDistributors;
    private float price;
    private long energyPerDistributor;

    public Producer(final int id, final String energyType, final int maxDistributors,
                    final float price, final long energyPerDistributor) {
        this.id = id;
        this.energyType = CreateEnergyType.getInstance().createEnergyType(energyType);
        this.maxDistributors = maxDistributors;
        this.price = price;
        this.energyPerDistributor = energyPerDistributor;
        this.distributorsInEachMonth = new HashMap<>();
    }

    public List<Observer> getObservers() {
        return observers;
    }

    /**
     * set the producer to be 'updated'
     * and notify all the observers
     */
    public void setUpdated() {
        notifyAllObservers();
    }

    /**
     * add a new observer to the list
     * @param observer to be added
     */
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * remove an observer from the list
     * @param observer to be removed
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * notify all the observers
     */
    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public int getMaxDistributors() {
        return maxDistributors;
    }

    public void setMaxDistributors(int maxDistributors) {
        this.maxDistributors = maxDistributors;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public long getEnergyPerDistributor() {
        return energyPerDistributor;
    }

    public void setEnergyPerDistributor(long energyPerDistributor) {
        this.energyPerDistributor = energyPerDistributor;
    }

    public Map<Integer, List<Integer>> getDistributorsInEachMonth() {
        return distributorsInEachMonth;
    }

    /**
     * add list of observers to the map with
     * all the distributors from each month
     * @param month when the list of distributors exist
     */
    public void addDistributors(int month) {

        List<Integer> distributors = new ArrayList<>();
        for (Observer observer : observers) {
            distributors.add(((Distributor) observer).getId());
        }

        distributorsInEachMonth.put(month, distributors);
    }
}
