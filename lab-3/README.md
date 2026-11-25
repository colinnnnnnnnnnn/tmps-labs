# TMPS Laboratory Work No. 3 — Behavioral Design Patterns

This repository subproject contains the implementation for **Laboratory Work No. 3 (Behavioral Design Patterns)**, continuing the **Vintage Groove Records** domain.

---

## Implemented Patterns

1. **Strategy Pattern**:
   - `ShippingStrategy` interface with interchangeable calculation algorithms: `StandardGroundShippingStrategy`, `ExpressCourierShippingStrategy`, and `InternationalAirShippingStrategy`.
   - Strategies can be dynamically swapped on `VinylOrderContext` at runtime based on customer preference or geographical requirements.
2. **State Pattern**:
   - Manages the lifecycle of a vinyl order across 5 distinct states: `PendingPaymentState`, `ProcessingState`, `ShippedState`, `DeliveredState`, and `CancelledState`.
   - Transitions are strictly encapsulated in state classes; forbidden transitions (e.g. shipping before paying or cancelling dispatched orders) throw descriptive exceptions.
3. **Observer Pattern**:
   - `OrderSubject` manages dynamic subscriptions of `OrderObserver` listeners (`CustomerNotificationObserver`, `WarehouseInventoryObserver`, `AuditLoggerObserver`).
   - Any state transition automatically broadcasts events to all subscribed observers; listeners can attach and detach at runtime.
4. **Command Pattern**:
   - Encapsulates order operations into objects (`PayOrderCommand`, `DispatchOrderCommand`, `CancelOrderCommand`, `ChangeShippingStrategyCommand`) with full `undo()` and `redo()` support.
   - Business logic resides in the receiver (`VinylOrderContext`); `OrderCommandInvoker` tracks command history and execution logs.

---

## Directory Structure

```text
lab-3/
├── run.sh                  # One-click execution script for the main demo
├── test.sh                 # One-click execution script for the test suite
├── lab-3.iml               # IntelliJ IDEA project descriptor
├── README.md               # Build & run instructions (this document)
├── REPORT.md               # Laboratory verification report
├── src/
│   └── com/tmps/lab3/
│       ├── domain/         # Core domain models (RecordOrder, Builder, VinylRecord)
│       ├── strategy/       # Strategy pattern (ShippingStrategy and concrete strategies)
│       ├── state/          # State pattern (OrderState, 5 concrete states, VinylOrderContext)
│       ├── observer/       # Observer pattern (OrderObserver, OrderSubject, 3 observers)
│       ├── command/        # Command pattern (OrderCommand, concrete commands, Invoker)
│       └── client/         # Client demonstration (Main.java)
└── test/
    └── com/tmps/lab3/test/ # Automated Unit Test Suite & TestRunner
```

---

## Requirements

- **Java Development Kit**: JDK 11 or higher (`javac`, `java`)
- **Operating System**: Linux / macOS / Windows (with bash or terminal)

---

## How to Build and Run

### 1. Run the Interactive Demonstration
Execute the demo using the provided shell script:
```bash
./run.sh
```
Or manually using `javac` and `java`:
```bash
mkdir -p out/production/lab-3
javac -d out/production/lab-3 $(find src -name "*.java")
java -cp out/production/lab-3 com.tmps.lab3.client.Main
```

### 2. Run the Unit Test Suite (One Command)
Run all 8 unit tests across all behavioral patterns:
```bash
./test.sh
```
Or manually:
```bash
mkdir -p out/production/lab-3 out/test/lab-3
javac -d out/production/lab-3 $(find src -name "*.java")
javac -cp out/production/lab-3 -d out/test/lab-3 $(find test -name "*.java")
java -cp out/production/lab-3:out/test/lab-3 com.tmps.lab3.test.TestRunner
```
