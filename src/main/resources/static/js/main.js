let table = null;
$().ready(function () {
    initDepartment();
    updateMailList();

    table = $('#table-mails').DataTable({
        "pageLength": 6,
        "bLengthChange": false,
        "language": {
            "url": "/js/Russian.json"
        }
    });


});

function initDepartment() {
    new Http()
        .body()
        .method("GET")
        .url('/api/departments')
        .preloader('#addressee-container')
        .send(function (msg) {
            // msg = JSON.parse(msg);
            let html = '';
            if (msg !== null && msg.length > 0) {
                msg.forEach(function (it) {
                    html += '<li id="department_' + it.id + '">' +
                        '<div class="department-name d-flex align-items-end"  id="department-name-' + it.id + '">' +
                        '<input type="checkbox" id="select-all-' + it.id + '" onclick="selectAll(' + it.id + ', $(this).is(\':checked\'))" />' +
                        '<div class="list-department-item" onclick="toggleDepartment('+it.id+')" >' + it.name + '</div>' +
                        '</div>' +
                        '<ul class="department-child-list" id="child_' + it.id + '"></ul>' +
                        '</li>';

                });
                $('#addressee-list').empty().append(html);
            } else {
                $('#addressee-list').empty().append('<div class="empty-table">Ничего не найдено</div>');
            }
        });
}

function toggleDepartment(id, callback = null) {
    let root = $('#department_' + id);

    let activeEl = root.find('.department-name');

    if (activeEl.hasClass('open-tree')) {
        root.find('ul').hide();
        activeEl.removeClass('open-tree');
        return false;
    }
    activeEl.addClass('open-tree');
    new Http()
        .body()
        .method("GET")
        .url('/api/department/' + id + '/addressees')
        .preloader('#addressee-container')
        .send(function (msg) {
            let html = '';
            if (msg !== null && msg.length > 0) {
                msg.forEach(function (it) {
                    html += ' <li class="addressee-list-item">' +
                        '<input type="checkbox" value="' + it.id + '"  onclick="onClickCheckAddressee( ' + id + ', event)"/>' +
                        '<div class="d-flex flex-column">' +
                        '<div>' + it.name + '</div>' +
                        '<div class="font-small">' + it.email + '</div>' +
                        '</div></li>';
                });
                html += '';
                root.find('ul').empty();
                root.find('ul').show();
                root.find('ul').append(html);

                if(callback != null) {
                    callback();
                }
            } else {
                // $('#addressee-list').empty().append('<div class="empty-table">Ничего не найдено</div>');
            }
        });
    return false;
}

function onClickCheckAddressee(parent_id, event) {
    event.stopPropagation();
    let all = $('#child_' + parent_id + ' li input');
    let selected = $('#child_' + parent_id + ' li input:checked');
    $('#select-all-' + parent_id).prop('checked', all.length === selected.length);
}

function getDetails(id, view) {
    // $('.addressee-list-item').removeClass('selected');
    // $('.auto-checked').prop('checked', false);
    // $('.auto-checked').removeClass('auto-checked');
    // $(view).addClass('selected');
    // $(view).find('input').addClass('auto-checked');
    // $(view).find('input').prop('checked', true);
    // updateMailList(id);
}


function updateMailList() {

    new Http()
        .method("GET")
        .url('/api/mails')
        .preloader('#mail-list-container')
        .send(function (msg, code) {

            html = '';
            if (msg.length === 0) {
                html += '<div class="text-center">Список рассылок пуст</div>'
            } else {

                table.rows().remove();

                msg.forEach(function (item) {
                    let a_name = [];
                    let a_emai = [];

                    item.addressee.forEach(function (addresses) {
                        a_name.push(addresses.name);
                        a_emai.push(addresses.email);
                    });

                    let created = new Date(0);
                    created.setMilliseconds(item.whenCreated);
                    let node = table.row.add([item.id, item.subject, a_name.join(', '), '<div>' + translateStatus(item.status) + '</div>', created.toLocaleDateString()])
                        .draw()
                        .node();
                    $(node).addClass(item.status.toLowerCase());

                });
            }
        });
}

