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
            <aside class="bg-Green lter aside hidden-print"  id="nav"><%@include file="/inc/leftMenu.jsp"%></aside>
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
                                                <a href="${ctx}/console/telenum/callnum/index?pageNo=1&pageSize=20">呼入号码管理</a>
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
                        <div class="head-box"><a href="#subNav" data-toggle="class:hide"> <i
                                class="fa fa-angle-left text"></i> <i class="fa fa-angle-right text-active"></i> </a>
                        </div>
                        <div class="wrapper header">
                            <span class="border-left">&nbsp;呼入号码</span>
                        </div>
                        <section class="scrollable wrapper w-f">
                            <section>
                                <div class="col-md-12 ">
                                    <div class="number_info">
                                        <p>1、呼入号码作为应用使用IVR功能的拨入号使用，测试阶段可使用统一的测试呼入号码测试IVR功能，应用上线后可租用固定独立的呼入号码实现交互功能。</p>
                                        <p>2、余额不足以支付每月扣除的100元月租费时，相关联的号码将过期。</p>
                                        <p>3、下线应用后呼入号码自动与应用解除关联。</p>
                                        <p>4、超过有效期7天的号码自动移除。</p>
                                    </div>
                                </div>
                            </section>

                            <section class="panel panel-default pos-rlt clearfix ">
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
                                            private String isDialing;//可主叫
                                            private String isCalled;//可被叫
                                            <td class="text-center">
                                                <c:if test="${result.isDialing==1||result.isThrough==1}">
                                                    ✔
                                                </c:if>
                                                <c:if test="${result.isDialing==0&&result.isThrough==0}">
                                                    ✘
                                                </c:if>
                                            </td>
                                            <td class="text-center">
                                                <c:if test="${result.isCalled==1}">
                                                    ✔
                                                </c:if>
                                                <c:if test="${result.isCalled==0}">
                                                    ✘
                                                </c:if>
                                            </td>
                                            <td class="text-left-fixed"><c:if test="${result.app==null}">无</c:if>
                                                <c:if test="${result.app!=null}">
                                                    <a href="${ctx}/console/app/detail?id=${result.app.id}">${result.app.name}</a>
                                                </c:if>
                                            </td>
                                            <td class="text-left-fixed">${result.areaCode}</td>
                                            <td>
                                                <c:if test="${result.rentExpire.time<time.time}">
                                                    <div style="color: red" >过期</div>
                                                </c:if>
                                                <c:if test="${result.rentExpire.time>time.time}">正常</c:if>
                                            </td>
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
                                        </>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </section>
                            <c:set var="pageUrl" value="${ctx}/console/telenum/callnum/index"></c:set>
                            <%@include file="/inc/pagefooter.jsp" %>

                            <!--待支付订单-->
                            <section class="panel panel-default pos-rlt clearfix" id="nopaid" style="display:">
                                <div class="from-group hr">
                                    <h5 class="orange">待支付号码订单</h5>
                                </div>
                                <div class="row">
                                    <div class="col-md-12 remove-padding">
                                        <p class="number_info">
                                            注意：订单的有效期为24小时，请在订单过期时间内完成支付,否则订单将会被系统自动取消。
                                        </p>
                                        <p>
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
                                                <th class="text-center">质量(1~5分)</th>
                                                <th class="text-right">资源占用费</th>
                                            </tr>
                                            </thead>
                                            <tbody id="nopaid-table">
                                            <tr>
                                                <td>13971068693</td>
                                                <td class="text-center">✔</td>
                                                <td class="text-center">✘</td>
                                                <td class="text-center"><span class="text-center-l-fixed">020</span></td>
                                                <td class="text-center">4</td>
                                                <td class="text-right">￥122.000</td>
                                            </tr>
                                            <tr>
                                                <td>13971068693</td>
                                                <td class="text-center">✔</td>
                                                <td class="text-center">✘</td>
                                                <td class="text-center"><span class="text-center-l-fixed">0757</span></td>
                                                <td class="text-center">4</td>
                                                <td class="text-right">￥122.000</td>
                                            </tr>
                                            </tbody>
                                        </table>
                                        <div class="text-right">
                                            <a  class="btn btn-primary btnpay">立即支付</a>
                                            <a  class="btn btn-default" onclick="closepay(1)">取消订单</a>
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
                                号码租户
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="number_info">
                                1.会员根据需要租用不同功能的号码，号码功能包括呼入和呼出功能。<br/>
                                2.租用号码时，每个号码需要一次性收取一定的费用作为号码的使用费。<br/>
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
                                        <option value="-1">全部</option>
                                        <option value="1">可呼入</option>
                                        <option value="2">可呼出</option>
                                    </select>
                                </div>
                                <div class="col-md-4 remove-padding">
                                    <span class="title">归属地：</span>
                                    <select v-model="serach.place" class="form-control select-box">
                                        <option value="-1">全部</option>
                                        <option value="1">广州</option>
                                        <option value="2">上海</option>
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
                                    <th class="text-center">质量(1~5分)
              <span class="order-by-box">
                  <i class="fa fa-sort-asc up {{ orderby==1 ? 'active' : '' }}" @click="sort(1)"></i>
                  <i class="fa fa-sort-desc down {{ orderby==2 ? 'active' : '' }}" @click="sort(2)"></i>
                </span>
                                    </th>
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
                                    <td class="text-center">{{ item.quality}}</td>
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
                                            <th class="text-center">质量(1~5分)</th>
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
                                            <td class="text-center">{{ item.quality}}</td>
                                            <td class="text-right">￥{{ item.price}}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <a class="btn btn-primary" @click="payOrder" v-if="shoplist.length > 0">立即下单</a>
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
                                号码租户
                            </h4>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-12 text-center"  v-if="paystatus==1">
                                    <img src="./images/register/icon_12.png" /><br/>
                                    <p>支付成功</p>
                                </div>

                                <div class="col-md-12 text-center"  v-if="paystatus==-1">
                                    <img src="./images/register/sign-error-icon.png" /><br/>
                                    <p>余额不足，支付失败</p>
                                </div>

                                <div class="col-md-12 remove-padding" v-if="paystatus==0">
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
                                            <th class="text-center">质量(1~5分)</th>
                                            <th class="text-right">资源占用费</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-for="item in shoplist">
                                            <td>{{ item.phone }}</td>
                                            <td class="text-center">{{ isCall[item.call] }}</td>
                                            <td class="text-center">{{ isCall[item.callout]}}</td>
                                            <td class="text-center"><span class="text-center-l-fixed">{{ item.place}}</span></td>
                                            <td class="text-center">{{ item.quality}}</td>
                                            <td class="text-right">￥{{ item.price}}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <div class="row">
                                        <div class="col-md-12 text-center">
                                            本次租用需要支付：￥<span class="orange">2000.000</span>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <a class="btn btn-primary" @click="checkMoney" v-if="paystatus==1">完成</a>
                            <a class="btn btn-primary" href="cost_recharge.html" v-if="paystatus==-1">前往充值</a>
                            <a class="btn btn-primary" @click="checkMoney" v-if="paystatus==0">立即支付</a>
                            <button type="button" class="btn btn-default"
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
    $('#modal-find').click(function () {

    });
    var vue = new Vue({
        el: '#vue-modal',
        data: {
            serach: {
                name: '',
                phone: -1,
                place: -1
            },
            isCall: ["✔", "✘"],
            paystatus:0,
            orderby:0,
            shop: [],
            shoplist: [],
            phonelist: [],
            paylist:[],
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
                this.paystatus = 0
                this.paylist = []
            },
            sort :function(v){
                this.orderby = v
                modalPage()
            },
            payOrder:function(){
                $('#call-modal').modal('hide');
                //立即下单，下单成功 ，获取订单数据，
                //回调订单数据
                var paylist = this.paylist

                noPay()

                $('#pay-modal').modal('show');
            },
            checkMoney:function(){
                //判断是否需要充值，提交订单


                //支付成功状态，同时3秒后刷新页面
                this.paystatus = 1
                //余额不足充值
                this.paystatus = -1


            },
            setPhoneList: function (nowPage, listRows) {

                //请求数据
                var param = { name:this.serach.name,phone:this.serach.phone,place:this.serach.place}





                var url = ''
                //ajaxsync(url,param,function(result){

                //});
                //假数据
                var data = [
                    {
                        id: '1' + nowPage,
                        phone: '13611460986',
                        call: 0,
                        callout: 1,
                        place: '广州',
                        quality: nowPage,
                        price: '111.000'
                    },
                    {
                        id: '2' + nowPage,
                        phone: '13611460983',
                        call: 0,
                        callout: 1,
                        place: '广州',
                        quality: nowPage,
                        price: '111.000'
                    },
                    {
                        id: '3' + nowPage,
                        phone: '13611460984',
                        call: 0,
                        callout: 1,
                        place: '广州',
                        quality: nowPage,
                        price: '111.000'
                    },
                    {
                        id: '4' + nowPage,
                        phone: '13611460983',
                        call: 0,
                        callout: 1,
                        place: '广州',
                        quality: nowPage,
                        price: '111.000'
                    },
                    {
                        id: '5' + nowPage,
                        phone: '13611460984',
                        call: 0,
                        callout: 1,
                        place: '广州',
                        quality: nowPage,
                        price: '111.000'
                    },
                ]
                //赋值
                this.phonelist = data;
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


    //分页
    function modalPage() {
        //获取数据总数
        var count = 11;
        //每页显示数量
        var listRow = 3;
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
        $('#pay-modal').modal('show');
    })

    //加载待支付数据
    function noPay(){
        $('#nopaid').show();
        $('paycreatetime').html('2016-10-28');
        $('paycreatetime').html('2016-10-29');

        var html = '';
        var data = [
            {
                id: '1',
                phone: '13611460986',
                call: 0,
                callout: 1,
                place: '广州',
                quality: 3,
                price: '111.000'
            },
            {
                id: '2',
                phone: '13611460983',
                call: 0,
                callout: 1,
                place: '广州',
                quality: 3,
                price: '111.000'
            },
            {
                id: '3',
                phone: '13611460984',
                call: 0,
                callout: 1,
                place: '广州',
                quality: 2,
                price: '111.000'
            },
        ]

        /*   <td>{{ item.phone }}</td>
         <td class="text-center">{{ isCall[item.call] }}</td>
         <td class="text-center">{{ isCall[item.callout]}}</td>
         <td>{{ item.place}}</td>
         <td class="text-center">{{ item.quality}}</td>
         <td>{{ item.price}}</td>*/


        for(var i =0 ; i<data.length; i++){
            html +='<tr><td>'+data[i].phone+'</td><td class="text-center">'+data[i].call+'</td><td  class="text-center">'+data[i].callout+'</td><td class="text-center"><span class="text-center-l-fixed">'+data[i].place+'</span></td><td  class="text-center">'+data[i].quality+'</td><td class="text-right">￥'+data[i].price+'</td></tr>'
        }

        $('#nopaid-table').html(html);

    }




    $('#common-close').click(function(){
        $('.common-info').fadeOut()
    })


    //取消
    function cancelpay() {
        showtoast('您的订单尚未支付，请及时付款！')
    }

    /**
     * id 为号码标识
     * @param id
     */
    function release(id){
        $('#editmark-tips').html('');
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                    title: "提示",
                    message: '<div class="row">  ' +
                    '<div class="col-md-12 text-center">您是否需要释放当前号码，如需再次使用需要重新购买新的号码 </div>  </div>',
                    buttons: {
                        success: {
                            label: "确认",
                            className: "btn-primary",
                            callback: function () {
                                showtoast("释放成功")


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

    /**
     * id 为订单标识
     * @param id
     */
    function closepay(id){
        bootbox.setLocale("zh_CN");
        bootbox.dialog({
                    title: "提示",
                    message: '<div class="row">  ' +
                    '<div class="col-md-12 text-center">您确认取消此订单吗？</div>  </div>',
                    buttons: {
                        success: {
                            label: "确认",
                            className: "btn-primary",
                            callback: function () {
                                //取消成功 刷新页面
                                showtoast('取消订单成功')
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
    bootbox.confirm("您是否需要释放当前号码，如需再次使用需要重新缴纳资源租用费", function(result) {
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