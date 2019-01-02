/**

 @Name: 求解板块

 */
 
layui.define('fly', function(exports){

  var $ = layui.jquery;
  var layer = layui.layer;
  var util = layui.util;
  var laytpl = layui.laytpl;
  var form = layui.form;
  var fly = layui.fly;
  
  var gather = {}, dom = {
    jieda: $('#jieda')
    ,content: $('#L_content')
    ,jiedaCount: $('#jiedaCount')
  };

  //监听专栏选择
  form.on('select(column)', function(obj){
    var value = obj.value
    ,tips = {
      tips: 1
      ,maxWidth: 250
      ,time: 3000
    };
    if(value === '1'){
      layer.tips('系统会对【分享】类型的帖子予以飞吻奖励，但我们需要审核，通过后方可展示', obj.othis, tips);
    }
  });


  //求解管理
  gather.jieAdmin = {
    //删求解
    del: function(div){
      layer.confirm('确认删除该求解么？', function(index){
        layer.close(index);
        fly.json('/js/f/front/jie/delete', {
          id: div.data('id')
        }, function(res){
          if(res.result === "true"){
            location.href = '/js/f/front/index';
          } else {
            layer.msg(res.msg);
          }
        });
      });
    }
    
    //设置置顶、状态
    ,set: function(div){
      var othis = $(this);
      fly.json('/js/f/front/jie/topOrGoodPost', {
        id: div.data('id')
        ,postIsgood: othis.attr('postIsgood')
        ,postIstop: othis.attr('postIstop')
      }, function(res){
        if(res.result === "true"){
          location.reload();
        }
      });
    }

    //收藏
    ,collect: function(div){
      var othis = $(this), type = othis.data('type');
      fly.json('/js/f/front/jie/collect?type='+ type, {
          'collectPost': div.data('id')
      }, function(res){
        if(type === 'add'){
          othis.data('type', 'remove').html('取消收藏').addClass('layui-btn-danger');
        } else if(type === 'remove'){
          othis.data('type', 'add').html('收藏').removeClass('layui-btn-danger');
        }
      });
    }
  };

  $('body').on('click', '.jie-admin', function(){
    var othis = $(this), type = othis.attr('type');
    gather.jieAdmin[type] && gather.jieAdmin[type].call(this, othis.parent());
  });

  //异步渲染
  var asyncRender = function(){
    var div = $('.fly-admin-box'), jieAdmin = $('#LAY_jieAdmin');
    var collectSpan = $(jieAdmin[0]).find('span')[1];
    //查询帖子是否收藏
    if(collectSpan){
      fly.json('/js/f/front/jie/isCollected', {
          'collectPost': div.data('id')
      }, function(res){
        jieAdmin.append('<span class="layui-btn layui-btn-xs jie-admin '+ (res.data ? 'layui-btn-danger' : '') +'" type="collect" data-type="'+ (res.data ? 'remove' : 'add') +'">'+ (res.data ? '取消收藏' : '收藏') +'</span>');
      });
    }
  }();

  //解答操作
  gather.jiedaActive = {
    zan: function(li){ //赞
      var othis = $(this), ok = othis.hasClass('zanok');
      fly.json('/api/jieda-zan/', {
        ok: ok
        ,id: li.data('id')
      }, function(res){
        if(res.status === 0){
          var zans = othis.find('em').html()|0;
          othis[ok ? 'removeClass' : 'addClass']('zanok');
          othis.find('em').html(ok ? (--zans) : (++zans));
        } else {
          layer.msg(res.msg);
        }
      });
    }
    ,reply: function(li){ //回复
      var val = dom.content.val();
      var aite = '@'+ li.find('.fly-detail-user cite').text().replace(/\s/g, '');
      dom.content.focus()
      if(val.indexOf(aite) !== -1) return;
      dom.content.val(aite +' ' + val);
    }
    ,accept: function(li){ //采纳
      var othis = $(this);
      layer.confirm('是否采纳该回答为最佳答案？', function(index){
        layer.close(index);
        fly.json('/js/f/front/jie/acceptReply', {
          id: li.data('id')
        }, function(res){
          if(res.result === "true"){
            $('.jieda-accept').remove();
            $('.status-span').text("已结");
            $('.status-span').css("background-color","#5FB878");
            li.addClass('jieda-daan');
            li.find('.detail-about').append('<i class="iconfont icon-caina" title="最佳答案"></i>');
          } else {
            layer.msg(res.msg);
          }
        });
      });
    }
    ,edit: function(li){ //编辑
      fly.json('/jie/getDa/', {
        id: li.data('id')
      }, function(res){
        var data = res.rows;
        layer.prompt({
          formType: 2
          ,value: data.content
          ,maxlength: 100000
          ,title: '编辑回帖'
          ,area: ['728px', '300px']
          ,success: function(layero){
            fly.layEditor({
              elem: layero.find('textarea')
            });
          }
        }, function(value, index){
          fly.json('/jie/updateDa/', {
            id: li.data('id')
            ,content: value
          }, function(res){
            layer.close(index);
            li.find('.detail-body').html(fly.content(value));
          });
        });
      });
    }
    ,del: function(li){ //删除
      layer.confirm('确认删除该回答么？', function(index){
        layer.close(index);
        fly.json('/js/f/front/jie/deleteReply', {
          id: li.data('id')
        }, function(res){
          if(res.result === "true"){
            var count = dom.jiedaCount.text()|0;
            dom.jiedaCount.html(--count);
            li.remove();
            //如果删除了最佳答案
            if(li.hasClass('jieda-daan')){
              $('.jie-status').removeClass('jie-status-ok').text('求解中');
            }
          } else {
            layer.msg(res.msg);
          }
        });
      });    
    }
  };

  $('.jieda-reply span').on('click', function(){
    var othis = $(this), type = othis.attr('type');
    gather.jiedaActive[type].call(this, othis.parents('li'));
  });


  //定位分页
  if(/\/page\//.test(location.href) && !location.hash){
    var replyTop = $('#flyReply').offset().top - 80;
    $('html,body').scrollTop(replyTop);
  }

  //*======================自定义方法区======================*/
    //刷新验证码
    var revalidBtn = $('.XYX_VALIDCODEBTN');
    revalidBtn.on('click', function(){
        $("#validCodeImg").attr("src","/js/validCode?"+new Date().getTime());
    });
    form.on('submit(JieForm)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        fly.json(action, data.field, function(res){
            location.href = "/js/f/front/index";
        },{error:function () {
            revalidBtn.click();
            $("#L_vercode").val("");
        }});
        return false;
    })

    form.on('submit(replyForm)', function(data){
        var action = $(data.form).attr('action'), button = $(data.elem);
        fly.json(action, data.field, function(res){
            location.reload();
        });
        return false;
    })
  exports('jie', null);
});