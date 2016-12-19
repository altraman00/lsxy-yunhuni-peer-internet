<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@include file="/inc/import.jsp" %>
<!DOCTYPE html>
<html>

<!-- header -->
<head>
    <%@include file="/inc/meta.jsp" %>

</head>
<body>
<section class="vbox">
    <%@include file="/inc/headerNav.jsp"%>
    <section class='aside-section'>
        <section class="hbox stretch">
            <!-- .aside -->
            <%@include file="/inc/leftMenu.jsp"%>
            <!-- /.aside -->

            <section id="content">
            <section class="hbox stretch">
                <!-- 如果没有三级导航 这段代码注释-->
                <aside class="bg-green lter aside-sm hidden-print ybox" id="subNav">
                    <section class="vbox">
                        <div class="wrapper header"><span class="margin_lr"></span><span class="margin_lr border-left">&nbsp;号码管理</span>
                        </div>
                        <section class="scrollable">
                            <div class="slim-scroll">
                                <!-- nav -->
                                <nav class="hidden-xs">
                                    <ul class="nav">
                                        <li>
                                            <div class="aside-li-a active">
                                                <a href="${ctx}/console/telenum/callnum/index?pageNo=1&pageSize=20">我的号码</a>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="aside-li-a">
                                                <a href="${ctx}/console/telenum/bind/index">测试号码绑定</a>
                                            </div>
                                        </li>
                                    </ul>
                                </nav>
                            </div>


                        </section>

                    </section>
                </aside>
                <aside>
                    <section class="vbox xbox">
                        <!-- 如果没有三级导航 这段代码注释-->
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide">
                            <i class=" iconfont icon-menu-left text"></i>
                            <i class=" iconfont icon-menu-right text-active"></i>
                        </a></div>
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;我的号码</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section>
                                <div class="col-md-12 ">
                                    <p class="number_info">
                                        1.应用未上线前，会员可以使用平台的测试号码进行测试，应用上线后，会员需要租用固定的号码作为某些特定功能的呼入或呼出号码使用。<br/>
                                        2.租用号码成功后，每个号码统一收取100元/月的号码月租费，余额不足以支付每月扣除号码月租费时，号码列表的号码将会过期，过期时间超过7天，号码则自动移除。<br/>
                                        3.应用下线后，号码自动与应用解除关联，应用下线不影响号码月租费的收取。<br/>
                                        4.会员根据自身业务需要，购买不同功能的号码。<br/>
                                    </p>
                                </div>
                            </section>

                            <section class="panel panel-default pos-rlt clearfix ">
                                <div class="row">
                                    <div class="col-md-12 text-right remove-padding">
                                        <a id="call-number" class="btn btn-primary">号码租用</a>
                                    </div>
                                </div>
                                <table class="table table-striped cost-table-history">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>号码</th>
                                        <th>状态</th>
                                        <th class="text-center">可呼入</th>
                                        <th class="text-center">可呼出</th>
                                        <th class="text-left-fixed">关联应用</th>
                                        <th class="text-left-fixed"><span class="">归属地</span></th>
                                        <th>有效期</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${pageObj.result}" var="result" varStatus="s">
                                        <tr id="app-${result.id}">
                                            <td scope="row">${s.index+1}</td>
                                            <td>${result.resourceTelenum.telNumber}</td>
                                            <td>
                                                <c:if test="${result.rentExpire.time<time.time}">
                                                    <div style="color: red" >过期</div>
                                                </c:if>
                                                <c:if test="${result.rentExpire.time==null||result.rentExpire.time>time.time}">正常</c:if>
                                            </td>
                                            <td class="text-center">
                                                <c:if test="${result.resourceTelenum.isCalled==1}">
                                                    ✔
                                                </c:if>
                                                <c:if test="${result.resourceTelenum.isCalled==0}">
                                                    ✘
                                                </c:if>
                                            </td>
                                            <td class="text-center">
                                                <c:if test="${result.resourceTelenum.isDialing==1||result.resourceTelenum.isThrough==1}">
                                                    ✔
                                                </c:if>
                                                <c:if test="${result.resourceTelenum.isDialing==0&&result.resourceTelenum.isThrough==0}">
                                                    ✘
                                                </c:if>
                                            </td>
                                            <td class="text-left-fixed"><c:if test="${result.app==null}">无</c:if>
                                                <c:if test="${result.app!=null}">
                                                    <a href="${ctx}/console/app/detail?id=${result.app.id}">${result.app.name}</a>
                                                </c:if>
                                            </td>
                                            <td class="text-left-fixed">${result.resourceTelenum.areaCode}</td>
                                            <td>
                                                <c:if test="${result.rentExpire.time<time.time}">
                                                    <div style="color: red" ><fmt:formatDate value="${result.rentExpire}" pattern="yyyy-MM-dd"/></div>
                                                </c:if>
                                                <c:if test="${result.rentExpire.time>time.time}">
                                                    <fmt:formatDate value="${result.rentExpire}" pattern="yyyy-MM-dd"/>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${result.app==null}"><a  onclick="release('${result.id}')">释放</a></c:if>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="pageUrl" value="${ctx}/console/telenum/callnum/index"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>
                            <!--待支付订单-->
                            <section class="panel panel-default pos-rlt clearfix" hidden id="nopaid" style="display:">
                                <div class="from-group hr">
                                    <h5 class="orange">待支付号码订单</h5>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 remove-padding">
                                        <p class="number_info">
                                            注意：订单的有效期为24小时，请在订单过期时间内完成支付,否则订单将会被系统自动取消。
                                        </p>
                                        <p>
                                            <span hidden id="orderid"></span>
                                            订单创建时间：<span id="paycreatetime" class="m-r-20">2016-03-03 16:00 </span>
                                            订单过期时间：<span id="paylasttime" class="m-r-20">2016-03-03 16:00</span>
                                            本次租用需要支付：￥<span id="paymoney" class="m-r-20 orange">2000.000</span>
                                        </p>
                                        <table class="table">
                                            <thead>
                                            <tr>
                                                <th>号码</th>
                                                <th class="text-center">可呼入</th>
                                                <th class="text-center">可呼出</th>
                                                <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                                                <th class="text-right">资源占用费</th>
                                            </tr>
                                            </thead>
                                            <tbody id="nopaid-table">
                                            <tr>
                                                <td>13971068693</td>
                                                <td class="text-center">✔</td>
                                                <td class="text-center">✘</td>
                                                <td class="text-center"><span class="text-center-l-fixed">020</span></td>
                                                <td class="text-right">￥122.000</td>
                                            </tr>
                                            <tr>
                                                <td>13971068693</td>
                                                <td class="text-center">✔</td>
                                                <td class="text-center">✘</td>
                                                <td class="text-center"><span class="text-center-l-fixed">0757</span></td>
                                                <td class="text-right">￥122.000</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                        <div class="text-right">
                                            <a  class="btn btn-primary btnpay">立即支付</a>
                                            <a  class="btn btn-default" onclick="closepay()">取消订单</a>
                                        </div>
                                    </div>
                                </div>
                            </section>
                        </section>
                    </section>
                </aside>
            </section>
            <a href="#" class="hide nav-off-screen-block" data-toggle="class:nav-off-screen" data-target="#nav"></a>
        </section>
    </section>
