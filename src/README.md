
> *From simulations, fighting animal movement logic, to solving imaginary bug tickets, POO takes me everywhere*

# Project overview - Baroana Cosmin George 323CA Tema 2 POO

- Bug Tracker Ticket Pro Solver

## Table of Contents:
- High-level architecture and package responsibilities, explaining the design patterns used.
- Class-by-class summaries for core entities.
- Key method descriptions.
- Runtime/workflow: initialization, command processing, day by day processing, outputs.
- Important behaviors, special-cases and edge-case notes / assumptions.

## High-level architecture and package resposibilities

- `main` — packet that includes everything logic-wise. `App` represents the entry point class for the aforementioned logic, initializes useful fields, loads Inputs, calls the Engine of the application.
- `fileio` — helpers and utilities for parsing input JSON and serializing output JSON nodes. Helps the App class to parse input.
- `engine` - contains the `Engine` class (a singleton), responsible for iterating through our ticket manager simulator day by day, command by command.
- `command` - houses all of the commands and the Command Pattern implementation, as well as the `Strategies` needed for some of the commands to work.
- `database` - houses the `Database` class (a singleton), housing universal information about the different entites that are participating in the current ticket processing simulation.
- `globals` - houses global interfaces and enums that are used all over the project, that are not packet specific.
- `milestones` - contains the `Milestone` class, which defines a simple Milestone, as well as useful enums for it.
- `tickets` - contains the abstract `Ticket` class, the different types of tickets and helper classes that are useful for it. All tickets are built using a builder.
- `users` - contains the models for the different types of Users, as well as useful Enums specific to them.

## Design Patterns

### Singleton

- I've used the singleton to implement the `Database` and the `Engine` classes because they are the brain and the heart of the whole project. I want there to be only one of each. However, the biggest reason that made me choose to use the Singleton Design Pattern for these 2 specific classes was because of the ease of debugging that this feature brings to the table. Being able to see all the entities inside the Project at any point inside the IntelliJ debugger is amazing.

### Builder

- I've decided to use the Builder in the `Ticket` class becuase of the fact that tickets have optional fields. I've chose to disregard the Director because I didn't find it necessary at this moment in time. Same with `Milestone`, even though it is not as restrictive.

### Observer

- I've decided to use the Observer to implement the `Notification` logic for obvious reasons. I wanted each of the existing Developer's that were affected by the Notification system to have an ease to manage way of adding notifications to their "inbox".

### Factory and Static Factory

- I've decided to use the Factory and Static Factory Design Patterns for the `User and Action` classes because they did not have the same restrictions as the tickets, by that I mean optional fields. Because I was also more familiar with them, I thought that they were a good solution for `User and Action` creation.

### Strategy

- Different `Commands` needed different `Strategies` depending on the type of User who called them. What better way to distinguish between `User` restrictions while mantaing the same overarching logic that the `Strategy` command pattern. For example, each `Generate` advance command comes with different types of formulas, for which I devised special classes that I choose depeding on the formula required using the `Strategy` Pattern. I plan to use it more wisely in the future submissions, but for now this is what I could come up with.

### Command 

- We have a lot of `Requests` coming in that contain a big enough logic behind them to prompt me to use this pattern. Through it, we create different stand-alone Objects to help us process the requests using the global `Singletons` to help us.

## Core classes and responsibilities

### Engine / Database

- `Engine` (singleton)
  - Responsibilities: overall coordinator. Manages simulation sessions, command processing, and output generation.
  - This class is the entry point into the code I've written.
  - Key methods:
    - `void run(String inputPath, String outputPath)` — main loop that processes commands sequentially.
    - `void reset()` — clear and reinitialize between simulations.
  - Holds state for whether a simulation is active.

- `Database` (singleton)
  - Responsibilities: global registry of environment entities and convenience accessors. It contains the entire data given as an input.
  - Key methods:
    - `void reset()` — clear and reinitialize between simulations.

  - Holds authoritative state for cells and entities.

### Command / CommandFactory

- `Command` (abstract Class):
  - Responsibilities: encapsulates a command's execution logic and helps set up different common fields between the commands.
  - Key method:
    - `void execute(CommandInput input, ArrayNode outputArray)` — performs the command's action, mutating state and appending results/errors to output.


### Ticket
- `Ticket` (abstract Class). I'm really proud of this class, even though it is enourmous I challeged myself to use different concepts that I am not used to and I think that the end result is acceptable.
  - Responsibilities: encapsulates a ticket's properties and behaviors.
  - Key ideas:
    - Uses the Builder pattern to handle optional fields.
    - Uses a record to define the comments structure.
    - Uses a Static Factory to create actions associated with the ticket.
    - Uses generic types.
  - Key methods:
    - `<T> void executeAddAction()` — takes a dynamic input and creates a new action using the Action Factory.
    - `void addAction()` — adds an action to the ticket's action list, overloaded for the differnet types of action inputs.
  - Action classes implement deepCopying via constructors.


### User and subclasses

- `User` (abstract Class) and subclasses (`Developer`, `Reporter, `Manager`):
  - Responsibilities: encapsulates user properties and behaviors.
  - Key ideas:
    - Uses a Factory to create users based on type.
  - Key methods:
    - `void clearNotifications(Notification notification)` — Developer specific, uses the clear() method to clear the Developer's notifications list.


## Runtime/workflow

1. Initialization
   - Input JSON describes the commands to run, parsed by `InputLoader`.
   - `App` initializes `Engine` and `Database` singletons, loading initial state, initializing output array.
   - Engine.run(inputLoader, outputs) starts the main simulation loop.
   - Between each test, the singletons are reset for a clean start.
   - `Engine` maintains simulation state and runs the logic on a day-by-day -> command-by-command basis.

2. Command processing loop
   - For each parsed command, `Command.execute(input, outputArray)` runs the appropriate handler.
   - Each handler validates preconditions and either mutates state or appends an error message.

3. State updates & ticks
   - Some commands prefer to defer state updates until the end of the day, which is handled by different reserve fields.
   - Computed changes are ran from the `Database` at the `START` of each day.
   - We ignore mistakes in the tests by running the commands in cronological order, and if they are not in cronological order, we just run them if they were supposed to be ran on a day before the current day.

## Debugging

- ALL the debugging was done using the IntelliJ debugger because I love it and it is really fun. One of the main reasons I preferred Singletons in my project.

## Use of LLM
- All the code inside this project and all the logic was thought out by me. All the LLM prompts used during the development of this homework were used for project structure opitmizations, understanding Intellij, learning new ways to solve problems more efficiently (for example learning to use Jackson or Lombok and other dependecies)(this README is NOT created by AI).
- Examples of prompts used:
  - "is lombok used in the industry java big projects? should I use the @Getter @Setter annotations in my homework?"
  - "in java, does Map.put() add the new entry in alphabetical order?"
  - "intellij filled out my C DRIVE FULLY HOW DO I STOP IT ITS ZGOONA CRASH MY PC" (actual prompt used becuase my C drive got full because of intellij caches)
  - "in the string formatting, what is the % for an int" (i forgor)
  - "can i somehow see all the TODOs in my project"
  - "how do i write good javadoc. give me some examples and site to read from"
  - "how to get a double to have oly the fist 2 decimas"