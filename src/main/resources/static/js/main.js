'use strict';
let table = null;


$().ready(function () {
    initDepartment();
    updateMailList();

    let tblView = $('#table-mails');
    table = tblView.DataTable({
        "pageLength": 6,
        "bLengthChange": false,
        "language": {
            "url": "/js/Russian.json"
        }
    });

    tblView.children('tbody').on('click', 'tr', function () {
        console.log(this);
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            table.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
            getDetails($(this).attr('id').split('_')[1]);


        }
    });

    let datapicker = $('.datepicker');
    datapicker.datepicker({
        setDate: new Date(),
        language: 'ru-RU',
        showOtherMonths: true,
        selectOtherMonths: true,
        autoclose: false,
        changeMonth: true,
        changeYear: true,
        startDate: "today",
        orientation: 'bottom'
    });

    datapicker.datepicker('setDate', new Date());

    let dateSelectView = $('#schedule-date');
    let dateInfoView = $('.schedule-info .date');
    let periodSelectView = $('#schedule-period');
    let periodInfoView = $('.schedule-info .period');

    dateSelectView.change(function () {
        dateInfoView.text($(this).val());
    });
    dateInfoView.text(dateSelectView.val());

    periodSelectView.change(function () {
        let text = $(this).children("option:selected").text();
        periodInfoView.text(text);
    });
    periodInfoView.text(periodSelectView.children("option:selected").text());


    $(document).on('submit', '.form-ajax', function () {

        return false;

    })
});

