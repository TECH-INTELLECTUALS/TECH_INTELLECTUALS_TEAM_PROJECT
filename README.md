# University Campus Service Hub

## 1. Project Overview

### What is this project?

The University Campus Service Hub is a modular Java application built for a Data Structures and Algorithms course. It manages campus operations such as maintenance requests, shuttle services, lecture halls, hostels, laboratories, resource movement, campus routing, and service scheduling.

### What problem does it solve?

Students, staff, and campus administrators need a unified system to request and coordinate campus services, plan efficient routes between buildings, allocate resources, and monitor service performance. This project provides a structured, maintainable platform for these needs using custom-built data structures and algorithms.

### Main features

- Maintenance and service request tracking
- Shuttle and route planning across campus
- Medical\Emergence requests
- Scheduling of service requests and task assignments
- Custom data structures instead of Java built-in collections
- Algorithmic support for search, sorting, graph traversal, and optimization

### Overall architecture

The application is built as separate layers and packages so each team can work independently. The main flow is:

- `controllers` accept user input and coordinate the system
- `services` execute business workflows
- `algorithms` provide reusable algorithm implementations
- `datastructures` provide custom core structures used by services and algorithms
- `database` manages persistence and repository access
- `models` define domain entities
- `utils` supply shared helpers and constants
- `exceptions` define domain-specific error handling
- `tests` hold test scaffolding for each module

---

## 2. Project Folder Structure

```
src/
│
├── app
├── controllers
├── services
├── database
├── datastructures
├── algorithms
├── models
├── interfaces
├── utils
├── exceptions
└── tests
```

### Packages and responsibilities

- `app`: Application entry points and bootstrap classes.
- `controllers`: Orchestrates user interaction and coordinates services.
- `services`: Implements core campus business logic and workflows.
- `database`: Manages persistence, database lifecycle, and repository access.
- `datastructures`: Contains custom implementations of core data structures.
- `algorithms`: Contains custom algorithm implementations and optimizers.
- `models`: Defines domain entities and data objects.
- `interfaces`: Defines common contracts for services, data structures, repositories, and algorithms.
- `utils`: Utility classes, configuration constants, validation helpers, and timing tools.
- `exceptions`: Custom exception types for controlled error handling.
- `tests`: Placeholder test classes and unit test scaffolding.

---

## 3. Team Responsibilities

### Data Structures Team

**Purpose:** Build and maintain the custom core data structures required for the assessed implementation.

**Responsibilities:**

- Design and implement each custom data structure.
- Avoid Java built-in collection classes for core implementations.
- Provide stable APIs for algorithms and services.
- Write documentation and usage notes.

**Deliverables:**

- `DynamicArray`
- `LinkedList`
- `Stack`
- `Queue`
- `CircularQueue`
- `Deque`
- `PriorityQueue`
- `Heap`
- `HashTable`
- `BinarySearchTree`
- `RedBlackTree`
- `BTree`
- `Graph`
- `DisjointSet`

#### DynamicArray

- Purpose: Resizable array storage for sequential elements.
- Used by: algorithms, services that require random access storage.
- Responsibilities: resizing, indexing, add/remove, capacity management.

#### LinkedList

- Purpose: Ordered dynamic list of nodes.
- Used by: Graph, Queue, Stack, DFS/BFS, and list-based workflows.
- Responsibilities: insert, remove, traversal, node linking.

#### Stack

- Purpose: LIFO structure for backtracking and nested operations.
- Used by: DFS, algorithm stacks, service workflows that require undo-style logic.
- Responsibilities: push, pop, peek, isEmpty.

#### Queue

- Purpose: FIFO structure for breadth-first operations and scheduling.
- Used by: BFS, scheduling workflows, resource queues.
- Responsibilities: enqueue, dequeue, peek, isEmpty.

#### CircularQueue

- Purpose: Fixed-capacity queue with wrap-around storage.
- Used by: round-robin scheduling, service dispatch buffers.
- Responsibilities: enqueue, dequeue, available slot management.

#### Deque

- Purpose: Double-ended queue for flexible front/back operations.
- Used by: routing, algorithm search fronts, service request ordering.
- Responsibilities: add/remove at both ends, peek front/back.

#### PriorityQueue

- Purpose: Priority-based ordering for service and route scheduling.
- Used by: scheduling service, Dijkstra, greedy optimizers.
- Responsibilities: insert by priority, remove highest/lowest priority.

#### Heap