</section>
        <!-- 租用号码（Modal） -->
        <div id="vue-modal">
            <div class="modal fade call-detail-modal" id="call-modal" tabindex="100" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true">
                                &times;
                            </button>
                            <h4 class="modal-title" id="myModalLabel">
                                号码租用
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="number_info">
                                1.会员根据需要租用不同功能的号码，号码功能包括呼入和呼出功能。<br/>
                                2.租用号码时，每个号码需要一次性收取一定的费用作为资源占用费和100元的号码月租费。<br/>
                                3.租用后自动进入我的号码列表，应用上线时可选择购买的号码进行绑定。<br/>
                                4.每次最多只能租用5个号码。<br/>
                            </div>

                            <!--内容-->
                            <div class="row">
                                <div class="col-md-3 remove-padding">
                                    <input type="text" v-model="serach.name" @keyup.enter="find" placeholder="号码搜索" id="modal-name" class="form-control"/>
                                </div>
                                <div class="col-md-4">
                                    <span class="title">号码功能：</span>
                                    <select v-model="serach.phone" class="form-control select-box">
                                        <option value="">全部</option>
                                        <option value="callin">可呼入</option>
                                        <option value="callout">可呼出</option>
                                    </select>
                                </div>
                                <div class="col-md-4 remove-padding">
                                    <span class="title">归属地：</span>
                                    <select  v-model="serach.place"  class="form-control select-box" id="city_list">
                                        <option value="">全部</option>
                                    </select>
                                </div>
                                <div class="col-md-1 text-right remove-padding">
                                    <a  @click="find" class="btn btn-primary">查询</a>
                                </div>
                            </div>

                            <!--表格-->
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>号码</th>
                                    <th class="text-center">可呼入</th>
                                    <th class="text-center">可呼出</th>
                                    <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                                    <th class="text-right">资源占用费
              <span class="order-by-box">
                  <i class="fa fa-sort-asc up {{ orderby==3 ? 'active' : '' }}" @click="sort(3)"></i>
                  <i class="fa fa-sort-desc down {{ orderby==4 ? 'active' : '' }}" @click="sort(4)"></i>
              </span>
                                    </th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr v-for="item in phonelist">
                                    <td scope="row"><input type="checkbox" v-model="shop" value="{{ item.id }}"/></td>
                                    <td>{{ item.phone }}</td>
                                    <td class="text-center">{{ isCall[item.call] }}</td>
                                    <td class="text-center">{{ isCall[item.callout]}}</td>
                                    <td class="text-center"><span class="text-center-l-fixed">{{ item.place}}</span></td>
                                    <td class="text-right">￥{{ item.price}}</td>
                                </tr>
                                </tbody>
                            </table>

                            <!--分页-->
                            <div id="datatablepage"></div>

                            <div class="row" v-if="shoplist.length > 0">
                                <div class="col-md-12 remove-padding">
                                    <h5 class="modal-title orange">
                                        你选择的号码
                                    </h5>
                                    <!--表格-->
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>号码</th>
                                            <th class="text-center">可呼入</th>
                                            <th class="text-center">可呼出</th>
                                            <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                                            <th class="text-right">资源占用费</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-for="item in shoplist">
                                            <td><a @click="delshop($index)" class="del-close" title="删除此号码"><i class="iconfont icon-oc-close "></i></a></td>
                                            <td>{{ item.phone }}</td>
                                            <td class="text-center">{{ isCall[item.call] }}</td>
                                            <td class="text-center">{{ isCall[item.callout]}}</td>
                                            <td class="text-center"><span class="text-center-l-fixed">{{ item.place}}</span></td>
                                            <td class="text-right">￥{{ item.price}}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <a class="btn btn-primary" @click="payOrder" v-if="shoplist.length > 0&&shoplist.length <=5 ">立即下单</a>
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">关闭
                            </button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
            </div>

            <div class="modal fade call-detail-modal" id="pay-modal" tabindex="101" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true"  >
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close"
                                    data-dismiss="modal" aria-hidden="true" onclick="cancelpay()">
                                &times;
                            </button>
                            <h4 class="modal-title" id="">
                                号码租用
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-12 text-center"  v-if="paystatus==1">
                                    <img src="${resPrefixUrl }/images/register/icon_12.png" /><br/>
                                    <p>支付成功</p>
                                </div>

                                <div class="col-md-12 text-center"  v-if="paystatus==-1">
                                    <img src="${resPrefixUrl }/images/register/sign-error-icon.png" /><br/>
                                    <p>余额不足，支付失败</p>
                                </div>

                                <div class="col-md-12 remove-padding" v-if="paystatus==0">
                                    <div class="number_info">
                                        每个号码需要收取100元作为当前月份的月租费，以后月租费统一在月初收取。
                                    </div>
                                    <h5 class="modal-title orange">
                                        你选择的号码
                                    </h5>
                                    <!--表格-->
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>号码</th>
                                            <th class="text-center">可呼入</th>
                                            <th class="text-center">可呼出</th>
                                            <th class="text-center"><span class="text-center-l-fixed">归属地</span></th>
                                            <th class="text-right">资源占用费</th>
                                        </tr>
                                        </thead>
                                        <tbody id="pay-modal2">
                                        </tbody>
                                        <tbody>
                                        <tr v-for="item in shoplist" >
                                            <td>{{ item.phone }}</td>
                                            <td class="text-center">{{ isCall[item.call] }}</td>
                                            <td class="text-center">{{ isCall[item.callout]}}</td>
                                            <td class="text-center"><span class="text-center-l-fixed">{{ item.place}}</span></td>
                                            <td class="text-right">￥{{ item.price}}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            本次租用需要支付：￥<span class="orange" id="order_play_cost"></span>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <a class="btn btn-primary" @click="canne" v-if="paystatus==1">完成</a>
                            <a class="btn btn-primary" href="${ctx}/console/cost/recharge" v-if="paystatus==-1">前往充值</a>
                            <a class="btn btn-primary" @click="checkMoney" v-if="paystatus==0">立即支付</a>
                            <button type="button" class="btn btn-default" v-if="paystatus!=1"
                                    data-dismiss="modal" onclick="cancelpay()">取消
                            </button>
                        </div>
                    </div>
                    <!-- /.modal-content -->
                </div>
            </div>
            <!-- /.modal -->
        </div>
