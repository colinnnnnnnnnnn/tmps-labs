public class SingleResponsibilityProgram {

    public static void main(String[] args) {
        System.out.println("1. Single Responsibility Principle");

        Report report = new Report("Monthly sales report");
        ReportPrinter printer = new ReportPrinter();
        printer.print(report);

        System.out.println();
    }

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
}
