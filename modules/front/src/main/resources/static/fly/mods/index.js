﻿/**

 @Name: Fly社区主入口

 */


layui.define(['layer', 'laytpl', 'form', 'element', 'upload', 'util'], function(exports){

    var $ = layui.jquery
        ,layer = layui.layer
        ,laytpl = layui.laytpl
        ,form = layui.form
        ,element = layui.element
        ,upload = layui.upload
        ,util = layui.util
        ,device = layui.device()

        ,DISABLED = 'layui-btn-disabled';

    //阻止IE7以下访问
    if(device.ie && device.ie < 8){
        layer.alert('如果您非得使用 IE 浏览器访问Fly社区，那么请使用 IE8+');
    }

    layui.focusInsert = function(obj, str){
        var result, val = obj.value;
        obj.focus();
        if(document.selection){ //ie
            result = document.selection.createRange();
            document.selection.empty();
            result.text = str;
        } else {
            result = [val.substring(0, obj.selectionStart), str, val.substr(obj.selectionEnd)];
            obj.focus();
            obj.value = result.join('');
        }
    };


    //数字前置补零
    layui.laytpl.digit = function(num, length, end){
        var str = '';
        num = String(num);
        length = length || 2;
        for(var i = num.length; i < length; i++){
            str += '0';
        }
        return num < Math.pow(10, length) ? str + (num|0) : num;
    };

    var fly = {

        //Ajax
        json: function(url, data, success, options){
            var that = this, type = typeof data === 'function';

            if(type){
                options = success
                success = data;
                data = {};
            }

            options = options || {};

            return $.ajax({
                type: options.type || 'post',
                dataType: options.dataType || 'json',
                data: data,
                url: url,
                success: function(res){
                    if(res.result === "true") {
                        success && success(res);
                    } else {
                        layer.msg(res.message, {shift: 6});
                        options.error && options.error();
                    }
                }, error: function(e){
                    layer.msg(e.responseJSON.message, {shift: 6});
                    options.error && options.error(e);
                }
            });
        }

        //计算字符长度
        ,charLen: function(val){
            var arr = val.split(''), len = 0;
            for(var i = 0; i <  val.length ; i++){
                arr[i].charCodeAt(0) < 299 ? len++ : len += 2;
            }
            return len;
        }

        ,form: {}

        //简易编辑器
        ,layEditor: function(options){
            var html = ['<div class="layui-unselect fly-edit">'
                ,'<span type="face" title="插入表情"><i class="iconfont icon-yxj-expression" style="top: 1px;"></i></span>'
                ,'<span type="picture" title="插入图片：img[src]"><i class="iconfont icon-tupian"></i></span>'
                ,'<span type="href" title="超链接格式：a(href)[text]"><i class="iconfont icon-lianjie"></i></span>'
                ,'<span type="code" title="插入代码或引用"><i class="iconfont icon-emwdaima" style="top: 1px;"></i></span>'
                ,'<span type="hr" title="插入水平线">hr</span>'
                ,'<span type="preview" title="预览"><i class="iconfont icon-yulan1"></i></span>'
                ,'</div>'].join('');

            var log = {}, mod = {
                face: function(editor, self){ //插入表情
                    var str = '', ul, face = fly.faces;
                    for(var key in face){
                        str += '<li title="'+ key +'"><img src="'+ face[key] +'"></li>';
                    }
                    str = '<ul id="LAY-editface" class="layui-clear">'+ str +'</ul>';
                    layer.tips(str, self, {
                        tips: 3
                        ,time: 0
                        ,skin: 'layui-edit-face'
                    });
                    $(document).on('click', function(){
                        layer.closeAll('tips');
                    });
                    $('#LAY-editface li').on('click', function(){
                        var title = $(this).attr('title') + ' ';
                        layui.focusInsert(editor[0], 'face' + title);
                    });
                }
                ,picture: function(editor){ //插入图片
                    layer.open({
                        type: 1
                        ,id: 'fly-jie-upload'
                        ,title: '插入图片'
                        ,area: 'auto'
                        ,shade: false
                        ,area: '465px'
                        ,fixed: false
                        ,offset: [
                            editor.offset().top - $(window).scrollTop() + 'px'
                            ,editor.offset().left + 'px'
                        ]
                        ,skin: 'layui-layer-border'
                        ,content: ['<ul class="layui-form layui-form-pane" style="margin: 20px;">'
                            ,'<li class="layui-form-item">'
                            ,'<label class="layui-form-label">URL</label>'
                            ,'<div class="layui-input-inline">'
                            ,'<input required name="image" placeholder="支持直接粘贴远程图片地址" value="" class="layui-input">'
                            ,'</div>'
                            ,'<button type="button" class="layui-btn layui-btn-primary" id="uploadImg"><i class="layui-icon">&#xe67c;</i>上传图片</button>'
                            ,'</li>'
                            ,'<li class="layui-form-item" style="text-align: center;">'
                            ,'<button type="button" lay-submit lay-filter="uploadImages" class="layui-btn">确认</button>'
                            ,'</li>'
                            ,'</ul>'].join('')
                        ,success: function(layero, index){
                            var image =  layero.find('input[name="image"]');
                            //执行上传实例
                            upload.render({
                                elem: '#uploadImg'
                                ,url: '/js/a/file/upload'
                                ,size: 2048
                                ,acceptMime: 'image/*'
                                ,choose:function (obj) {
                                    var file = $(".layui-upload-file").get(0).files[0];
                                    this.data = {
                                        "fileName":file.name,
                                        "fileMd5":Math.random(),
                                        "uploadType":"image"
                                    };
                                }
                                ,done: function(res){
                                    if(res.result == "true"){
                                        image.val("/js" + res.fileUpload.fileUrl);
                                    } else {
                                        layer.msg(res.message, {icon: 5});
                                    }
                                }
                            });

                            form.on('submit(uploadImages)', function(data){
                                var field = data.field;
                                if(!field.image) return image.focus();
                                layui.focusInsert(editor[0], 'img['+ field.image + '] ');
                                layer.close(index);
                            });
                        }
                    });
                }
                ,href: function(editor){ //超链接
                    layer.prompt({
                        title: '请输入合法链接'
                        ,shade: false
                        ,fixed: false
                        ,id: 'LAY_flyedit_href'
                        ,offset: [
                            editor.offset().top - $(window).scrollTop() + 'px'
                            ,editor.offset().left + 'px'
                        ]
                    }, function(val, index, elem){
                        if(!/^http(s*):\/\/[\S]/.test(val)){
                            layer.tips('这根本不是个链接，不要骗我。', elem, {tips:1})
                            return;
                        }
                        layui.focusInsert(editor[0], ' a('+ val +')['+ val + '] ');
                        layer.close(index);
                    });
                }
                ,code: function(editor){ //插入代码
                    layer.prompt({
                        title: '请贴入代码或任意文本'
                        ,formType: 2
                        ,maxlength: 10000
                        ,shade: false
                        ,id: 'LAY_flyedit_code'
                        ,area: ['800px', '360px']
                    }, function(val, index, elem){
                        layui.focusInsert(editor[0], '[pre]\n'+ val + '\n[/pre]');
                        layer.close(index);
                    });
                }
                ,hr: function(editor){ //插入水平分割线
                    layui.focusInsert(editor[0], '[hr]');
                }
                ,preview: function(editor, span){ //预览
                    var othis = $(span), getContent = function(){
                        var content = editor.val();
                        return /^\{html\}/.test(content)
                            ? content.replace(/^\{html\}/, '')
                            : fly.content(content)
                    }, isMobile = device.ios || device.android;

                    if(mod.preview.isOpen) return layer.close(mod.preview.index);

                    mod.preview.index = layer.open({
                        type: 1
                        ,title: '预览'
                        ,shade: false
                        ,offset: 'r'
                        ,id: 'LAY_flyedit_preview'
                        ,area: [
                            isMobile ? '100%' : '775px'
                            ,'100%'
                        ]
                        ,scrollbar: isMobile ? false : true
                        ,anim: -1
                        ,isOutAnim: false
                        ,content: '<div class="detail-body layui-text" style="margin:20px;">'+ getContent() +'</div>'
                        ,success: function(layero){
                            editor.on('keyup', function(val){
                                layero.find('.detail-body').html(getContent());
                            });
                            mod.preview.isOpen = true;
                            othis.addClass('layui-this');
                        }
                        ,end: function(){
                            delete mod.preview.isOpen;
                            othis.removeClass('layui-this');
                        }
                    });
                }
            };

            layui.use('face', function(face){
                options = options || {};
                fly.faces = face;
                $(options.elem).each(function(index){
                    var that = this, othis = $(that), parent = othis.parent();
                    parent.prepend(html);
                    parent.find('.fly-edit span').on('click', function(event){
                        var type = $(this).attr('type');
                        mod[type].call(that, othis, this);
                        if(type === 'face'){
                            event.stopPropagation()
                        }
                    });
                });
            });

        }

        ,escape: function(html){
            return String(html||'').replace(/&(?!#?[a-zA-Z0-9]+;)/g, '&amp;')
                .replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/'/g, '&#39;').replace(/"/g, '&quot;');
        }

        //内容转义
        ,content: function(content){
            //支持的html标签
            var html = function(end){
                return new RegExp('\\n*\\['+ (end||'') +'(pre|hr|div|span|p|table|thead|th|tbody|tr|td|ul|li|ol|li|dl|dt|dd|h2|h3|h4|h5)([\\s\\S]*?)\\]\\n*', 'g');
            };
            content = fly.escape(content||'') //XSS
                .replace(/img\[([^\s]+?)\]/g, function(img){  //转义图片
                    return '<img src="' + img.replace(/(^img\[)|(\]$)/g, '') + '">';
                }).replace(/@(\S+)(\s+?|$)/g, '@<a href="javascript:;" class="fly-aite">$1</a>$2') //转义@
                .replace(/face\[([^\s\[\]]+?)\]/g, function(face){  //转义表情
                    var alt = face.replace(/^face/g, '');
                    var src = fly.faces[alt];
                    return '<img alt="'+ alt +'" title="'+ alt +'" src="' + src + '">';
                }).replace(/a\([\s\S]+?\)\[[\s\S]*?\]/g, function(str){ //转义链接
                    var href = (str.match(/a\(([\s\S]+?)\)\[/)||[])[1];
                    var text = (str.match(/\)\[([\s\S]*?)\]/)||[])[1];
                    if(!href) return str;
                    var rel =  /^(http(s)*:\/\/)\b(?!(\w+\.)*(sentsin.com|layui.com))\b/.test(href.replace(/\s/g, ''));
                    return '<a href="'+ href +'" target="_blank"'+ (rel ? ' rel="nofollow"' : '') +'>'+ (text||href) +'</a>';
                }).replace(html(), '\<$1 $2\>').replace(html('/'), '\</$1\>') //转移HTML代码
                .replace(/\n/g, '<br>') //转义换行
            return content;
        }

        //新消息通知
        ,newmsg: function(){
            var elemUser = $('.fly-nav-user');
            if(layui.cache.user.uid !== -1 && elemUser[0]){
                fly.json('/message/nums/', {
                    _: new Date().getTime()
                }, function(res){
                    if(res.status === 0 && res.count > 0){
                        var msg = $('<a class="fly-nav-msg" href="javascript:;">'+ res.count +'</a>');
                        elemUser.append(msg);
                        msg.on('click', function(){
                            fly.json('/message/read', {}, function(res){
                                if(res.status === 0){
                                    location.href = '/user/message/';
                                }
                            });
                        });
                        layer.tips('你有 '+ res.count +' 条未读消息', msg, {
                            tips: 3
                            ,tipsMore: true
                            ,fixed: true
                        });
                        msg.on('mouseenter', function(){
                            layer.closeAll('tips');
                        })
                    }
                });
            }
            return arguments.callee;
        }

    };

    //签到
    var tplSignin = ['{{# if(d.signed){ }}'
        ,'<button class="layui-btn layui-btn-disabled">今日已签到</button>'
        ,'<span>获得了<cite>{{ d.experience }}</cite>飞吻</span>'
        ,'{{# } else { }}'
        ,'<button class="layui-btn layui-btn-danger" id="LAY_signin">今日签到</button>'
        ,'<span>可获得<cite>{{ d.experience }}</cite>飞吻</span>'
        ,'{{# } }}'].join('')
        ,tplSigninDay = '已连续签到<cite>{{ d.days }}</cite>天'

        ,signRender = function(data){
            laytpl(tplSignin).render(data, function(html){
                elemSigninMain.html(html);
            });
            laytpl(tplSigninDay).render(data, function(html){
                elemSigninDays.html(html);
            });
        }

        ,elemSigninHelp = $('#LAY_signinHelp')
        ,elemSigninTop = $('#LAY_signinTop')
        ,elemSigninMain = $('.fly-signin-main')
        ,elemSigninDays = $('.fly-signin-days');

    if(elemSigninMain[0]){
        /*
        fly.json('/sign/status', function(res){
          if(!res.data) return;
          signRender.token = res.data.token;
          signRender(res.data);
        });
        */
    }
    $('body').on('click', '#LAY_signin', function(){
        var othis = $(this);
        if(othis.hasClass(DISABLED)) return;

        fly.json('/js/f/front/user/sign', null, function(res){
            signRender(res);
        }, {
            error: function(){
                othis.removeClass(DISABLED);
            }
        });

        othis.addClass(DISABLED);
    });

    //签到说明
    elemSigninHelp.on('click', function(){
        layer.open({
            type: 1
            ,title: '签到说明'
            ,area: '300px'
            ,shade: 0.8
            ,shadeClose: true
            ,content: ['<div class="layui-text" style="padding: 20px;">'
                ,'<blockquote class="layui-elem-quote">“签到”可获得社区飞吻，规则如下</blockquote>'
                ,'<table class="layui-table">'
                ,'<thead>'
                ,'<tr><th>连续签到天数</th><th>每天可获飞吻</th></tr>'
                ,'</thead>'
                ,'<tbody>'
                ,'<tr><td>＜5</td><td>5</td></tr>'
                ,'<tr><td>≥5</td><td>10</td></tr>'
                ,'<tr><td>≥15</td><td>15</td></tr>'
                ,'<tr><td>≥30</td><td>20</td></tr>'
                ,'<tr><td>≥100</td><td>30</td></tr>'
                ,'<tr><td>≥365</td><td>50</td></tr>'
                ,'</tbody>'
                ,'</table>'
                ,'<ul>'
                ,'<li>中间若有间隔，则连续天数重新计算</li>'
                ,'<li style="color: #FF5722;">不可利用程序自动签到，否则飞吻清零</li>'
                ,'</ul>'
                ,'</div>'].join('')
        });
    });

    //签到活跃榜
    var tplSigninTop = ['{{# layui.each(d.data, function(index, item){ }}'
        ,'<li>'
        ,'<a href="/js/f/front/user/{{item.userCode}}" target="_blank">'
        ,'{{# var avatarUrl = item.avatarUrl.replace(\'ctxPath\',\'js\') }}'
        ,'<img src="{{avatarUrl}}">'
        ,'<cite class="fly-link">{{item.userName}}</cite>'
        ,'</a>'
        ,'{{# var date = new Date(item.front.upSignDate); if(d.index < 2){ }}'
        ,'<span class="fly-grey">签到于 {{ layui.laytpl.digit(date.getHours()) + ":" + layui.laytpl.digit(date.getMinutes()) + ":" + layui.laytpl.digit(date.getSeconds()) }}</span>'
        ,'{{# } else { }}'
        ,'<span class="fly-grey">已连续签到 <i>{{ item.front.upSignCount }}</i> 天</span>'
        ,'{{# } }}'
        ,'</li>'
        ,'{{# }); }}'
        ,'{{# if(d.data.length === 0) { }}'
        ,'{{# if(d.index < 2) { }}'
        ,'<li class="fly-none fly-grey">今天还没有人签到</li>'
        ,'{{# } else { }}'
        ,'<li class="fly-none fly-grey">还没有签到记录</li>'
        ,'{{# } }}'
        ,'{{# } }}'].join('');

    elemSigninTop.on('click', function(){
        var loadIndex = layer.load(1, {shade: 0.8});
        fly.json('/js/f/front/signin', function(res){ //实际使用，请将 url 改为真实接口
            var tpl = $(['<div class="layui-tab layui-tab-brief" style="margin: 5px 0 0;">'
                ,'<ul class="layui-tab-title">'
                ,'<li class="layui-this">最新签到</li>'
                ,'<li>今日最快</li>'
                ,'<li>总签到榜</li>'
                ,'</ul>'
                ,'<div class="layui-tab-content fly-signin-list" id="LAY_signin_list">'
                ,'<ul class="layui-tab-item layui-show"></ul>'
                ,'<ul class="layui-tab-item">2</ul>'
                ,'<ul class="layui-tab-item">3</ul>'
                ,'</div>'
                ,'</div>'].join(''))
                ,signinItems = tpl.find('.layui-tab-item');

            layer.close(loadIndex);

            layui.each(signinItems, function(index, item){
                var html = laytpl(tplSigninTop).render({
                    data: res.data[index]
                    ,index: index
                });
                $(item).html(html);
            });

            layer.open({
                type: 1
                ,title: '签到活跃榜 - TOP 20'
                ,area: '300px'
                ,shade: 0.8
                ,shadeClose: true
                ,id: 'layer-pop-signintop'
                ,content: tpl.prop('outerHTML')
            });

        }, {type: 'get'});
    });


    //回帖榜
    var tplReply = ['{{# layui.each(d.data, function(index, item){ }}'
        ,'<dd>'
        ,'<a href="/u/{{item.uid}}">'
        ,'<img src="{{item.user.avatar}}">'
        ,'<cite>{{item.user.username}}</cite>'
        ,'<i>{{item["count(*)"]}}次回答</i>'
        ,'</a>'
        ,'</dd>'
        ,'{{# }); }}'].join('')
        ,elemReply = $('#LAY_replyRank');

    if(elemReply[0]){
        /*
        fly.json('/top/reply/', {
          limit: 20
        }, function(res){
          var html = laytpl(tplReply).render(res);
          elemReply.find('dl').html(html);
        });
        */
    };

    //相册
    if($(window).width() > 750){
        layer.photos({
            photos: '.photos'
            ,zIndex: 9999999999
            ,anim: -1
        });
    } else {
        $('body').on('click', '.photos img', function(){
            window.open(this.src);
        });
    }


    //搜索
    $('.fly-search').on('click', function(){
        layer.open({
            type: 1
            ,title: false
            ,closeBtn: false
            //,shade: [0.1, '#fff']
            ,shadeClose: true
            ,maxWidth: 10000
            ,skin: 'fly-layer-search'
            ,content: ['<form action="http://cn.bing.com/search">'
                ,'<input autocomplete="off" placeholder="搜索内容，回车跳转" type="text" name="q">'
                ,'</form>'].join('')
            ,success: function(layero){
                var input = layero.find('input');
                input.focus();

                layero.find('form').submit(function(){
                    var val = input.val();
                    if(val.replace(/\s/g, '') === ''){
                        return false;
                    }
                    input.val('site:layui.com '+ input.val());
                });
            }
        })
    });

    //新消息通知
    fly.newmsg();

    //发送激活邮件
    fly.activate = function(email){
        fly.json('/api/activate/', {}, function(res){
            if(res.status === 0){
                layer.alert('已成功将激活链接发送到了您的邮箱，接受可能会稍有延迟，请注意查收。', {
                    icon: 1
                });
            };
        });
    };
    $('#LAY-activate').on('click', function(){
        fly.activate($(this).attr('email'));
    });

    //点击@
    $('body').on('click', '.fly-aite', function(){
        var othis = $(this), text = othis.text();
        if(othis.attr('href') !== 'javascript:;'){
            return;
        }
        text = text.replace(/^@|（[\s\S]+?）/g, '');
        othis.attr({
            href: '/js/f/front/user/jump?username='+ text
            ,target: '_blank'
        });
    });

    //表单提交
    form.on('submit(*)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        fly.json(action, data.field, function(res){
            var end = function(){
                if(res.action){
                    location.href = res.action;
                } else {
                    fly.form[action||button.attr('key')](data.field, data.form);
                }
            };
            if(res.status == 0){
                button.attr('alert') ? layer.alert(res.msg, {
                    icon: 1,
                    time: 10*1000,
                    end: end
                }) : end();
            };
        });
        return false;
    });

    //加载特定模块
    if(layui.cache.page && layui.cache.page !== 'index'){
        var extend = {};
        extend[layui.cache.page] = layui.cache.page;
        layui.extend(extend);
        layui.use(layui.cache.page);
    }

    //加载IM
    if(!device.android && !device.ios){
        //layui.use('im');
    }

    //加载编辑器
    fly.layEditor({
        elem: '.fly-editor'
    });

    //手机设备的简单适配
    var treeMobile = $('.site-tree-mobile')
        ,shadeMobile = $('.site-mobile-shade')

    treeMobile.on('click', function(){
        $('body').addClass('site-mobile');
    });

    shadeMobile.on('click', function(){
        $('body').removeClass('site-mobile');
    });

    //获取统计数据
    $('.fly-handles').each(function(){
        var othis = $(this);
        $.get('/api/handle?alias='+ othis.data('alias'), function(res){
            othis.html('（下载量：'+ res.number +'）');
        })
    });

    //固定Bar
    util.fixbar({
        bar1: '&#xe642;'
        ,bgcolor: '#009688'
        ,click: function(type){
            if(type === 'bar1'){
                addPostBtn.click();
            }
        }
    });

    /*======================自定义方法区======================*/
    //登录
    form.on('submit(loginForm)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        var secretKey = layui.cache.data.secretKey;
        data.field.username = DesUtils.encode(data.field.username,secretKey);
        data.field.password = DesUtils.encode(data.field.password, secretKey);
        data.field.validCode = DesUtils.encode(data.field.validCode, secretKey);
        fly.json(action, data.field, function(res){
            var end = function(res){
                location.href = "/js/f/front/index";
            };
            if(res.result == "true"){
                layer.msg('登录中', {
                    icon: 16,
                    shade: 0.01,
                    end:end(res),
                });
            };
        },{error:function () {
            revalidBtn.click();
            $("#L_vercode").val("");
        }});
        return false;
    });
    //刷新验证码
    var revalidBtn = $('.XYX_VALIDCODEBTN');
    revalidBtn.on('click', function(){
        $("#validCodeImg").attr("src","/js/validCode?"+new Date().getTime());
    });
    //注册
    form.on('submit(regForm)', function(data){
        layui.cache.regFormData = data;
        var action = $(data.form).attr('action'), button = $(data.elem);
        fly.json(action, data.field, function(res){
            layer.prompt({title: res.message, formType: 0}, function(regValidCode, index){
                var data = parent.layui.cache.regFormData;
                data.field.regValidCode = regValidCode;
                fly.json("/js/account/saveRegByValidCode", data.field, function(res){
                        layer.close(index);
                        layer.alert(res.message, {icon: 6},function () {
                            location.href = "/js/f/front/user/login";
                        });
                });

            });
        },{error:function () {
                revalidBtn.click();
                $("#L_vercode").val("");
            }});
        return false;
    });
    //退出
    var logoutBtn = $('#XYX_logoutBtn');
    logoutBtn.on('click', function(){
        var url = "/js/a/logout?__ajax=json";
        fly.json(url, null, function(res){
            location.href = "/js/f/front/user/login";
        });
        return false;
    });
    /*//发表新帖按钮点击事件
    var addPostBtn = $(".LAY_ADDPOST");
    addPostBtn.on('click', function(){
        var url = "/js/f/front/jie/ajaxTurn";
        fly.json(url, null, function(res){
            location.href = res.data;
        });
        return false;
    });*/
    exports('fly', fly);

});

