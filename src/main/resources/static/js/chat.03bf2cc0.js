(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chat"],{"10d6":function(t,e,n){"use strict";var r=n("acc5"),s=n.n(r);s.a},1507:function(t,e,n){"use strict";var r=n("2ef5"),s=n.n(r);s.a},"28af":function(t,e,n){},"2ef5":function(t,e,n){},"444b":function(t,e,n){},"4de4":function(t,e,n){"use strict";var r=n("23e7"),s=n("b727").filter,a=n("1dde"),o=n("ae40"),c=a("filter"),i=o("filter");r({target:"Array",proto:!0,forced:!c||!i},{filter:function(t){return s(this,t,arguments.length>1?arguments[1]:void 0)}})},5530:function(t,e,n){"use strict";n.d(e,"a",(function(){return a}));n("a4d3"),n("4de4"),n("4160"),n("e439"),n("dbb4"),n("b64b"),n("159b");function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function s(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);e&&(r=r.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,r)}return n}function a(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?s(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):s(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}},"7a8a":function(t,e,n){"use strict";var r=n("444b"),s=n.n(r);s.a},acc5:function(t,e,n){},c98b:function(t,e,n){"use strict";n.r(e);var r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("main",{staticClass:"row"},[n("chat-select"),n("div",{staticClass:"chats"},t._l(t.conversations,(function(t,e){return n("app-conversation",{key:e,attrs:{conversation:t}})})),1)],1)},s=[],a=n("5530"),o=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"conversation col"},[n("div",{staticClass:"conversation__toolbar row"},[n("p",[t._v(" "+t._s(t.conversation.id)+" ")]),n("button",{staticClass:"closeButton",on:{click:function(e){return t.closeConversation(t.conversation.id)}}},[t._v(" X ")])]),n("div",{directives:[{name:"chat-scroll",rawName:"v-chat-scroll"}],staticClass:"conversation__content col"},t._l(t.conversation.content,(function(e,r){return n("div",{key:r,class:["message",{message__own:e.author===t.conversation.user}]},[n("p",[t._v(t._s(e.content))])])})),0),n("form",{staticClass:"row",on:{submit:function(e){return e.preventDefault(),t.sendMessage(e)}}},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.message,expression:"message"}],attrs:{type:"text"},domProps:{value:t.message},on:{input:function(e){e.target.composing||(t.message=e.target.value)}}}),n("button",{attrs:{type:"submit"}},[t._v("SEND")])])])},c=[],i=n("2f62"),u={props:["conversation"],data:function(){return{message:""}},computed:Object(a["a"])({},Object(i["b"])({stompClient:function(t){return t.user.stompClient}})),methods:{closeConversation:function(t){this.$store.commit("chats/CLOSE_CONVERSATION",t)},sendMessage:function(){var t="/app/conversation/".concat(this.conversation.id,"/sendmessage");this.$store.state.chats.stompClient.send(t,{},JSON.stringify({participantId:this.conversation.user,content:this.message,type:"CHAT"})),this.message=""}}},p=u,l=(n("10d6"),n("2877")),f=Object(l["a"])(p,o,c,!1,null,"e4ff148a",null),v=f.exports,d=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"chatSelect"},[n("h3",{staticClass:"title"},[t._v(" open conversation ")]),t._l(t.groups,(function(t){return n("chat-users",{key:t.group.id,attrs:{chat:t.group,participantId:t.participantId}})}))],2)},h=[],b=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"group"},[n("h4",{staticClass:"group__name",on:{click:t.toggle}},[t._v(t._s(t.chat.name))]),t.open&&t.otherUsers.length?n("ul",{staticClass:"users col"},t._l(t.otherUsers,(function(e){return n("li",{key:e.id,on:{click:function(n){return t.openConversation(e.id)}}},[n("p",[t._v(t._s(e.nickname))])])})),0):t._e(),t.open&&!t.otherUsers.length?n("div",[n("p",[t._v("No users in group")])]):t._e()])},m=[],g=(n("4de4"),n("a9e3"),n("96cf"),n("1da1")),O={props:{chat:{type:Object,required:!0},participantId:{type:[Number,String],required:!0}},data:function(){return{open:!1,users:[]}},computed:{otherUsers:function(){var t=this;return this.users.filter((function(e){return e.id!==t.participantId}))}},methods:{toggle:function(){var t=this;return Object(g["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(!t.open){e.next=4;break}t.open=!1,e.next=7;break;case 4:return e.next=6,t.fetchUsers();case 6:t.open=!0;case 7:case"end":return e.stop()}}),e)})))()},fetchUsers:function(){var t=this;return Object(g["a"])(regeneratorRuntime.mark((function e(){var n,r;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,t.$http.group.getParticipants(t.chat.id);case 3:n=e.sent,r=n.data,t.users=r,t.open=!0,e.next=12;break;case 9:e.prev=9,e.t0=e["catch"](0),console.log(e.t0);case 12:case"end":return e.stop()}}),e,null,[[0,9]])})))()},openConversation:function(t){var e="/app/conversation/new/".concat(t);this.$store.state.chats.stompClient.send(e,{},JSON.stringify({participantId:this.participantId,type:"NEW"}))}}},_=O,w=(n("ff11"),Object(l["a"])(_,b,m,!1,null,"368a6b2a",null)),j=w.exports,y={components:{chatUsers:j},computed:Object(a["a"])({},Object(i["b"])({groups:function(t){return t.user.groups}}))},C=y,k=(n("1507"),Object(l["a"])(C,d,h,!1,null,"73142832",null)),x=k.exports,P={components:{appConversation:v,chatSelect:x},computed:Object(a["a"])({},Object(i["b"])({conversations:function(t){return t.chats.conversations}})),methods:{openChatSearch:function(){this.openModal("chatSelect")}}},S=P,E=(n("7a8a"),Object(l["a"])(S,r,s,!1,null,"7a1c48f2",null));e["default"]=E.exports},dbb4:function(t,e,n){var r=n("23e7"),s=n("83ab"),a=n("56ef"),o=n("fc6a"),c=n("06cf"),i=n("8418");r({target:"Object",stat:!0,sham:!s},{getOwnPropertyDescriptors:function(t){var e,n,r=o(t),s=c.f,u=a(r),p={},l=0;while(u.length>l)n=s(r,e=u[l++]),void 0!==n&&i(p,e,n);return p}})},e439:function(t,e,n){var r=n("23e7"),s=n("d039"),a=n("fc6a"),o=n("06cf").f,c=n("83ab"),i=s((function(){o(1)})),u=!c||i;r({target:"Object",stat:!0,forced:u,sham:!c},{getOwnPropertyDescriptor:function(t,e){return o(a(t),e)}})},ff11:function(t,e,n){"use strict";var r=n("28af"),s=n.n(r);s.a}}]);
//# sourceMappingURL=chat.03bf2cc0.js.map