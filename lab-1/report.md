# Laboratory Work Report: SOLID Design Principles

**Course:** Software Engineering / Techniques of Modeling and Software Design (TMPS)  
**Topic:** Implementation and Analysis of Fundamental SOLID Principles (SRP, OCP, DIP)  
**Language:** Java  

---

## 1. Introduction

In modern software engineering, writing software that merely functions is insufficient. As applications evolve, requirements inevitably change, scale expands, and software systems become increasingly complex. Without disciplined design foundations, codebases suffer from common architectural flaws such as **rigidity** (hard to change), **fragility** (changes break unrelated parts), and **immobility** (hard to reuse components).

To address these challenges, Robert C. Martin ("Uncle Bob") compiled the **SOLID** design principles in the early 2000s. These five principles provide a roadmap for developing modular, maintainable, extensible, and testable object-oriented software:
- **S** - Single Responsibility Principle (SRP)
- **O** - Open/Closed Principle (OCP)
- **L** - Liskov Substitution Principle (LSP)
- **I** - Interface Segregation Principle (ISP)
- **D** - Dependency Inversion Principle (DIP)

### Objective of the Laboratory Work
The primary objective of this laboratory work is to examine and demonstrate three fundamental principles from the SOLID suite:
1. **Single Responsibility Principle (SRP)**
2. **Open/Closed Principle (OCP)**
3. **Dependency Inversion Principle (DIP)**

By studying the concrete Java implementations in `src/`, this report explores how each principle is formulated theoretically, how it is practically realized in the source code, what anti-patterns it prevents, and the design benefits it yields.

---

## 2. Analysis of the Implemented Principles

The project contains three dedicated programs illustrating each targeted principle, along with an orchestration runner in `src/Main.java`.

---

### 2.1 Principle 1: Single Responsibility Principle (SRP)

#### Theoretical Concept
> *"A class should have one, and only one, reason to change."* — Robert C. Martin

The Single Responsibility Principle asserts that every class, module, or component must encapsulate a single, well-defined piece of functionality and purpose. A "responsibility" can be considered an axis of change or a specific stakeholder requirement. When a class handles multiple unrelated concerns (for example, managing domain state while simultaneously handling presentation, persistence, or network transmission), any change to one concern risks destabilizing the other.

#### Code Implementation Analysis (`src/SingleResponsibilityProgram.java`)

In `SingleResponsibilityProgram.java`, the domain scenario revolves around managing and displaying a report:

```java
static class Report {
    private final String title;

    Report(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }
}

static class ReportPrinter {
    void print(Report report) {
        System.out.println("Printing: " + report.getTitle());
    }
}
```

The execution flow is demonstrated in `main`:
```java
Report report = new Report("Monthly sales report");
ReportPrinter printer = new ReportPrinter();
printer.print(report);
```

#### How SRP Applies in the Code
1. **Separation of Concerns:**
   - **`Report` class:** Its sole responsibility is domain data encapsulation. It holds the attributes of a report (in this case, `title`) and exposes accessors. Its only reason to change is a change in what constitutes a report's data model.
   - **`ReportPrinter` class:** Its sole responsibility is output presentation. Its only reason to change is a change in the rendering mechanism or format (e.g., printing to console, rendering formatted ASCII, or routing output to a printer).

2. **Contrast with Anti-Pattern (SRP Violation):**
   If SRP were violated, the `Report` class would manage both its data and its printing logic:
   ```java
   // VIOLATION: Mixed responsibilities
   class Report {
       private String title;
       public void print() {
           System.out.println("Printing: " + title); // Mixed presentation logic
       }
   }
   ```
   In such a coupled design, if the printing format needs to change (e.g., printing as JSON, HTML, or routing to a physical printer), the core domain entity `Report` must be modified and recompiled, increasing regression risks.

3. **Benefits Observed:**
   - **High Cohesion:** Each class is focused on one task.
   - **Low Coupling:** `Report` has no dependency on the printing medium.
   - **Reusability:** `ReportPrinter` can be extended or swapped without impacting `Report`.

---

### 2.2 Principle 2: Open/Closed Principle (OCP)

#### Theoretical Concept
> *"Software entities (classes, modules, functions, etc.) should be open for extension, but closed for modification."* — Bertrand Meyer