function translateStatus(status) {
    let result = status;
    switch (status.toUpperCase()) {
        case 'NEW':
            result = 'Новое';
            break;
        case 'SENT':
            result = 'Отправлено';
            break;
        case 'FAILED':
            result = 'Ошибка';
            break;
        case 'CANCELLED':
            result = 'Отмененное';
            break;

    }
    return result;

}

function selectAll(id, state) {
    $('#child_' + id + ' li input').prop('checked', state);
    //
    if (state && !$('#department-name-' + id).hasClass('open-tree')) {
        toggleDepartment(id, function () {
            $('#department_' + id + ' li input').prop('checked', state);
        })
    } else {
        $('#department_' + id + ' li input').prop('checked', state);
    }
}

function sendNow() {

    let check = $('.department-child-list li input:checked');
    let addressees = [];

    if (check.length === 0) {
        showAlert('Необходимо выбрать получателя', 'alert-danger');
        return;
    }

    check.each(function (key, item) {
        let a = new Addressee();
        a.id = $(item).val();
        addressees.push(a);
    });


    let subject = $('#new-message-subject').val();
    let text = $('#new-message-text').val();

    if (subject.length === 0 || text.length === 0) {
        showAlert('Необходимо заполнить <b>Тему</b> и <b>Текст</b> письма', 'alert-danger');
        return;
    }

    let mailTask = new MailTask();
    mailTask.intervalTime = 0;
    mailTask.repeatsLeft = 0;
    mailTask.startTime = 0;

    let mail = new Mail();
    mail.addressee = addressees;
    mail.subject = subject;
    mail.message = text;


    let json = JSON.stringify(mail);
    new Http()
        .body(json)
        .method("POST")
        .url('/api/mail')
        .preloader('#create-message-container')
        .send(function (msg, code) {
            if (code !== 201) {
                alert('Ой, что-то пошло не так, повторите попытку поже');
                return;
            }

            showAlert('Сообщение добавлено в очередь отправки', 'alert-success');
            $('#new-message-subject').val('');
            $('#new-message-text').val('');

            if (mail.addressee.length === 1) {
                updateMailList(mail.addressee[0].id);
            }
            updateMailList()
        });
}

function createModal(title, text) {

    $('#dynamic-modal .title').text(title);
    $('#dynamic-modal .modal-body').empty().append(text);
    $('#dynamic-modal').modal('show');
}

var timeoutInstan = null;

function showAlert(text, style) {
    if (timeoutInstan !== null)
        clearTimeout(timeoutInstan);

    $("#alert-dynamic")
        .removeAttr('class')
        .attr('class', '')
        .addClass('alert')
        .addClass(style)
        .empty()
        .append(text)
        .show();

    timeoutInstan = setTimeout(function () {
        $('#alert-dynamic').hide()
    }, 2000)

}

function hideAlert() {

}

class Http {

    _method;
    _body;
    _url;
    _preloader;

    method(m) {
        this._method = m;
        return this;
    }

    url(m) {
        this._url = m;
        return this;
    }


    preloader(m) {
        this._preloader = m;
        return this;
    }

    body(data) {
        this._body = data;
        return this;
    }

    send(callback) {
        let preload = $(this._preloader);
        let loading = $('<div>', {class: 'loading'});
        let timeoutInstant = setTimeout(function () {
            preload.append(loading);
        }, 300);

        $.ajax({
            type: this._method,
            contentType: "application/json",
            url: this._url,
            dataType: "json",
            data: this._body,
            async: true,
            complete: function (msg, status) {
                clearTimeout(timeoutInstant);
                loading.remove();
                let response = null;
                if (msg.responseText != null && msg.responseText.length > 0) {
                    response = JSON.parse(msg.responseText);
                }
                callback(response, msg.status);

            },

        })
    }
}
