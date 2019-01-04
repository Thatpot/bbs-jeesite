/**

 @Name: 用户模块

 */
 
layui.define(['laypage', 'fly', 'element', 'flow', 'table'], function(exports){

  var $ = layui.jquery;
  var layer = layui.layer;
  var util = layui.util;
  var laytpl = layui.laytpl;
  var form = layui.form;
  var laypage = layui.laypage;
  var fly = layui.fly;
  var flow = layui.flow;
  var element = layui.element;
  var upload = layui.upload;
  var table = layui.table;

    //我发的贴
  table.render({
      elem: '#LAY_mySendCard'
      ,url: '/js/f/front/user/postapi'
      ,method: 'post'
      ,request:{
        pageName:'pageNo',
        limitName:'pageSize'
      },
      parseData:function(res){
        return {
            "code":"0",
            "msg":res.data.message,
            "count":res.data.count,
            "data":res.data.list
        }
      }
      ,cols: [[
          {field: 'title', title: '帖子标题', minWidth: 300, templet: '<div><a href="/js/f/front/jie/{{ d.id }}" target="_blank" class="layui-table-link">{{ d.postTitle }}</a></div>'}
          ,{field: 'postStatus', title: '状态', width: 100, align: 'center', templet: function(d){
                  if(d.postIstop == '1'){
                      return '<span style="color: #FF5722;">加精</span>';
                  } else if(d.status == "-1"){
                      return '<span style="color: #ccc;">审核中</span>';
                  } else if(d.status == "1"){
                      return '<span style="color: #ccc;">已删除</span>';
                  }else {
                      return '<span style="color: #999;">正常</span>'
                  }
           }}
          ,{field: 'postStatus', title: '结贴', width: 100, align: 'center', templet: function(d){
                  return d.postStatus >= 1? '<span style="color: #5FB878;">已结</span>' : '<span style="color: #ccc;">未结</span>'
              }}
          ,{field: 'createDate', title: '发表时间', width: 120, align: 'center', templet: '<div>{{ layui.util.timeAgo(d.createDate, 1) }}</div>'}
          ,{title: '数据', width: 150, templet: '<div><span style="font-size: 12px;">{{d.postViewCount}}阅/{{d.postCommentCount}}答</span></div>'}
          ,{title: '操作', width: 100, templet: function(d){
                  return d.postStatus == 0 && d.status == 0  ? '<a class="layui-btn layui-btn-xs" href="/js/f/front/jie/add?id='+ d.id +'" target="_blank">编辑</a>' : ''
              }}
      ]]
      ,page: true
      ,skin: 'line'
  });

  //帖子管理
  table.render({
        elem: '#LAY_postManager'
        ,url: '/js/f/front/user/postmanagerapi'
        ,method: 'post'
        ,request:{
            pageName:'pageNo',
            limitName:'pageSize'
        },
        parseData:function(res){
            return {
                "code":"0",
                "msg":res.data.message,
                "count":res.data.count,
                "data":res.data.list
            }
        }
        ,cols: [[
            {field: 'title', title: '帖子标题', minWidth: 300, templet: '<div><a href="/js/f/front/jie/{{ d.id }}" target="_blank" class="layui-table-link">{{ d.postTitle }}</a></div>'}
            ,{field: 'postUp', title: '作者', minWidth: 100, templet: '<div><a href="/js/f/front/user/{{ d.postUp.userCode }}" target="_blank" class="layui-table-link">{{ d.postUp.userName }}</a></div>'}
            ,{field: 'postStatus', title: '状态', width: 100, align: 'center', templet: function(d){
                    if(d.postIstop == '1'){
                        return '<span style="color: #FF5722;">加精</span>';
                    } else if(d.status == "-1"){
                        return '<span style="color: #ccc;">审核中</span>';
                    } else if(d.status == "1"){
                        return '<span style="color: #ccc;">已删除</span>';
                    }else {
                        return '<span style="color: #999;">正常</span>'
                    }
                }}
            ,{field: 'postStatus', title: '结贴', width: 100, align: 'center', templet: function(d){
                    return d.postStatus >= 1? '<span style="color: #5FB878;">已结</span>' : '<span style="color: #ccc;">未结</span>'
                }}
            ,{field: 'createDate', title: '发表时间', width: 120, align: 'center', templet: '<div>{{ layui.util.timeAgo(d.createDate, 1) }}</div>'}
            ,{title: '数据', width: 100, templet: '<div><span style="font-size: 12px;">{{d.postViewCount}}阅/{{d.postCommentCount}}答</span></div>'}
            ,{title: '操作', width: 100, toolbar:'<div><a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a></div>'}
        ]]
        ,page: true
        ,skin: 'line'
    });

  //我收藏的帖
  if($('#LAY_myCollectioncard')[0]){
      table.render({
          elem: '#LAY_myCollectioncard'
          ,url: '/js/f/front/user/collectapi'
          ,method: 'post'
          ,request:{
              pageName:'pageNo',
              limitName:'pageSize'
          }
          ,parseData:function(res){
              return {
                  "code":"0",
                  "msg":res.data.message,
                  "count":res.data.count,
                  "data":res.data.list
              }
          }
          ,cols: [[
              {field: 'collectPost.postTitle', title: '帖子标题', minWidth: 300, templet: '<div><a href="/js/f/front/jie/{{ d.collectPost.id }}" target="_blank" class="layui-table-link">{{ d.collectPost.postTitle }}</a></div>'}
              ,{field: 'createDate', title: '收藏时间', width: 120, align: 'center', templet: '<div>{{ layui.util.timeAgo(d.createDate, 1) }}</div>'}
          ]]
          ,page: true
          ,skin: 'line'
      });

  }

  //显示当前tab
  if(location.hash){
      element.tabChange('user', location.hash.replace(/^#/, ''));
  }

  element.on('tab(user)', function(){
      var othis = $(this), layid = othis.attr('lay-id');
      if(layid){
          location.hash = layid;
      }
  });

//监听工具条
table.on("tool(LAY_postManager)", function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
    var data = obj.data; //获得当前行数据
    var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
    var tr = obj.tr; //获得当前行 tr 的DOM对象

    if(layEvent === 'del'){ //删除
        layer.confirm('真的删除行么', function(index){
            obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
            layer.close(index);
            //向服务端发送删除指令
        });
    } else if(layEvent === 'edit'){ //编辑
        //do something

        //同步更新缓存对应的值
        obj.update({
            username: '123'
            ,title: 'xxx'
        });
    }
});

  var gather = {}, dom = {
    mine: $('#LAY_mine')
    ,mineview: $('.mine-view')
    ,minemsg: $('#LAY_minemsg')
    ,infobtn: $('#LAY_btninfo')
  };

  //根据ip获取城市
  if($('#L_city').val() === ''){
    $.getScript('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js', function(){
      $('#L_city').val(remote_ip_info.city||'');
    });
  }

  //上传图片
  if($('.upload-img')[0]){
    layui.use('upload', function(upload){
      var avatarAdd = $('.avatar-add');
      upload.render({
        elem: '.upload-img'
        ,url: '/js/f/front/user/upload'
        ,auto:true
        ,size: 1024
        ,before: function(){
          this.data = {
              userCode:$("#userCode").val()
          };
          avatarAdd.find('.loading').show();
        }
        ,done: function(res){
          if(res.result == "true"){
              location.reload();
          } else {
            layer.msg(res.message, {icon: 5});
          }
          avatarAdd.find('.loading').hide();
        }
        ,error: function(){
          avatarAdd.find('.loading').hide();
        }
      });
    });
  }

  //合作平台
  if($('#LAY_coop')[0]){

    //资源上传
    $('#LAY_coop .uploadRes').each(function(index, item){
      var othis = $(this);
      upload.render({
        elem: item
        ,url: '/api/upload/cooperation/?filename='+ othis.data('filename')
        ,accept: 'file'
        ,exts: 'zip'
        ,size: 30*1024
        ,before: function(){
          layer.msg('正在上传', {
            icon: 16
            ,time: -1
            ,shade: 0.7
          });
        }
        ,done: function(res){
          if(res.code == 0){
            layer.msg(res.msg, {icon: 6})
          } else {
            layer.msg(res.msg)
          }
        }
      });
    });

    //成效展示
    var effectTpl = ['{{# layui.each(d.data, function(index, item){ }}'
    ,'<tr>'
      ,'<td><a href="/u/{{ item.uid }}" target="_blank" style="color: #01AAED;">{{ item.uid }}</a></td>'
      ,'<td>{{ item.authProduct }}</td>'
      ,'<td>￥{{ item.rmb }}</td>'
      ,'<td>{{ item.create_time }}</td>'
      ,'</tr>'
    ,'{{# }); }}'].join('');

    var effectView = function(res){
      var html = laytpl(effectTpl).render(res);
      $('#LAY_coop_effect').html(html);
      $('#LAY_effect_count').html('你共有 <strong style="color: #FF5722;">'+ (res.count||0) +'</strong> 笔合作授权订单');
    };

    var effectShow = function(page){
      fly.json('/cooperation/effect', {
        page: page||1
      }, function(res){
        effectView(res);
        laypage.render({
          elem: 'LAY_effect_page'
          ,count: res.count
          ,curr: page
          ,jump: function(e, first){
            if(!first){
              effectShow(e.curr);
            }
          }
        });
      });
    };

    effectShow();

  }

  //提交成功后刷新
  fly.form['set-mine'] = function(data, required){
    layer.msg('修改成功', {
      icon: 1
      ,time: 1000
      ,shade: 0.1
    }, function(){
      location.reload();
    });
  }

  //帐号绑定
  $('.acc-unbind').on('click', function(){
    var othis = $(this), type = othis.attr('type');
    layer.confirm('整的要解绑'+ ({
      qq_id: 'QQ'
      ,weibo_id: '微博'
    })[type] + '吗？', {icon: 5}, function(){
      fly.json('/api/unbind', {
        type: type
      }, function(res){
        if(res.status === 0){
          layer.alert('已成功解绑。', {
            icon: 1
            ,end: function(){
              location.reload();
            }
          });
        } else {
          layer.msg(res.msg);
        }
      });
    });
  });


  //我的消息
  gather.minemsg = function(){
    var delAll = $('#LAY_delallmsg')
    ,tpl = '{{# var len = d.rows.length;\
    if(len === 0){ }}\
      <div class="fly-none">您暂时没有最新消息</div>\
    {{# } else { }}\
      <ul class="mine-msg">\
      {{# for(var i = 0; i < len; i++){ }}\
        <li data-id="{{d.rows[i].id}}">\
          <blockquote class="layui-elem-quote">{{ d.rows[i].content}}</blockquote>\
          <p><span>{{d.rows[i].time}}</span><a href="javascript:;" class="layui-btn layui-btn-sm layui-btn-danger fly-delete">删除</a></p>\
        </li>\
      {{# } }}\
      </ul>\
    {{# } }}'
    ,delEnd = function(clear){
      if(clear || dom.minemsg.find('.mine-msg li').length === 0){
        dom.minemsg.html('<div class="fly-none">您暂时没有最新消息</div>');
      }
    }
    
    
    /*
    fly.json('/message/find/', {}, function(res){
      var html = laytpl(tpl).render(res);
      dom.minemsg.html(html);
      if(res.rows.length > 0){
        delAll.removeClass('layui-hide');
      }
    });
    */
    
    //阅读后删除
    dom.minemsg.on('click', '.mine-msg li .fly-delete', function(){
      var othis = $(this).parents('li'), id = othis.data('id');
      fly.json('/message/remove/', {
        id: id
      }, function(res){
        if(res.status === 0){
          othis.remove();
          delEnd();
        }
      });
    });

    //删除全部
    $('#LAY_delallmsg').on('click', function(){
      var othis = $(this);
      layer.confirm('确定清空吗？', function(index){
        fly.json('/message/remove/', {
          all: true
        }, function(res){
          if(res.status === 0){
            layer.close(index);
            othis.addClass('layui-hide');
            delEnd(true);
          }
        });
      });
    });

  };

/*  gather.mine(1,1,1);*/

  dom.minemsg[0] && gather.minemsg();
  /*======================自定义方法区======================*/
    //修改基本信息
    form.on('submit(basicInfoForm)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        fly.json(action, data.field, function(res){
                layer.msg(res.message, {icon: 6,shade: 0.01,time: 500},function () {
                    location.reload();
                });
        });
        return false;
    });
    //修改密码
    form.on('submit(passwordForm)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        var secretKey = layui.cache.data.secretKey;
        data.field.oldPassword = DesUtils.encode(data.field.oldPassword,secretKey);
        data.field.newPassword = DesUtils.encode(data.field.newPassword, secretKey);
        data.field.confirmNewPassword = DesUtils.encode(data.field.confirmNewPassword, secretKey);
        fly.json(action, data.field, function(res){
              layer.msg(res.message, {icon: 6,shade: 0.01,time: 500},function () {
                  location.reload();
              });
        });
        return false;
    });
  exports('user', null);
  
});