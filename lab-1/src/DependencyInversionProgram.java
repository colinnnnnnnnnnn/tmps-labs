public class DependencyInversionProgram {

    public static void main(String[] args) {
        System.out.println("3. Dependency Inversion Principle");

        MessageSender emailSender = new EmailSender();
        NotificationService service = new NotificationService(emailSender);
        service.notify("Your order has shipped.");
    }

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
}
