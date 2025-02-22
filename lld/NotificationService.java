


interface Message<M> {
   M getMessage();

}

class SimpleMessage implements Message<String> {
    private String content;

    public SimpleMessage(String content) {
        this.content = content;
    }

    @Override
    public String getMessage() {
        // modify the content based on SMS Message format;
        return content;
    }

    @Override
    public String toString() {
        return content;
    }
}


interface Notification<T extends Message> {
    void send(T message);    
}


class NotificationDecorator<T extends Message> implements Notification<T> {
    private Notification<T> wrappee;

    public NotificationDecorator(Notification<T> notification) {
        this.wrappee = notification;
    }

    @Override
    public void send(T message) {
       wrappee.send(message); 
    }
}

class BasicNotification<M extends Message> implements Notification<M> {
    @Override
    public void send(M message) {
        System.out.println("Dummy notification implementation: " + message);
    }
}


class SMSNotificationDecorator<M extends Message> extends NotificationDecorator<M> {
    private String phoneNumber;
    public SMSNotificationDecorator(Notification<M> notification,
                                    String phoneNumber) {
        super(notification);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void send(M message) {
        super.send(message); 
        System.out.println("Sending message: " + message + ", to phone number: " + phoneNumber); 
    }
}

class EmailNotificationDecorator<M extends Message> extends NotificationDecorator<M> {
    private String fromEmail;
    private String toEmail;

    public EmailNotificationDecorator(Notification<M> notification,
                                        String fromEmail,
                                        String toEmail) {
        super(notification);
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
    }

    @Override
    public void send(M message) {
        super.send(message);
        System.out.println("Sending message from: " + fromEmail + ", to: " + toEmail + ", with message: " + message);
    }
}

class NotificationBuilder<M extends Message> {
    private Notification<M> notification;

    public NotificationBuilder() {
        this.notification = new BasicNotification<M>();
    }

    public NotificationBuilder<M> withSMS(String phoneNumber) {
        this.notification = new SMSNotificationDecorator<M>(notification, phoneNumber);
        return this;
    }

    public NotificationBuilder<M> withEmail(String fromEmail, String toEmail) {
        this.notification = new EmailNotificationDecorator<M>(notification, fromEmail, toEmail);
        return this;
    }

    public Notification<M> build() {
        return notification;
    } 
}

public class NotificationService {
    public static void main(String[] args) {
       Notification<SimpleMessage> notification = new NotificationBuilder<SimpleMessage>()
            .withSMS("+919954")
            .withEmail("from@email.com", "to@email.com")
            .build();
        notification.send(new SimpleMessage("This is a test messsage"));
        

        //var message = new SimpleMessage("Test without Builder");

        //var basicNot = new BasicNotification<SimpleMessage>();
        //var emailDecorator = new EmailNotificationDecorator<SimpleMessage>(basicNot, "from@email.com", "to@email.com");
        //var smsDecorator = new SMSNotificationDecorator<SimpleMessage>(emailDecorator, "+919954");
        //
        //smsDecorator.send(message);
    }
}
