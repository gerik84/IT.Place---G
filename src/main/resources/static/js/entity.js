class Mail {
    subject;
    message;
    attempts = 0;
    addressee;
    sender;
    mailTask;
}

class MailTask {
    startTime;
    intervalTime;
    repeatsLeft;
}

class Addressee {
    id
}
