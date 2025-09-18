# Smart Home Simulator

Smart Home Simulator is a Java-based application developed as a college project to practice and demonstrate various software design patterns. The project simulates a smart home environment. Me and my colleague developed this application for educational purposes, particularly to deepen understanding of object-oriented principles and established design patterns.

## Purpose

This project was built to practice applying classic software design patterns. Each major feature or component in the simulator is implemented using a specific pattern, making the codebase a practical reference for learning and reviewing these concepts.

## Implemented Design Patterns

Below is a list of design patterns used in this project, along with a brief description of their application:

- **Singleton Pattern:**  
  Ensures that certain classes (e.g., Simulation, Timetracker, House etc.) have only one instance throughout the application's lifecycle.

- **Observer Pattern:**
  DoesStuffEveryTenMinutes Class

- **Decorator Pattern:**  
  Adds additional features to Bills  at runtime (Bill class) without modifying the core class.

- **State Design Pattern:**  
  Models complex behavior that changes based on internal state (Door class).

  - **Chain of Responsibility Design Pattern:**  
  Choosing correct logger based on the event

## How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Filouss/Smart-Home-Simulator.git
   cd Smart-Home-Simulator
   ```
2. **Build and Run:**
   - Open in your preferred Java IDE (e.g., IntelliJ IDEA, Eclipse)
   - Build the project
   - Run the main class
