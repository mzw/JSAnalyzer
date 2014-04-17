
var selectValue = { selectObj: null, selectContent: null, keyword: null, searchtype: 0 };
$(function () {
    selectValue.selectObj = $("#head-searchbar-select");
    selectValue.selectContent = $("#head-searchbar-select-Content");
    selectValue.selectObj.text("Products");
    if (_syscurrentpage == "Search Company") {
        selectValue.selectObj.text("Suppliers");
        selectValue.selectContent.attr("data-value", "suppliers");
        selectValue.searchtype = 5;
    }
    if (_syscurrentpage == "Search Product") {
        selectValue.selectObj.text("Products");
        selectValue.selectContent.attr("data-value", "products");
        selectValue.searchtype = 0;
    }
    if (_syscurrentpage == "Search Buying Lead") {
        selectValue.selectObj.text("Buyers");
        selectValue.selectContent.attr("data-value", "buyers");
        selectValue.searchtype = 1;
    }
    if (_syscurrentpage == "Search Natural Stones") {
        selectValue.selectObj.text("Natural stones");
        selectValue.selectContent.attr("data-value", "natural stones");
        selectValue.searchtype = 2;
    }
    if (_syscurrentpage == "Search Quarry") {
        selectValue.selectObj.text("Quarries");
        selectValue.selectContent.attr("data-value", "quarries");
        selectValue.searchtype = 3;
    }
    if (_syscurrentpage == "Search Price List") {
        selectValue.selectObj.text("Prices");
        selectValue.selectContent.attr("data-value", "prices");
        selectValue.searchtype = 4;
    }
    $("#head-searchbar-select").bind("click", function () {
        selectValue.selectContent.toggle();
    });
    $(".select-type").bind("click", function () {
        var dataValue = $(this).attr("data-value");
        var dataText = $(this).text();
        var hidTopKeyword = $("#txttopkeyword").val();
        selectValue.selectObj.text(dataText);
        selectValue.selectContent.attr("data-value", dataValue);
        if (dataValue == "products") {
            dataText = hidTopKeyword == "" ? "Search Product" : hidTopKeyword;
            selectValue.searchtype = 0;
        }
        if (dataValue == "suppliers") {
            dataText = hidTopKeyword == "" ? "Search Company" : hidTopKeyword;
            selectValue.searchtype = 5;
        }
        if (dataValue == "buyers") {
            dataText = hidTopKeyword == "" ? "Search Buying Lead" : hidTopKeyword;
            selectValue.searchtype = 1;
        }
        if (dataValue == "natural stones") {
            dataText = hidTopKeyword == "" ? "Search Natural Stones" : hidTopKeyword;
            selectValue.searchtype = 2;
        }
        if (dataValue == "quarries") {
            dataText = hidTopKeyword == "" ? "Search Quarry" : hidTopKeyword;
            selectValue.searchtype = 3;
        }
        if (dataValue == "prices") {
            dataText = hidTopKeyword == "" ? "Search Price List" : hidTopKeyword;
            selectValue.searchtype = 4;
        }
        $("#txttopkeyword").val(dataText);
        selectValue.selectContent.hide();
    });
    //搜索
    $("#txttopkeyword").autocomplete("/DataManager/StoneContactDataHandler.ashx?action=GetSuggestKeywordAutoComplete&searchtype=" + selectValue.searchtype + "", {
        minchar: 1,
        width: 565,
        selectFirst: false,
        formatItem: function (row, i, max) {
            //  if (selectValue.selectContent.attr("data-value") == "suppliers") return;
            var item = "<table id='auto" + i + "' style='width:100%;'border='0' cellpadding='0' cellspacing='0'><tr><td align='left'>" + row + "</td></tr></table>";
            return item;
        },
        formatResult: function (row, i, max) {
            // if (selectValue.selectContent.attr("data-value")== "suppliers") return;
            return row;
        }
    });
    $("#txttopkeyword").blur();
    lastinput = $("#txttopkeyword").val();
    $("#txttopkeyword").focus(function () {
        var keyword = $("#txttopkeyword").val();
        if (keyword.indexOf('Search ') != -1) {
            $("#txttopkeyword").val('');
        }
    }).blur(function () {
        var keyword = $("#txttopkeyword").val();
        if (keyword == '') {
            $("#txttopkeyword").val(lastinput);
        }
    });
    $("#txttopkeyword").bind('keydown', 'return', function () {
        searchPage();
        return false;
    });
    $('#btntopSearch').click(function () {
        searchPage();
    });
    //头部显示客服livechat是否在线状态
    //var flickerAPI = "http://live.stonecontact.com/livechat/Home/GetUserOnline?jsoncallback=?";
    //$.getJSON(flickerAPI, { companyid: '4dacfa9a-5a78-43e9-8e3e-41ba2e6435e7', ran: Math.random(), format: "json" })
    //.done(function (data) {
    //    if (data == '') {
    //        return;
    //    }
    //    if (data.list.split('|')[1] == 0) {
    //        $('#imglivechat').attr("src", '/Images/Chat/Offline.png');
    //        $('#alivechat').css({ "color": "#919191" });
    //    }
    //    else {
    //        $('#imglivechat').attr("src", '/Images/Chat/Online.png');
    //        $('#alivechat').css({ "color": "#0027ce" });
    //    }
    //});
    //在线聊天
    //setInterval(chatjs, 5000);
    //setInterval(CustomerServiceHead, 10000);
    //    $("#show-close-chat").toggle(function () {
    //        $("#ul-livechat").hide();
    //    }, function () {
    //        $("#ul-livechat").show();
    //    });
});
function searchPage() {

    var reg = /(\s)/g;
    var keywordType = 0;
    var selectType = $("#head-searchbar-select-Content").attr("data-value");
    var keyword = $("#txttopkeyword").val();
    keyword = $.trim(keyword);
    var keyIndex = keyword.indexOf('Search ');
    if (keyword == '' || keyIndex != -1) {
        $("#txttopkeyword").focus();
    }
    if (keyIndex == 0) {
        keyword = "";
    }
    var url = '/';
    if (typeof keyword != "undefined" && keyword != "") {
        if (_syscurrentpage == "Search Product" || selectType == "products") {
            url = '/Main.aspx?pid=ProductSearch&seachKeyword=' + $("#txttopkeyword").val();
        }
        if (selectType == "quarries") {
            url = '/Main.aspx?pid=QuarrySearch&seachKeyword=' + keyword
            keywordType = 1;
        }
        if (selectType == "buyers") {
            url = '/Main.aspx?pid=OfferSearch&topKeyword=' + keyword
            keywordType = 4;
        }
        if (selectType == "suppliers") {
            keywordType = 5;
            url = '/Main.aspx?pid=CompanySearch&seachKeyword=' + keyword
        }
        if (selectType == "prices") {
            url = '/Main.aspx?pid=PriceListSearch2&topKeyword=' + keyword
            keywordType = 2;
        }
        if (selectType == "natural stones") {
            url = '/Main.aspx?pid=InfoCenterMaterial&topKeyword=' + keyword
            keywordType = 5;
        }
        location.href = url;
    }

}
/*
 * $extendPrototype 扩展prototype函数
 * @param {Function} constructor
 * @param {Object} prototype
 */