<%@include file="/inc/footer.jsp"%>
<script src="${resPrefixUrl }/js/include.js"></script>
<script src="${resPrefixUrl }/bower_components/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/js/bootstrap-datepicker.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js'> </script>
<script type="text/javascript" src='${resPrefixUrl }/js/cost/order.js'> </script>
<!---jsp请引入 vue.min.js 这个文件 -->
<script type="text/javascript" src='${resPrefixUrl }/js/vue/vue.js'></script>
<script type="text/javascript" src='${resPrefixUrl }/js/page.js'></script>
<script>
    function getCityList(){
        var html = '<option value="">全部</option>';
        var params = {'${_csrf.parameterName}':'${_csrf.token}'};
        ajaxsubmit("${ctx}/console/telenum/callnum/city/list",params,function(result) {
            var re = result.data;
            for(var i=0;i<re.length;i++){
                html += '<option value="'+re[i].areaCode+'">'+re[i].city+'</option>';
            }
            $('#city_list').html(html);
        });
    }
    getCityList();
    $('#modal-find').click(function () {

    });
    var vue = new Vue({
        el: '#vue-modal',
        data: {
            serach: {
                name: '',
                phone: '',
                place: ''
            },
            isCall: [ "✘","✔"],
            paystatus:0,
            orderby:0,
            shop: [],
            shoplist: [],
            phonelist: [],
            paylist:[]
        },
        watch: {
            'shop': function (v) {
                //判断是否有支付订单，如果有支付订单，则提示
                //showtoast('您有未支付的订单，请完成支付后，再进行号码租用') return
                var s = this.shoplist
                var p = this.phonelist
                //添加数据
                var number = [];
                p.forEach(function (ex) {
                    number.push(ex.id)
                })
                var snumber = [];
                s.forEach(function (ex) {
                    snumber.push(ex.id)
                })
                if (s.length > 0) {
                    var number2 = []
                    number.forEach(function (m, index) {
                        if (contains(v, m) && !contains(snumber, m))
                            s.push(p[index])
                    })
                } else {
                    p.forEach(function (ex) {
                        if (contains(v, ex.id))
                            s.push(ex);
                    })
                }
                //去除数据
                s.forEach(function (x, index) {
                    if (v.indexOf(x.id) == -1)
                        s.splice(index, 1)
                })
            }
        },
        methods: {
            delshop: function(index){
                var shop = this.shop
                shop.splice(index,1);
            },
            find:function(){
                modalPage()
            },
            clear: function () {
                this.shop = []
                this.shoplist = []
                this.serach = {name: '', phone: -1, place: -1}
            },
            clearpay: function(){
                this.shop = []
                this.paystatus = 0
                this.paylist = []
            },
            sort :function(v){
                this.orderby = v;
                modalPage()
            },
            canne:function(){
                window.location.href=ctx+"/console/telenum/callnum/index";
            },
            payOrder:function(){
                $('#call-modal').modal('hide');
                var ids = "";
                for(var i = 0;i<this.shop.length;i++){
                    ids+=this.shop[i];
                    if(i!=this.shop.length-1){
                        ids+=",";
                    }
                }
                var paylist = this.paylist
                //立即下单，下单成功 ，获取订单数据，
                var params = {'${_csrf.parameterName}':'${_csrf.token}',"ids":ids};
                ajaxsync("${ctx}/console/telenum/callnum/telnum/order/new",params,function(result) {
                    if(result.success){
                        showtoast("创建订单成功");
                        var re = result.data;
                        $('#orderid').html(re.id);
                        $('#order_play_cost').html(getAmont(re.amount));
                        $('#pay-modal').modal('show');
                        //回调订单数据
                        noPay();
                    }else{
                        showtoast(result.errorMsg)
                    }


                });
            },
            checkMoney:function(){
                var t =this;
                //判断是否需要充值，提交订单
                var id = $('#orderid').html();
                var params = {'${_csrf.parameterName}':'${_csrf.token}'};
                ajaxsync("${ctx}/console/telenum/callnum/telnum/order/play/"+id,params,function(result) {
                    if(result.success){
                        //支付成功状态，同时3秒后刷新页面
                        t.paystatus = 1;
                        $('#orderid').html('');
                    }else{
                        if(result.errorCode==-1){
                            //余额不足充值
                            t.paystatus = -1;
                        }else{
                        }
                    }
                });
            },
            setPhoneList: function (nowPage, listRows) {
                //请求数据
//               saram = { name:this.serach.name,phone:this.serach.phone,place:this.serach.place}
                if(this.serach.name==-1){
                    this.serach.name='';
                }
                if(this.serach.phone==-1){
                    this.serach.phone='';
                }
                if(this.serach.place==-1){
                    this.serach.place='';
                }
                var order = '';
                if(this.orderby==3){
                    order = "amount:1";
                }else if(this.orderby==4){
                    order = "amount:0";
                }
                var params = {'${_csrf.parameterName}':'${_csrf.token}','pageNo':nowPage,'pageSize':listRows,'telnum':this.serach.name,'type':this.serach.phone,'areaCode':this.serach.place,'order':order};
                var self =this;
                ajaxsync("${ctx}/console/telenum/callnum/telnum/plist",params,function(result) {
                    var re = result.data.result;
                    var data= [];
                    for(var i=0;i<re.length;i++){
                        var d = {id:re[i].id,
                            phone: re[i].telNumber,
                            call: re[i].isCalled,
                            callout: (re[i].isDialing+re[i].isThrough)>0?1:0,
                            place: re[i].areaCode,
                            price:  re[i].amount
                        };
                        data.push(d);
                    }
                    self.phonelist = data;
                });
            }
        }
    })

    //数组辅助
    function contains(a, obj) {
        for (var i = 0; i < a.length; i++) {
            if (a[i] === obj) {
                return true;
            }
        }
        return false;
    }

    var gnowPage = 1;
    var glistRows = 5;
    //分页
    function modalPage() {
        if(vue.serach.name==-1){
            vue.serach.name='';
        }
        if(vue.serach.phone==-1){
            vue.serach.phone='';
        }
        if(vue.serach.place==-1){
            vue.serach.place='';
        }
        var params = {'${_csrf.parameterName}':'${_csrf.token}','pageNo':gnowPage,'pageSize':glistRows,'telnum':vue.serach.name,'type':vue.serach.phone,'areaCode':vue.serach.place,'order':''};
        //获取数据总数
        var count = 0;
        ajaxsync("${ctx}/console/telenum/callnum/telnum/plist",params,function(result) {
            count = result.data.totalCount;
        });
        //每页显示数量
        var listRow = glistRows;
        //显示多少个分页按钮
        var showPageCount = 4;
        //指定id，创建分页标签
        var pageId = 'datatablepage';
        //searchTable 为方法名
        var page = new Page(count, listRow, showPageCount, pageId, searchTable);
        page.show();
    }

    $('#call-number').click(function () {
        //重置数据
        vue.clear();
        modalPage();
        $('#call-modal').modal('show');
    });
    function getlongDate(longtime){
        var ct =  new Date(longtime);
        var year = ct.getFullYear();
        var month = (ct.getMonth()+1)>=10?(ct.getMonth()+1):"0"+(ct.getMonth()+1);
        var day = ct.getDate()>=10?ct.getDate():"0"+ct.getDate();
        var hour = ct.getHours()>=10?ct.getHours():"0"+ct.getHours();
        var m = ct.getMinutes()>=10?ct.getMinutes():"0"+ct.getMinutes();
        return year+"-"+month+"-"+day+" "+hour+":"+m;
    }
    /**
     * 分页回调方法
     * @param nowPage 当前页数
     * @param listRows 每页显示多少条数据
     * */
    var searchTable = function (nowPage, listRows) {
        vue.setPhoneList(nowPage, listRows);
    }

    $('.btnpay').click(function(){
        vue.clearpay();
        var params = {'${_csrf.parameterName}':'${_csrf.token}'};
        ajaxsync("${ctx}/console/telenum/callnum/telnum/order",params,function(data){
            if(data.success){
                if(data.data.order!=null){
                    var order = data.data.order;
                    $('#orderid').html(order.id);
                    $('#order_play_cost').html(getAmont(order.amount));
                    var re = data.data.list;
                    var data= [];
                    for(var i=0;i<re.length;i++){
                        var d = {id:re[i].telnum.id,
                            phone: re[i].telnum.telNumber,
                            call: re[i].telnum.isCalled>0?"✔":"✘",
                            callout: (re[i].telnum.isDialing+re[i].telnum.isThrough)>0?"✔":"✘",
                            place: re[i].telnum.areaCode,
                            price:  re[i].amount
                        };
                        data.push(d);
                    }
                    var html = '';
                    for(var i =0 ; i<data.length; i++){
                        html +='<tr><td>'+data[i].phone+'</td><td class="text-center">'+data[i].call+'</td><td  class="text-center">'+data[i].callout+'</td><td class="text-center"><span class="text-center-l-fixed">'+data[i].place+'</span></td><td class="text-right">￥'+data[i].price+'</td></tr>'
                    }

                    $('#pay-modal2').html(html);
                    $('#pay-modal').modal('show');
                }
            }else{
                showtoast("获取订单信息错误"+data.errorMsg);
            }
        },"get");

    })
    //加载待支付数据
    function noPay(){
        var params = {'${_csrf.parameterName}':'${_csrf.token}'};
        ajaxsync("${ctx}/console/telenum/callnum/telnum/order",params,function(data){
            if(data.success){
                if(data.data.order!=null){
                    var order = data.data.order;
                    $('#nopaid').show();
                    $('#orderid').html(order.id);
                    $('#paycreatetime').html(getlongDate(order.createTime));
                    $('#paylasttime').html(getlongDate(order.deadline));
                    $('#paymoney').html(getAmont(order.amount));
                    var re = data.data.list;
                    var data= [];
                    for(var i=0;i<re.length;i++){
                        var d = {id:re[i].telnum.id,
                            phone: re[i].telnum.telNumber,
                            call: re[i].telnum.isCalled>0?"✔":"✘",
                            callout: (re[i].telnum.isDialing+re[i].telnum.isThrough)>0?"✔":"✘",
                            place: re[i].telnum.areaCode,
                            price:  re[i].amount
                        };
                        data.push(d);
                    }
                    var html = '';
                    for(var i =0 ; i<data.length; i++){
                        html +='<tr><td>'+data[i].phone+'</td><td class="text-center">'+data[i].call+'</td><td  class="text-center">'+data[i].callout+'</td><td class="text-center"><span class="text-center-l-fixed">'+data[i].place+'</span></td><td class="text-right">￥'+data[i].price+'</td></tr>'
                    }

                    $('#nopaid-table').html(html);
                }
            }else{
                showtoast("获取订单信息错误"+data.errorMsg);
            }
        },"get");
    }
    noPay()



    $('#common-close').click(function(){
        $('.common-info').fadeOut()
    })


    //取消
    function cancelpay() {
//        if($('#orderid').html()!=''){
            showtoast('您的订单尚未支付，请及时付款！');
//        }
//        noPay();
        $('#pay-modal2').html('');
    }

    /**
     * id 为号码标识
     * @param id
     */
