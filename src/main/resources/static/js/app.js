$(document).ready(function() {
    // 页面导航
    $('.nav-link').on('click', function(e) {
        e.preventDefault();
        $('.nav-link').removeClass('active');
        $(this).addClass('active');
        loadPage($(this).data('page'));
    });

    // 初始加载仪表盘
    loadPage('dashboard');
});

// 页面加载函数
function loadPage(page) {
    showLoading();
    $.get(`/templates/${page}.html`, function(data) {
        $('#page-content').html(data);
        initializePage(page);
    }).fail(function() {
        alert('加载页面失败');
    }).always(function() {
        hideLoading();
    });
}

// 页面初始化
function initializePage(page) {
    switch(page) {
        case 'dashboard':
            initDashboard();
            break;
        case 'mailbox-config':
            initMailboxConfig();
            break;
        case 'email-archive':
            initEmailArchive();
            break;
    }
}

// 仪表盘初始化
function initDashboard() {
    // 获取活跃邮箱数
    $.get('/api/mailbox', function(data) {
        const activeCount = data.filter(config => config.active).length;
        $('#active-mailboxes').text(activeCount);
    });

    // 获取最近归档邮件
    const today = new Date();
    const startOfDay = new Date(today.setHours(0, 0, 0, 0));
    $.get(`/api/archive/search?startTime=${startOfDay.toISOString()}&size=10`, function(data) {
        $('#today-emails').text(data.totalElements);
        updateRecentEmails(data.content);
    });
}

// 邮箱配置初始化
function initMailboxConfig() {
    loadMailboxList();

    // 保存邮箱配置
    $('#saveMailbox').on('click', function() {
        const mailboxData = {
            protocol: $('#protocol').val(),
            host: $('#host').val(),
            port: parseInt($('#port').val()),
            username: $('#username').val(),
            password: $('#password').val(),
            active: $('#active').is(':checked')
        };

        const mailboxId = $('#mailboxId').val();
        const url = mailboxId ? `/api/mailbox/${mailboxId}` : '/api/mailbox';
        const method = mailboxId ? 'PUT' : 'POST';

        $.ajax({
            url: url,
            method: method,
            contentType: 'application/json',
            data: JSON.stringify(mailboxData),
            success: function() {
                $('#mailboxModal').modal('hide');
                loadMailboxList();
            },
            error: function() {
                alert('保存失败');
            }
        });
    });
}

// 加载邮箱列表
function loadMailboxList() {
    $.get('/api/mailbox', function(data) {
        const tbody = $('#mailbox-list');
        tbody.empty();

        data.forEach(mailbox => {
            tbody.append(`
                <tr>
                    <td>${mailbox.id}</td>
                    <td>${mailbox.protocol}</td>
                    <td>${mailbox.host}</td>
                    <td>${mailbox.port}</td>
                    <td>${mailbox.username}</td>
                    <td>
                        <span class="status-badge ${mailbox.active ? 'status-active' : 'status-inactive'}"></span>
                        ${mailbox.active ? '启用' : '禁用'}
                    </td>
                    <td>
                        <button class="btn btn-sm btn-primary edit-mailbox" data-id="${mailbox.id}">
                            <i class="fa fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-danger delete-mailbox" data-id="${mailbox.id}">
                            <i class="fa fa-trash"></i>
                        </button>
                        <button class="btn btn-sm btn-success archive-now" data-id="${mailbox.id}">
                            <i class="fa fa-refresh"></i>
                        </button>
                    </td>
                </tr>
            `);
        });

        // 编辑邮箱
        $('.edit-mailbox').on('click', function() {
            const id = $(this).data('id');
            const mailbox = data.find(m => m.id === id);
            if (mailbox) {
                $('#mailboxId').val(mailbox.id);
                $('#protocol').val(mailbox.protocol);
                $('#host').val(mailbox.host);
                $('#port').val(mailbox.port);
                $('#username').val(mailbox.username);
                $('#password').val(''); // 出于安全考虑不显示密码
                $('#active').prop('checked', mailbox.active);
                $('#mailboxModal').modal('show');
            }
        });

        // 删除邮箱
        $('.delete-mailbox').on('click', function() {
            if (confirm('确定要删除该邮箱配置吗？')) {
                const id = $(this).data('id');
                $.ajax({
                    url: `/api/mailbox/${id}`,
                    method: 'DELETE',
                    success: function() {
                        loadMailboxList();
                    },
                    error: function() {
                        alert('删除失败');
                    }
                });
            }
        });

        // 立即归档
        $('.archive-now').on('click', function() {
            const id = $(this).data('id');
            $.post(`/api/archive/mailbox/${id}`, function() {
                alert('归档任务已启动');
            }).fail(function() {
                alert('启动归档任务失败');
            });
        });
    });
}