$.fn.serializeObject = function () {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function () {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


function getDetails(id) {

    // createModal('Детали рассылки', 'asdsa d');
    new Http()
        .preloader('#table-mails')
        .method('GET')
        .url('/api/mail/' + id)
        .send(function (msg, status) {

            let html = '';

            html += ' <div class="d-flex">' +
                '<div class="w-25">' +
                ' <div>Получатели:</div>' +
                '<div id="modal-addressee-list">';

            let created = new Date(0);
            created.setMilliseconds(msg.whenCreated);


            msg.addressee.forEach(function (address) {

                html += '<div>' +
                    '   <div>' + address.name + '</div>' +
                    '   <div class="font-small">' + address.email + '</div>' +
                    '</div>'
            });
            html += '</div>';
            html += '</div>';
            html += '<div class="w-75">' +
                '   <div><span>Создано:&nbsp;</span><span id="mail-mail-created">' + created.toLocaleDateString() + '</span></div>' +
                '   <div class="d-flex align-items-center">' +
                '   <div>Статус:&nbsp;<span>' + translateStatus(msg.mailTask.status) + '</span></div>' +
                '</div>' +
                '<div>Текст сообщения:</div>' +
                '   <div id="modal-mail-message">' + msg.message + '</div>' +
                '</div>';
            html += '</div>';

            let footer = '';
            footer += '  <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>' ;
            footer += '<button type="button" class="btn btn-primary" onclick="changeStatus( ' + msg.id + ', \'' + (msg.mailTask.status === 'IN_PROGRESS' ? 'PAUSED' : 'IN_PROGRESS') + '\')">' + (msg.mailTask.status === 'IN_PROGRESS' ? 'Приостановить' : 'Возобновить') + '</button>';


            createModal(msg.subject, html, footer);
        });


}

function changeStatus(id, status) {
    // let status = $('#mail-mail-status').children('option:selected').val();
    let mail = new Mail();
    mail.id = id;
    mail.status = status;
    let json = JSON.stringify(mail);
    new Http()
        .preloader('body')
        .method('PATCH')
        .body(json)
        .url('/api/mail/' + id + '/task/status/' + status )
        .send(function (msg, statusCode) {
            if (statusCode === 202) {
                showAlert('Статус рассылки успешно изменен', 'alert-success');
                destroyModal();
                updateMailList();
            } else {
                alert('Ой, что-то пошло не так');
            }
        });
}

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
                        '<div class="department-name d-flex align-items-center"  id="department-name-' + it.id + '">' +
                        '<div class="moz-border"><input type="checkbox"  id="select-all-' + it.id + '" onclick="selectAll(' + it.id + ', $(this).is(\':checked\'))" /></div>' +
                        '<div class="list-department-item w-100" onclick="toggleDepartment(' + it.id + ')" >' + it.name + '</div>' +
                        '<div class="control-container ' + (adminMode ? '' : 'd-none') + '">' +
                        '   <div class="control add" onclick="addAddressee(' + it.id + ')"></div>' +
                        '   <div class="control edit" onclick="editDepartment(' + it.id + ', \'' + it.name + '\')"></div>' +
                        '   <div class="control delete" onclick="deleteDepartment(' + it.id + ')"></div></div>' +
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


function sendForm(form, callback, isFile = false) {
    let data;
    if (!isFile) {
        data = $(form).serializeObject();
    } else {
        let a = $(form).find('input[type="file"]');
        data = new FormData;
        data.append('file', a.prop('files')[0]);

        $.ajax({
            url: $(form).attr('action'),
            type: "POST",
            data: data, // this will get all the input fields of your form.
            enctype: 'multipart/form-data',
            processData: false,  // tell jQuery not to process the data
            contentType: false,   // tell jQuery not to set contentType
            dataType: 'json', // as you want
            complete: function (response, code) {
                callback(response, code);
            }
        });

        return false;

    }


    new Http()
    // .preloader('#table-mails')
        .method($(form).attr('method'))
        .url($(form).attr('action'))
        .body(JSON.stringify(data))
        .send(function (msg, status) {
            callback(msg, status);
        });

    return false;
}

function callbackDepartment(msg, status) {
    initDepartment();
    destroyModal();
    showAlert('Успешно изменено', 'alert-success');
}

function callbackAddressee(msg, status) {
    $('input:checked').prop('checked', false);
    toggleDepartment(msg.department.id, true);
    destroyModal();
    showAlert('Успешно изменено', 'alert-success');
}

function editDepartment(id, name) {

    let html = '<form class="form-ajax d-flex" action="/api/department/' + id + '" method="patch" onsubmit="sendForm(this, callbackDepartment);">' +
        '<input class="form-control" name="name" value="' + name + '" />' +
        '<input class="btn btn-primary" type="submit" value="Сохранить"></form>';
    createModal('Редактировать департамент', html, null);

}

function editAddressee(id, name, email) {
    let html = '<form class="form-ajax d-flex" action="/api/addressee/' + id + '" method="patch" onsubmit="sendForm(this, callbackAddressee);">' +
        '<input class="form-control" name="name" value="' + name + '" />' +
        '<input class="form-control" type="email" name="email" value="' + email + '" />' +
        // '<input class="form-control" name="department" value="' + email + '" />' +
        '<input class="btn btn-primary" type="submit" value="Сохранить"></form>';
    createModal('Редактировать адрессата', html, null);
}

function addAddressee(id) {

    let html = '<ul class="nav nav-tabs" id="myTab" role="tablist">' +
        '  <li class="nav-item">' +
        '    <a class="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Одного</a>' +
        '  </li>' +
        '  <li class="nav-item">' +
        '    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Из файла</a>' +
        '  </li>' +
        '</ul>' +
        '<div class="tab-content" id="myTabContent">' +
        '<div class="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab"> <br/>' +
        '<form class="form-ajax d-flex" action="/api/department/' + id + '/addressee" method="POST" onsubmit="sendForm(this, callbackDepartment);">' +
        '<input class="form-control" name="name" placeholder="Имя" />' +
        '<input class="form-control" name="email"  type="email" placeholder="Email" />' +
        // '<input class="form-control" name="department" value="' + email + '" />' +
        '<input class="btn btn-primary" type="submit" value="Сохранить"></form>' +
        '</div>' +
        '<div class="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab"><br/>' +
        '<form class="form-ajax" action="/api/department/' + id + '/csv/file" method="post" enctype="multipart/form-data"  onsubmit="sendForm(this, callbackDepartment, true);">' +
        '                        <div class="input-group">' +
        '                            <div class="custom-file">' +
        '                                <input type="file" name="file" />' +
        '                            </div>' +
        '                        </div>' +
        '                        <div class="text-right">' +
        '                           <input type="submit" class="btn btn-primary" value="Добавить"/>' +
        '                        </div>' +
        '</form>' +
        '</div>'
    '</div>';

    createModal('Создать адрессата', html, null);
}

let adminMode = false;

function toggleMode(view) {

    adminMode = !adminMode;
    if (adminMode) {
        $('.control-container')
            .removeClass('d-none')
            .show();
        $(view).addClass('config_on');
    } else {
        $('.control-container')
            .addClass('d-none')
            .hide();
        $(view).removeClass('config_on');
    }
    console.log('asd');

}

function deleteDepartment(id) {
    if (confirm('При удалении департаменты будут удалены все получатели. Вы точно желаете удалить?')) {
        new Http()
            .body()
            .method("DELETE")
            .url('/api/department/' + id)
            .preloader('#addressee-container')
            .send(function (msg, status) {
                if (status !== 200) {
                    alert('Ой, что-то пошло не так');
                    return;
                }
                showAlert('Департамент удален', 'alert-success');
                initDepartment();
            });
    }
}

function deleteAddressee(id, department_id) {
    if (confirm('Вы точно желаете удалить?')) {
        new Http()
            .body()
            .method("DELETE")
            .url('/api/addressee/' + id)
            .preloader('#addressee-container')
            .send(function (msg, status) {
                if (status !== 200) {
                    alert('Ой, что-то пошло не так');
                    return;
                }
                showAlert('Адрессат удален', 'alert-success');
                toggleDepartment(department_id, true);

            });
    }
}

function addDepartment() {
    let html = '<form class="form-ajax d-flex" action="/api/department" method="post" onsubmit="sendForm(this, callbackDepartment);">' +
        '<input class="form-control" name="name" value="' + name + '" />' +
        // '<input class="form-control" name="department" value="' + email + '" />' +
        '<input class="btn btn-primary" type="submit" value="Создать"></form>';

    createModal('Создать департамент', html, null);

    // new Http()
    //     .body()
    //     .method("POST")
    //     .url('/api/department')
    //     .preloader('#addressee-container')
    //     .send(function (msg, status) {
    //         initDepartment();
    //     });
}

function toggleDepartment(id, state = null, callback = null) {
    let root = $('#department_' + id);

    let activeEl = root.find('.department-name');

    if (activeEl.hasClass('open-tree') && state === null) {
        root.find('ul').hide();
        activeEl.removeClass('open-tree');
        return false;
    }

    activeEl.addClass('open-tree');
    new Http()
        .method("GET")
        .url('/api/department/' + id + '/addressees')
        .preloader('#addressee-container')
        .send(function (msg) {
            let html = '';
            if (msg !== null && msg.length > 0) {
                msg.forEach(function (it) {
                    html += ' <li class="addressee-list-item">' +
                        '<div class="w-100">' +
                        '<div class="moz-border"><input type="checkbox" value="' + it.id + '"  onclick="onClickCheckAddressee( ' + id + ', event)"/></div>' +
                        '<div class="d-flex flex-column w-100">' +
                        '<div>' + it.name + '</div>' +
                        '<div class="font-small">' + it.email + '</div>' +
                        '</div>' +
                        '<div class="control-container ' + (adminMode ? '' : 'd-none') + '"><div class="control edit" onclick="editAddressee(' + it.id + ', \'' + it.name + '\' , \'' + it.email + '\' )"></div><div class="control delete" onclick="deleteAddressee(' + it.id + ', ' + id + ')"></div></div>' +
                        '</div>' +
                        '</li>';
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



function updateMailList() {

    new Http()
        .method("GET")
        .url('/api/mails')
        .preloader('#mail-list-container')
        .send(function (msg, code) {

            let html = '';
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
                    let node = table.row.add([item.id, item.subject, a_name.join(', '), '<div>' + translateStatus(item.mailTask.status) + '</div>', created.toLocaleDateString()])
                        .draw()
                        .node();
                    $(node).attr('id', 'mail_' + item.id);
                    $(node).addClass(item.status.toLowerCase());

                });
            }
        });
}

function translateStatus(status) {
    let result = status;
    switch (status.toUpperCase()) {
        case 'NEW':
            result = 'Новая задача';
            break;
        case 'IN_PROGRESS':
            result = 'Выполняется';
            break;
        case 'PAUSED':
            result = 'приостановлена';
            break;
        case 'DONE':
            result = 'выполнено';
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
        toggleDepartment(id, null, function () {
            $('#department_' + id + ' li input').prop('checked', state);
        })
    } else {
        $('#department_' + id + ' li input').prop('checked', state);
    }
}

function sendNow() {

    toggleSchedule(false);

    let dateStart = $('#schedule-date').val().split('.');
    let period = $('#schedule-period').val();

    let date = new Date();
    date.setFullYear(dateStart[2], dateStart[1] - 1, dateStart[0]);

    let a = date.getTime();
    console.log(date);
    console.log(a);

    console.log(dateStart);
    console.log(period);

    let task = new MailTask();
    task.startTime = date.getTime();
    task.period = period;
    // task.repeatsLeft =  period === 0 ? 0 : -1;

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
    mail.mailTask = task;

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
            resetForm();

            if (mail.addressee.length === 1) {
                updateMailList(mail.addressee[0].id);
            }
            updateMailList()
        });
}

function resetForm() {
    $('#schedule-period').val('ONCE');
    let datapicker = $('.datepicker');
    datapicker.datepicker('setDate', new Date());

    $('#new-message-subject').val('');
    $('#new-message-text').val('');
}

function createModal(title, text, footer = null) {

    $('#dynamic-modal .modal-title').text(title);
    $('#dynamic-modal .modal-body').empty().append(text);
    $('#dynamic-modal .modal-footer').empty().append(footer);
    $('#dynamic-modal').modal('show');
}

function destroyModal() {
    $('#dynamic-modal').modal('hide');
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

function toggleSchedule(isShow) {
    if (isShow) {
        // $('.schedule-container *').show();
        $('#schedule-btn-toggle').hide();

        $('.schedule-container').animate({width: '100%', height: '100%', top: '-40px'}, 200, function () {
            $('#schedule-btn-create').show();
            $('.schedule-container .datepicker').datepicker('show');
        });

    } else {
        $('.schedule-container .datepicker').datepicker('hide');
        $('.schedule-container').animate({width: 0, height: 0, top: '100%'}, 200, function () {
            $('#schedule-btn-toggle').show();
        });

    }
}



class Http {

    // _method;
    // _body;
    // _url;
    // _preloader;

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