//    function release(id){
//        $('#editmark-tips').html('');
//        bootbox.setLocale("zh_CN");
//        bootbox.dialog({
//                    title: "提示",
//                    message: '<div class="row">  ' +
//                    '<div class="col-md-12 text-center">您是否需要释放当前号码，如需再次使用需要重新购买新的号码 </div>  </div>',
//                    buttons: {
//                        success: {
//                            label: "确认",
//                            className: "btn-primary",
//                            callback: function () {
//                                showtoast("释放成功")
//
//
//                                //异步加载数据，释放成功
//                            }
//                        },
//                        cancel:{
//                            label: "关闭",
//                            className: "btn-default",
//                        }
//                    }
//                }
//        );
//    }

    /**
     * id 为订单标识
     * @param id
     */
    function closepay(){
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                    title: "提示",
                    message: '<div class="row">  ' +
                    '<div class="col-md-12 text-center">取消订单后，选择的号码将会被系统收回，请确认您此次的操作？</div>  </div>',
                    buttons: {
                        success: {
                            label: "确认",
                            className: "btn-primary",
                            callback: function () {
                                var id = $('#orderid').html();
                                //取消成功 刷新页面
                                var params = {'${_csrf.parameterName}':'${_csrf.token}'};
                                ajaxsync("${ctx}/console/telenum/callnum/telnum/order/delete/"+id,params,function(data){
                                    if(data.success){
                                        showtoast('取消订单成功')
                                        $('#orderid').html('');
                                        $('#nopaid').hide();
                                    }else{
                                        showtoast(data.errorMsg);
                                    }
                                },"post");
                                //异步加载数据，释放成功
                            }
                        },
                        cancel:{
                            label: "关闭",
                            className: "btn-default",
                        }
                    }
                }
        );
    }


</script>

<script type="text/javascript">
function release(id){
    bootbox.setLocale("zh_CN");
    bootbox.confirm("您是否需要释放当前号码，如需再次使用需要重新购买新的号码", function(result) {
        if(result){
            var params = {'id':id,'${_csrf.parameterName}':'${_csrf.token}'};
            ajaxsync("${ctx}/console/telenum/callnum/release",params,function(data){
                if(data.success){
                    showtoast("释放成功");
                    $('#app-'+id).remove();
                }else{
                    showtoast(data.errorMsg);
                }
            },"post");
        }else{
            showtoast("取消");
        }
    });
}
</script>
</body>
</html>