// 邮件归档页面初始化
function initEmailArchive() {
    let currentPage = 0;
    
    // 搜索表单提交
    $('#searchForm').on('submit', function(e) {
        e.preventDefault();
        currentPage = 0;
        loadEmails();
    });

    // 加载邮件列表
    function loadEmails() {
        const params = {
            fromEmail: $('#fromEmail').val(),
            startTime: $('#startTime').val(),
            endTime: $('#endTime').val(),
            page: currentPage,
            size: 10
        };

        $.get('/api/archive/search', params, function(data) {
            updateEmailList(data);
            updatePagination(data);
        });
    }

    // 更新邮件列表
    function updateEmailList(data) {
        const tbody = $('#email-list');
        tbody.empty();

        data.content.forEach(email => {
            tbody.append(`
                <tr class="email-item ${email.hasAttachment ? 'has-attachment' : ''}">
                    <td>${email.subject}</td>
                    <td>${email.fromEmail}</td>
                    <td>${email.toEmail}</td>
                    <td>${moment(email.receiveTime).format('YYYY-MM-DD HH:mm:ss')}</td>
                    <td>
                        ${email.hasAttachment ? '<i class="fa fa-paperclip"></i>' : ''}
                    </td>
                    <td>
                        <button class="btn btn-sm btn-info view-email" data-id="${email.id}">
                            <i class="fa fa-eye"></i>
                        </button>
                    </td>
                </tr>
            `);
        });

        // 查看邮件详情
        $('.view-email').on('click', function() {
            const email = data.content.find(e => e.id === $(this).data('id'));
            if (email) {
                $('#detail-subject').text(email.subject);
                $('#detail-from').text(email.fromEmail);
                $('#detail-to').text(email.toEmail);
                $('#detail-time').text(moment(email.receiveTime).format('YYYY-MM-DD HH:mm:ss'));
                $('#detail-body').html(email.body);

                const attachments = email.attachmentFilePaths ? email.attachmentFilePaths.split(',') : [];
                const attachmentHtml = attachments.map(path => 
                    `<div><i class="fa fa-file"></i> ${path}</div>`
                ).join('');
                $('#detail-attachments').html(attachmentHtml);

                $('#emailDetailModal').modal('show');
            }
        });
    }

    // 更新分页
    function updatePagination(data) {
        const pagination = $('#pagination');
        pagination.empty();

        // 上一页
        pagination.append(`
            <li class="page-item ${data.first ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage - 1}">上一页</a>
            </li>
        `);

        // 页码
        for (let i = 0; i < data.totalPages; i++) {
            pagination.append(`
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `);
        }

        // 下一页
        pagination.append(`
            <li class="page-item ${data.last ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${currentPage + 1}">下一页</a>
            </li>
        `);

        // 页码点击事件
        $('.page-link').on('click', function(e) {
            e.preventDefault();
            if (!$(this).parent().hasClass('disabled')) {
                currentPage = parseInt($(this).data('page'));
                loadEmails();
            }
        });
    }

    // 初始加载
    loadEmails();
}

// 更新最近邮件列表
function updateRecentEmails(emails) {
    const tbody = $('#recent-emails');
    tbody.empty();

    emails.forEach(email => {
        tbody.append(`
            <tr>
                <td>${email.subject}</td>
                <td>${email.fromEmail}</td>
                <td>${moment(email.receiveTime).format('YYYY-MM-DD HH:mm:ss')}</td>
                <td>${email.hasAttachment ? '<i class="fa fa-paperclip"></i>' : ''}</td>
            </tr>
        `);
    });
}

// 显示加载动画
function showLoading() {
    $('.loading').css('display', 'flex');
}

// 隐藏加载动画
function hideLoading() {
    $('.loading').css('display', 'none');
} 