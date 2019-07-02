class Mail {
    subject;
    message;
    attempts = 0;
    addressee;
    sender;
    mailTask;
    mailLog
}

class MailTask {
    startTime;
    period;
    repeatsLeft;
}

class Addressee {
    id
}
