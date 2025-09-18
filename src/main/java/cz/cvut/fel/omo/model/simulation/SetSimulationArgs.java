package cz.cvut.fel.omo.model.simulation;

/**
 * @param totalFloors the total floors in the house
 * @param roomsPerFloor the total rooms on each floor of the house
 * @param totalPeople the total amount of people in the house (1 will be an admin)
 * @param totalCats the total amount of cats in the house
 */
public record SetSimulationArgs (int totalFloors, int roomsPerFloor, int totalPeople, int totalCats, long seed) { }
