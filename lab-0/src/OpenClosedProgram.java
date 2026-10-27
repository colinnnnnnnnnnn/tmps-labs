public class OpenClosedProgram {

    public static void main(String[] args) {
        System.out.println("2. Open/Closed Principle");

        AreaCalculator calculator = new AreaCalculator();
        System.out.println("Circle area: " + calculator.calculate(new Circle(3)));
        System.out.println("Rectangle area: " + calculator.calculate(new Rectangle(4, 5)));

        System.out.println();
    }

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
}