The Open/Closed Principle dictates that software architecture should allow the behavior of a system to be extended without modifying its existing source code. When new requirements or features emerge, developers should add new code rather than altering tested, working code. This is predominantly achieved via **abstraction and polymorphism**.

#### Code Implementation Analysis (`src/OpenClosedProgram.java`)

In `OpenClosedProgram.java`, the system calculates the area of various geometric shapes:

```java
interface Shape {
    double area();
}

static class Circle implements Shape {
    private final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

static class Rectangle implements Shape {
    private final double width;
    private final double height;

    Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }
}

static class AreaCalculator {
    double calculate(Shape shape) {
        return shape.area();
    }
}
```

Usage in `main`:
```java
AreaCalculator calculator = new AreaCalculator();
System.out.println("Circle area: " + calculator.calculate(new Circle(3)));
System.out.println("Rectangle area: " + calculator.calculate(new Rectangle(4, 5)));
```

#### How OCP Applies in the Code
1. **Polymorphic Abstraction via `Shape`:**
   The `Shape` interface defines an abstract contract (`double area()`). The high-level consumer `AreaCalculator` depends solely on this contract.
2. **Closed for Modification:**
   `AreaCalculator.calculate(Shape shape)` does not inspect the concrete type of the shape and contains zero conditional logic. It simply invokes `shape.area()`. Therefore, `AreaCalculator` never needs to be edited when new geometric shapes are introduced.
3. **Open for Extension:**
   If a new requirement introduces a `Triangle` or `Polygon`, a developer simply creates a new class implementing `Shape`:
   ```java
   static class Triangle implements Shape {
       private final double base, height;
       Triangle(double base, double height) { this.base = base; this.height = height; }
       @Override public double area() { return 0.5 * base * height; }
   }
   ```
   The existing `AreaCalculator` handles `Triangle` seamlessly without a single modification.

4. **Contrast with Anti-Pattern (OCP Violation):**
   Without OCP, `AreaCalculator` would rely on type checking (e.g., `instanceof`) and conditionals:
   ```java
   // VIOLATION: Closed for extension, requires modification for every new shape
   class AreaCalculator {
       double calculate(Object shape) {
           if (shape instanceof Circle) {
               Circle c = (Circle) shape;
               return Math.PI * c.radius * c.radius;
           } else if (shape instanceof Rectangle) {
               Rectangle r = (Rectangle) shape;
               return r.width * r.height;
           }
           // Every new shape breaks open this method!
           throw new IllegalArgumentException();
       }
   }
   ```
   Such an approach is fragile, prone to bugs when adding shapes, and violates OCP.

5. **Benefits Observed:**
   - **Extensibility:** New shapes can be integrated without touching existing code.
   - **Stability:** Established calculation logic remains untouched, preventing unintended regressions.

---

### 2.3 Principle 3: Dependency Inversion Principle (DIP)

#### Theoretical Concept
> *"1. High-level modules should not depend on low-level modules. Both should depend on abstractions.*  
> *2. Abstractions should not depend on details. Details should depend on abstractions."* — Robert C. Martin

In traditional layered designs, high-level business components often directly instantiate and depend on low-level implementation details (such as database drivers, third-party APIs, or specific notification protocols). DIP reverses this dependency relationship: high-level policies dictate an abstract contract, and low-level modules implement that contract. Consequently, high-level logic remains independent of volatile implementation mechanisms.

#### Code Implementation Analysis (`src/DependencyInversionProgram.java`)

In `DependencyInversionProgram.java`, a notification subsystem sends alert messages:

```java
interface MessageSender {
    void send(String message);
}

static class EmailSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println("Email sent: " + message);
    }
}

static class NotificationService {
    private final MessageSender sender;

    NotificationService(MessageSender sender) {
        this.sender = sender;
    }

    void notify(String message) {
        sender.send(message);
    }
}
```

Usage in `main`:
```java
MessageSender emailSender = new EmailSender();
NotificationService service = new NotificationService(emailSender);
service.notify("Your order has shipped.");
```

#### How DIP Applies in the Code
1. **Identification of Module Hierarchy:**
   - **High-level module:** `NotificationService` (embodies the business rule: when an event occurs, notify the recipient).
   - **Low-level module:** `EmailSender` (embodies a concrete technological detail: sending via email).
   - **Abstraction:** `MessageSender` interface.