- Purpose: Efficient priority storage supporting quick extraction.
- Used by: Dijkstra, Prim, scheduling algorithms, PriorityQueue internals.
- Responsibilities: heapify, insert, remove, peek root.

#### HashTable

- Purpose: Fast lookup and key-based storage.
- Used by: search services, repository caches, index mappings.
- Responsibilities: insert, remove, retrieve, handle collisions.

#### BinarySearchTree

- Purpose: Ordered search tree for sorted inserts and lookups.
- Used by: search services, sorted domain data.
- Responsibilities: insert, delete, search, traversals.

#### RedBlackTree

- Purpose: Balanced search tree for reliable log-time operations.
- Used by: sorted service data, search indexing.
- Responsibilities: insert, delete, balancing, rotation.

#### BTree

- Purpose: Balanced tree for higher fan-out and block-based storage.
- Used by: database-like indexing or bulk sorted data.
- Responsibilities: split, merge, search, insert, delete.

#### Graph

- Purpose: Model campus locations and route connectivity.
- Used by: route planning, Dijkstra, Prim, Kruskal, BFS/DFS.
- Responsibilities: add vertices, add edges, traverse neighbors.

#### DisjointSet

- Purpose: Manage grouping and connectivity for union-find operations.
- Used by: Kruskal, connectivity checks, clustering.
- Responsibilities: union, find, path compression.

---

### Algorithms Team

**Purpose:** Implement algorithm logic used by the system to solve search, sorting, traversal, and optimization problems.

**Responsibilities:**

- Build clear algorithm APIs.
- Use custom data structures from the Data Structures Team.
- Document inputs, outputs, and complexity.
- Ensure algorithms are reusable across services.

**Deliverables:**

- `LinearSearch`
- `BinarySearch`
- `SelectionSort`
- `InsertionSort`
- `MergeSort`
- `QuickSort`
- `BreadthFirstSearch`
- `DepthFirstSearch`
- `DijkstraAlgorithm`
- `PrimAlgorithm`
- `KruskalAlgorithm`
- `GreedyScheduler`
- `DynamicProgrammingOptimizer`

#### Linear Search

- Problem solved: sequential lookup in an unsorted dataset.
- Input: collection and search key.
- Output: match result or index.
- Data structures required: `DynamicArray`, `LinkedList`, or other iterable containers.
- Expected complexity: O(n).
- Dependencies: basic data traversal.

#### Binary Search

- Problem solved: lookup in a sorted dataset.
- Input: sorted collection and search key.
- Output: match result or index.
- Data structures required: `DynamicArray`, `BinarySearchTree`-style ordered structure.
- Expected complexity: O(log n).
- Dependencies: sorted data source.

#### Selection Sort

- Problem solved: simple in-place sorting.
- Input: unsorted collection.
- Output: sorted collection.
- Data structures required: `DynamicArray`.
- Expected complexity: O(n²).
- Dependencies: item comparison and swap operations.

#### Insertion Sort

- Problem solved: stable sorting for small or nearly sorted data.
- Input: unsorted collection.
- Output: sorted collection.
- Data structures required: `DynamicArray`.
- Expected complexity: O(n²).
- Dependencies: comparison and ordered insertion.

#### Merge Sort

- Problem solved: efficient divide-and-conquer sort.
- Input: collection to sort.
- Output: sorted collection.
- Data structures required: `DynamicArray`, temporary storage.
- Expected complexity: O(n log n).
- Dependencies: recursive merging.

#### Quick Sort

- Problem solved: efficient partition-based sorting.
- Input: collection to sort.
- Output: sorted collection.
- Data structures required: `DynamicArray`.
- Expected complexity: average O(n log n), worst O(n²).
- Dependencies: pivot selection and partitioning.

#### Breadth-First Search (BFS)

- Problem solved: shortest-path layer traversal on graphs.
- Input: `Graph` and start vertex.
- Output: visitation order or distance map.
- Data structures required: `Queue`, `Graph`.
- Expected complexity: O(V + E).
- Dependencies: graph adjacency traversal.

#### Depth-First Search (DFS)

- Problem solved: deep traversal and path exploration.
- Input: `Graph` and start vertex.
- Output: visitation order or path.
- Data structures required: `Stack` or recursion, `Graph`.
- Expected complexity: O(V + E).
- Dependencies: graph traversal.

#### Dijkstra Algorithm

- Problem solved: shortest path in weighted graphs.
- Input: weighted `Graph`, source node.
- Output: shortest path tree or distance map.
- Data structures required: `Graph`, `PriorityQueue`, `Heap`.
- Expected complexity: O((V + E) log V).
- Dependencies: `Graph`, `Heap`, `PriorityQueue`.

