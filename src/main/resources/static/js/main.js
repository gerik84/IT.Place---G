$().ready(function () {
    initDepartment();
});

function initDepartment() {
    new Http()
        .body()
        .method("GET")
        .url('/api/departments')
        .preloader('#addressee-container')
        .send(function (msg) {
            console.log(msg);
            // msg = JSON.parse(msg);
            let html = '';
            if (msg !== null && msg.length > 0) {
                msg.forEach(function (it) {
                    html += '<li id="department_' + it.id + '">' +
                        '<div class="department-name d-flex align-items-end"  id="department-name-' + it.id + '">' +
                        '<input type="checkbox" onclick="selectAll(' + it.id + ', $(this).is(\':checked\'))" />' +
                        '<div class="list-department-item" onclick="toggleDepartment('+it.id+')" >' + it.name + '</div>' +
                        '</div>' +
                        '<ul id="child_' + it.id + '"></ul>' +
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
            console.log(msg);
            let html = '';
            if (msg !== null && msg.length > 0) {
                msg.forEach(function (it) {
                    html += ' <li class="addressee-list-item">' +
                        '                                <input type="checkbox" value="' + it.name + '" />' +
                        '                                <div class="d-flex flex-column">' +
                        '                                    <div>' + it.name + '</div>' +
                        '                                    <div class="font-small">' + it.email + '</div>' +
                        '                                </div></li>';
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


function selectAll(id, state) {
    console.log(state);

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
            body: this._body,
            async: true,
            complete: function (msg, status) {
                clearTimeout(timeoutInstant);
                loading.remove();
            },
            success: function (msg) {
                // preload.removeClass('loading');

                callback(msg);
            }
        })
    }
}
