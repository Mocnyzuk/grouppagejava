(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-e6ca4dde"],{"065f":function(e,t,s){"use strict";s.r(t);var n=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("form",{staticClass:"groupSettings col",on:{submit:function(t){return t.preventDefault(),e.save(t)}}},[s("h2",[e._v(e._s(e.data.name)+" settings")]),s("GroupJoinRequests",{attrs:{requests:e.requests},on:{"user-accept":e.onUserAccept}}),s("button",{staticClass:"save",attrs:{type:"submit"}},[e._v("save")])],1)},r=[],a=(s("96cf"),s("1da1")),c=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"col"},[s("div",{staticClass:"row"},[s("h4",[e._v("Join requests: "+e._s(e.requests.length))]),s("button",{on:{click:function(t){t.preventDefault(),e.open=!e.open}}},[e._v(" "+e._s(e.open?"hide":"open")+" ")])]),e.open&&!e.requests.length?s("div",[e._v(" No join requests ")]):e._e(),e.open&&e.requests.length?s("div",{staticClass:"col"},[s("ul",{staticClass:"col requestList"},e._l(e.requests,(function(t,n){return s("li",{key:n,staticClass:"col requestList__item"},[s("h4",[e._v("nickname: "+e._s(t.nickname))]),e._l(e.requestForm(t),(function(t,n){return s("ul",{key:n,staticClass:"col"},[s("li",{staticClass:"col"},[s("p",[s("strong",[e._v(" "+e._s(n)+" ")])]),s("p",[e._v(" "+e._s(t)+" ")])])])})),s("button",{attrs:{disabled:e.users.includes(t.nickname)},on:{click:function(s){return s.preventDefault(),e.acceptUser(t.nickname)}}},[e._v(" accept ")])],2)})),0)]):e._e()])},u=[],o={props:{requests:Array},data:function(){return{users:[],open:!1}},methods:{requestForm:function(e){return e.form.form},acceptUser:function(e){this.users.push(e),this.$emit("user-accept",this.users)}}},i=o,p=(s("5f4d"),s("2877")),l=Object(p["a"])(i,c,u,!1,null,"2ac1fd65",null),f=l.exports,d=function(e){var t={name:"",description:"",category:"",imagePath:"",accept:0,private:0,reactionId:1,color:""};return e?Object.assign(t,e):t},v={components:{GroupJoinRequests:f},props:{modalProps:{type:Object,required:!1}},data:function(){return{data:JSON.parse(JSON.stringify(this.modalProps)),group:this.createNewGroup(this.data),requests:[],users:[]}},created:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.next=2,e.fetchRequests();case 2:case"end":return t.stop()}}),t)})))()},methods:{createNewGroup:d,fetchRequests:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){var s,n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,e.$http.group.getJoinRequests(e.data.id);case 3:s=t.sent,n=s.data,e.requests=n,t.next=10;break;case 8:t.prev=8,t.t0=t["catch"](0);case 10:case"end":return t.stop()}}),t,null,[[0,8]])})))()},save:function(){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,e.$http.group.acceptRequests(e.data.id,e.users);case 3:e.$eventBus.$emit("close-modal"),t.next=9;break;case 6:t.prev=6,t.t0=t["catch"](0),console.log(t.t0);case 9:case"end":return t.stop()}}),t,null,[[0,6]])})))()},onUserAccept:function(e){this.users=e}}},h=v,m=(s("acaf"),Object(p["a"])(h,n,r,!1,null,"763cfd51",null));t["default"]=m.exports},"0e6d":function(e,t,s){},1497:function(e,t,s){},"5f4d":function(e,t,s){"use strict";var n=s("1497"),r=s.n(n);r.a},acaf:function(e,t,s){"use strict";var n=s("0e6d"),r=s.n(n);r.a}}]);
//# sourceMappingURL=chunk-e6ca4dde.d3591920.js.map