#### Prim Algorithm

- Problem solved: minimum spanning tree.
- Input: weighted connected `Graph`.
- Output: MST edge set.
- Data structures required: `Graph`, `PriorityQueue`, `Heap`.
- Expected complexity: O(E log V).
- Dependencies: graph adjacency and priority queue.

#### Kruskal Algorithm

- Problem solved: minimum spanning tree via edge sorting.
- Input: weighted `Graph` edge list.
- Output: MST edge set.
- Data structures required: `Graph`, `DisjointSet`, sorting algorithm.
- Expected complexity: O(E log E).
- Dependencies: `DisjointSet`, sort implementation.

#### Greedy Scheduler

- Problem solved: service request scheduling using greedy heuristics.
- Input: list of service requests and priorities.
- Output: ordered schedule.
- Data structures required: `PriorityQueue`, `Queue`, `HashTable`.
- Expected complexity: O(n log n).
- Dependencies: scheduling data and priority queue.

#### Dynamic Programming Optimizer

- Problem solved: optimization problems with overlapping subproblems.
- Input: optimization state and constraints.
- Output: optimized plan or value.
- Data structures required: `DynamicArray`, `HashTable`.
- Expected complexity: depends on problem, typically polynomial.
- Dependencies: memoization structures and problem formulation.

---

### Database Team

**Purpose:** Manage persistence, database setup, and repository access for domain entities.

**Classes and responsibilities:**

#### DatabaseConnection

- Opens and closes the database connection.
- Manages connection sessions and configuration.

#### DatabaseManager

- Creates tables and initializes the database.
- Coordinates repositories and persistence lifecycle.

#### LocationRepository

- CRUD operations for `Location` entities.
- Responsible for storing and retrieving campus location data.

#### ServiceRequestRepository

- CRUD operations for `ServiceRequest` entities.
- Responsible for request persistence and retrieval.

#### ResourceRepository

- CRUD operations for `Resource` entities.
- Responsible for resource availability persistence.

#### AlgorithmRunRepository

- Stores algorithm execution results and performance metrics.
- Responsible for experiment logging and analytics data.

#### AuditRepository

- Stores audit history and system event logs.
- Responsible for recording user actions and system events.

**Repository communication with Services:**

- Services call repository methods to persist or query domain entities.
- The database layer should remain decoupled from business logic.
- Each service uses only the repositories it needs.

---

### Services Team

**Purpose:** Implement the business operations that power campus workflows.

**Services and responsibilities:**

#### SchedulingService

- Purpose: Manage service request scheduling and dispatch.
- Uses: `PriorityQueue`, `Queue`, `ServiceRequestRepository`.
- Responsibilities: build schedules, assign work, trigger follow-up workflows.

#### RouteService

- Purpose: Plan campus routes and navigation.
- Uses: `Graph`, `DijkstraAlgorithm`.
- Responsibilities: compute best routes, validate connectivity, support campus navigation.

#### SearchService

- Purpose: Search campus data entities and resources.
- Uses: `BinarySearchTree`, `HashTable`, search algorithms.
- Responsibilities: locate locations, requests, resources, and route metadata.

#### ResourceService

- Purpose: Manage campus resource inventory and allocation.
- Uses: `ResourceRepository`, `ServiceRequestRepository`, scheduling logic.
- Responsibilities: track resources, allocate material, manage availability.

#### ReportingService

- Purpose: Produce reports and analytics for operations.
- Uses: `AlgorithmRunRepository`, `AuditRepository`.
- Responsibilities: generate usage and performance reports.

---

### Controllers Team

**Purpose:** Coordinate user-facing workflows and orchestrate the underlying services.

**Responsibilities:**

- Receive and interpret user commands or startup options.
- Route execution to services.
- Manage application startup and lifecycle.

#### Main

- Purpose: Application startup entry point.
- Responsibilities: bootstrap the controller and launch the system.

#### CampusServiceController

- Purpose: Orchestrate the main campus service functionality.
- Responsibilities: coordinate services, repositories, and user workflows.

#### MenuController

- Purpose: Handle menu display and user navigation.
- Responsibilities: present options and dispatch selections to controllers.

---

### Models Team

**Purpose:** Define the domain entities used across the system.

#### Location

- Represents a campus location: building, lab, hostel, shuttle stop.

#### Road

- Represents a path or connection between two locations.

#### ServiceRequest

- Represents a maintenance or service request submitted by users.

