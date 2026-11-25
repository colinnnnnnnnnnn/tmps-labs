# TMPS Laboratory Work No. 2 — Structural Design Patterns

This repository subproject contains the implementation for **Laboratory Work No. 2 (Structural Design Patterns)**, extending the **Vintage Groove Records** domain.

---

## Implemented Patterns

1. **Composite Pattern**:
   - Represents catalog items hierarchically (`SingleRecordItem` as Leaf, `VinylBoxSet` as Composite implementing `OrderItemComponent`).
   - Allows treating single records and complex nested box sets uniformly with automated bundle discounts and recursive item counts.
2. **Decorator Pattern**:
   - Dynamically augments invoice cost and itemized descriptions with value-added services (`UltrasonicCleaningDecorator`, `VinylInsuranceDecorator`, `GiftWrapDecorator`).
   - Enables stacking in arbitrary permutations without subclass explosion.
3. **Proxy Pattern**:
   - `SecuredOrderFulfillmentProxy` stands in for `StandardOrderFulfillmentService`, enforcing role-based authorization, high-value transaction approval, priority courier dispatch permissions, and immutable security audit logging.
4. **Facade Pattern**:
   - `StoreCheckoutFacade` provides a unified, simplified checkout interface orchestrating the subsystem (Order Builder, Composite Items, Proxy, and Decorators) without harboring business logic.

---

## Directory Structure

```text
lab-2/
├── run.sh                  # One-click execution script for the main demo
├── test.sh                 # One-click execution script for the test suite
├── lab-2.iml               # IntelliJ IDEA project descriptor
├── README.md               # Build & run instructions (this document)
├── REPORT.md               # Laboratory verification report
├── src/
│   └── com/tmps/lab2/
│       ├── domain/         # Core domain models (RecordOrder, Builder, Sessions)
│       ├── composite/      # Composite pattern (OrderItemComponent, SingleRecordItem, VinylBoxSet)
│       ├── decorator/      # Decorator pattern (OrderInvoice, BaseOrderInvoice, Decorators)
│       ├── proxy/          # Proxy pattern (OrderFulfillmentService, Proxy, Real Subject)
│       ├── facade/         # Facade pattern (StoreCheckoutFacade, Request/Response DTOs)
│       └── client/         # Client demonstration (Main.java)
└── test/
    └── com/tmps/lab2/test/ # Automated Unit Test Suite & TestRunner
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
mkdir -p out/production/lab-2
javac -d out/production/lab-2 $(find src -name "*.java")
java -cp out/production/lab-2 com.tmps.lab2.client.Main
```

### 2. Run the Unit Test Suite (One Command)
Run all 8 unit tests across all structural patterns:
```bash
./test.sh
```
Or manually:
```bash
mkdir -p out/production/lab-2 out/test/lab-2
javac -d out/production/lab-2 $(find src -name "*.java")
javac -cp out/production/lab-2 -d out/test/lab-2 $(find test -name "*.java")
java -cp out/production/lab-2:out/test/lab-2 com.tmps.lab2.test.TestRunner
```