var $extendPrototype = function (constructor, prototype) {
    var c = constructor || function () { },
        p = prototype || {};
    for (var atr in p) {
        c.prototype[atr] = p[atr];
    }
    return c;
}

/* 
 * String扩展
 */
var strEx = {
    removeJS: function () {    /*删除Script字符串内容*/
        return this.replace(/<script[^>]*?>([\w\W]*?)<\/script>/ig, '');
    },
    getJS: function () {        /*将Script字符串转换为Script对象,返回Script or false*/
        var js = this.replace(/[\s\S]*?<script[^>]*?>([\w\W]*?)<\/script>[\s\S]*?/g, '$1\r');
        if (js == this) {
            return false;
        } else {
            var s = document.createElement('script');
            s.text = js;
            return s;
        }
    },
    trim: function (str) {    /*删除首尾相应内容,参数为空则删除空格*/
        var reg;
        if (str) {
            str = str.replace(/([\.\+\?\*\\\^\&\[\]\(\)\{\}\$\,])/g, '\\$1');
            reg = new RegExp("^(" + str + ")+|(" + str + ")+$", "g");/* 特殊字符 (. + ? * \ ^ & [ ] ( ) { } $ ,) */
        } else {
            reg = /^\s+|\s+$/g;
        }
        return this.replace(reg, "");
    }
}
String = $extendPrototype(String, strEx);