#### Resource

- Represents a campus resource, such as furniture, equipment, or vehicles.

#### AlgorithmRun

- Represents an algorithm execution record for analysis.

#### AuditEvent

- Represents a logged event for auditing and traceability.

---

### Utilities Team

**Purpose:** Provide shared helpers, validation, timing, and configuration.

#### CSVLoader

- Loads and parses CSV data for import scenarios.

#### ValidationUtils

- Validates request payloads and domain inputs.

#### PerformanceTimer

- Measures algorithm and service execution performance.

#### Constants

- Stores application-wide constants and configuration keys.

---

### Exceptions Team

**Purpose:** Define domain-specific exception types for clean error handling.

#### InvalidRequestException

- Thrown when a request is invalid or malformed.

#### ResourceUnavailableException

- Thrown when a requested resource is unavailable.

#### RouteNotFoundException

- Thrown when a campus route cannot be found.

#### DuplicateRecordException

- Thrown when attempting to persist a duplicate entity.

---

## 4. System Architecture

```
User
  ↓
Controller
  ↓
Service
  ↓
Algorithms
  ↓
Custom Data Structures
  ↓
Repository
  ↓
Database
```

### Flow explanation

- Users interact with the application through controllers.
- Controllers invoke services to perform business workflows.
- Services call algorithms and custom data structures for computation.
- Services call repositories to persist or retrieve domain entities.
- Repositories use `DatabaseConnection` and `DatabaseManager` to manage storage.

---

## 5. Package Dependencies

| Package          | Can Access                                                                                | Notes                                 |
| ---------------- | ----------------------------------------------------------------------------------------- | ------------------------------------- |
| `controllers`    | `services`, `models`, `exceptions`, `interfaces`                                          | Entry point for application workflows |
| `services`       | `datastructures`, `algorithms`, `database`, `models`, `exceptions`, `utils`, `interfaces` | Business logic layer                  |
| `database`       | `models`                                                                                  | Persistence layer only                |
| `datastructures` | `interfaces`                                                                              | Core custom structures                |
| `algorithms`     | `datastructures`, `interfaces`, `models`                                                  | Compute layer                         |
| `models`         | none                                                                                      | Domain entities                       |
| `interfaces`     | none                                                                                      | Contracts and shared types            |
| `utils`          | none                                                                                      | Shared helpers and constants          |
| `exceptions`     | none                                                                                      | Custom error types                    |
| `tests`          | all packages                                                                              | Validation and unit tests             |

---

## 6. Git Workflow

### Branch naming

- `feature/<team>-<short-description>` for new features.
- `bugfix/<team>-<short-description>` for fixes.
- `refactor/<team>-<short-description>` for structural changes.
- `doc/<short-description>` for documentation updates.

### Commit message conventions

- Use present tense: `Add`, `Fix`, `Update`, `Refactor`.
- Include the team or package area.
- Example: `Add Graph adjacency support for route planning`

### Pull requests

- Create a pull request for every feature branch.
- Include a clear description of what changed and why.
- List impacted packages and any assumptions.
- Add reviewers from affected teams.

### Code reviews

- Review for correctness, clarity, design, and test coverage.
- Verify implementation uses custom data structures where required.
- Ensure services only depend on repositories and algorithms, not direct database details.

### Merge strategy

- Use merge requests after approvals.
- Prefer fast-forward or squash merges when history must stay clean.
- Do not merge incomplete or unreviewed work.

---

## 7. Coding Standards

### Java naming conventions

- Classes and interfaces: `PascalCase`.
- Methods and variables: `camelCase`.
- Constants: `UPPER_SNAKE_CASE`.

### Package naming

- Use lowercase package names.
- Keep package names descriptive and aligned with responsibilities.

### Method naming

- Use verbs for actions: `saveLocation`, `executeSearch`, `loadCsv`.
- Keep names clear and intention-revealing.

### Documentation

- Add JavaDoc comments for public classes and methods.
- Document purpose, inputs, outputs, and behavior.

### Comments

- Use comments for design intent and TODO markers.
- Avoid redundant comments that restate obvious code.

### Formatting

- Use 4-space indentation.
- Keep line length readable and consistent.
- Use blank lines to separate logical groups of code.

---

## Notes for contributors

- Respect the assessed requirement: do not use built-in Java collections for core data structure implementations.
- Keep modules decoupled and follow package responsibilities.
- If you need to add a new shared utility, add it under `utils` with documentation.
- Use `tests` to scaffold unit testing for your package.