2. **Inversion of Dependencies:**
   Rather than `NotificationService` creating and depending directly on `EmailSender`, both `NotificationService` and `EmailSender` depend on the abstraction `MessageSender`.
3. **Dependency Injection (Constructor Injection):**
   The concrete dependency is passed into `NotificationService` via its constructor (`NotificationService(MessageSender sender)`). The service is completely agnostic to how the message is transmitted.
4. **Contrast with Anti-Pattern (DIP Violation):**
   If DIP were violated, `NotificationService` would instantiate its concrete dependency directly:
   ```java
   // VIOLATION: High-level module depends on low-level detail
   class NotificationService {
       private EmailSender emailSender = new EmailSender(); // Hardcoded coupling!

       void notify(String message) {
           emailSender.send(message);
       }
   }
   ```
   In this coupled design, `NotificationService` cannot be reused with SMS, Slack, or Webhook senders without rewriting its code. Furthermore, unit testing becomes difficult because `EmailSender` cannot easily be mocked.

5. **Benefits Observed:**
   - **Loose Coupling:** Swapping communication mechanisms (e.g., adding `SmsSender` or `PushNotificationSender`) requires no changes to `NotificationService`.
   - **Testability:** In automated testing environments, a mock or stub `MessageSender` can be injected effortlessly to verify notifications without sending real messages.

---

## 3. Demonstration and Execution Results

The program entry point `src/Main.java` sequentially invokes the entry points of all three programs:

```java
public class Main {
    public static void main(String[] args) {
        SingleResponsibilityProgram.main(args);
        OpenClosedProgram.main(args);
        DependencyInversionProgram.main(args);
    }
}
```

### Execution Output
Compiling and running the application produces the following output:

```text
1. Single Responsibility Principle
Printing: Monthly sales report

2. Open/Closed Principle
Circle area: 28.274333882308138
Rectangle area: 20.0

3. Dependency Inversion Principle
Email sent: Your order has shipped.
```

Each module operates independently, verifying that the separation of concerns, polymorphism, and dependency injection mechanisms function as intended.

---

## 4. Summary Table: Principles Comparison

| Principle | Primary Objective | Code Implementation in Project | Key Structural Mechanism |
| :--- | :--- | :--- | :--- |
| **Single Responsibility Principle (SRP)** | Ensure a class has only one reason to change; decouple unrelated concerns. | `Report` holds data; `ReportPrinter` handles presentation. | Class decomposition & focused responsibilities. |
| **Open/Closed Principle (OCP)** | Allow functionality extension without modifying existing code. | `Shape` interface with `Circle` and `Rectangle`; `AreaCalculator` accepts any `Shape`. | Polymorphism & interface abstraction. |
| **Dependency Inversion Principle (DIP)** | Decouple high-level policy from low-level implementation details. | `NotificationService` relies on `MessageSender` interface; `EmailSender` implements it. | Interface abstraction & Constructor Dependency Injection. |

---

## 5. Conclusions

This laboratory work explored the practical application of three fundamental SOLID design principles: the **Single Responsibility Principle (SRP)**, the **Open/Closed Principle (OCP)**, and the **Dependency Inversion Principle (DIP)** using Java.

Key conclusions drawn from this analysis include:
1. **Synergy of Principles:** The principles do not operate in isolation. Rather, they reinforce one another. For instance, achieving OCP in `AreaCalculator` relies on SRP (each shape is responsible only for its own geometry) and abstraction. Similarly, applying DIP naturally preserves SRP by preventing high-level services from taking on the responsibility of managing dependency lifecycles.
2. **Maintainability and Scalability:** Separating responsibilities into distinct classes and relying on abstractions significantly reduces code fragility. Modifying how a report is printed, adding a new geometric shape, or switching message delivery protocols can all be accomplished with zero changes to existing core logic.
3. **Testability:** Decoupling high-level business rules from specific external mechanisms (as demonstrated with DIP) makes systems inherently testable, allowing mock and stub implementations to be injected seamlessly during unit tests.

In conclusion, adhering to SOLID principles provides a disciplined foundation for building modular, robust, and adaptable software systems that accommodate evolving requirements while minimizing technical debt.
