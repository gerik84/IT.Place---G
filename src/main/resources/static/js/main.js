$().ready(function () {
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
                    html += '<li><div class="list-department-item" id="' + it.id + '"><div class="department-name">' + it.name + '</div></div><ul></ul></li>';

                });
                $('#addressee-list').empty().append(html);
            } else {
                $('#addressee-list').empty().append('<div class="empty-table">Ничего не найдено</div>');
            }
        });

    $(document).on('click', '.list-department-item', function (event) {

        if ($(this).hasClass('open-tree')) {
            console.log($(this));
            $(this).parent().find('ul').hide();
            $(this).removeClass('open-tree');
            event.preventDefault();
            return false;
        }

        let id = $(this).attr('id');
        $(this).addClass('open-tree');
        let dom = $(this).parent();
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
                    dom.find('ul').empty();
                    dom.find('ul').show();
                    dom.find('ul').append(html);
                } else {
                    // $('#addressee-list').empty().append('<div class="empty-table">Ничего не найдено</div>');
                }
            });
        return false;
    })
});

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
