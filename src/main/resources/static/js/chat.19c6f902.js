(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chat"],{"181a":function(t,e,n){"use strict";var r=n("42ba"),s=n.n(r);s.a},"2cc3":function(t,e,n){},3881:function(t,e,n){"use strict";var r=n("2cc3"),s=n.n(r);s.a},"42ba":function(t,e,n){},"4de4":function(t,e,n){"use strict";var r=n("23e7"),s=n("b727").filter,a=n("1dde"),o=n("ae40"),i=a("filter"),c=o("filter");r({target:"Array",proto:!0,forced:!i||!c},{filter:function(t){return s(this,t,arguments.length>1?arguments[1]:void 0)}})},5530:function(t,e,n){"use strict";n.d(e,"a",(function(){return a}));n("a4d3"),n("4de4"),n("4160"),n("e439"),n("dbb4"),n("b64b"),n("159b");function r(t,e,n){return e in t?Object.defineProperty(t,e,{value:n,enumerable:!0,configurable:!0,writable:!0}):t[e]=n,t}function s(t,e){var n=Object.keys(t);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(t);e&&(r=r.filter((function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable}))),n.push.apply(n,r)}return n}function a(t){for(var e=1;e<arguments.length;e++){var n=null!=arguments[e]?arguments[e]:{};e%2?s(Object(n),!0).forEach((function(e){r(t,e,n[e])})):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(n)):s(Object(n)).forEach((function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(n,e))}))}return t}},"9ab9":function(t,e,n){},"9dac":function(t,e,n){"use strict";var r=n("9ab9"),s=n.n(r);s.a},a15b:function(t,e,n){"use strict";var r=n("23e7"),s=n("44ad"),a=n("fc6a"),o=n("a640"),i=[].join,c=s!=Object,u=o("join",",");r({target:"Array",proto:!0,forced:c||!u},{join:function(t){return i.call(a(this),void 0===t?",":t)}})},bdcd:function(t,e,n){},c98b:function(t,e,n){"use strict";n.r(e);var r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("main",{staticClass:"row"},[n("div",{staticClass:"sidebar col"},[n("h3",{staticClass:"title"},[t._v(" new conversation ")]),t._l(t.groups,(function(t){return n("ChatUsers",{key:t.group.id,attrs:{chat:t.group,participantId:t.participantId}})}))],2),n("div",{staticClass:"chats"},t._l(t.conversations,(function(t,e){return n("app-conversation",{key:e,attrs:{conversation:t}})})),1),n("div",{staticClass:"sidebar col"},[n("h3",{staticClass:"title"},[t._v("existing conversations")]),t._l(t.existingConversations,(function(t){return n("ExistingConversation",{key:t.id,attrs:{data:t}})}))],2)])},s=[],a=(n("96cf"),n("1da1")),o=n("5530"),i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"group"},[n("h4",{staticClass:"group__name",on:{click:t.toggle}},[t._v(t._s(t.chat.name))]),t.open&&t.otherUsers.length?n("ul",{staticClass:"users col"},t._l(t.otherUsers,(function(e){return n("li",{key:e.id,on:{click:function(n){return t.openConversation(e.id)}}},[n("p",[t._v(t._s(e.nickname))])])})),0):t._e(),t.open&&!t.otherUsers.length?n("div",[n("p",[t._v("No users in group")])]):t._e()])},c=[],u=(n("4de4"),n("a9e3"),{props:{chat:{type:Object,required:!0},participantId:{type:[Number,String],required:!0}},data:function(){return{open:!1,users:[]}},computed:{otherUsers:function(){var t=this;return this.users.filter((function(e){return e.id!==t.participantId}))}},methods:{toggle:function(){var t=this;return Object(a["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(!t.open){e.next=4;break}t.open=!1,e.next=7;break;case 4:return e.next=6,t.fetchUsers();case 6:t.open=!0;case 7:case"end":return e.stop()}}),e)})))()},fetchUsers:function(){var t=this;return Object(a["a"])(regeneratorRuntime.mark((function e(){var n,r;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,t.$http.group.getParticipants(t.chat.id);case 3:n=e.sent,r=n.data,t.users=r,t.open=!0,e.next=12;break;case 9:e.prev=9,e.t0=e["catch"](0),console.log(e.t0);case 12:case"end":return e.stop()}}),e,null,[[0,9]])})))()},openConversation:function(t){var e="/app/conversation/new/".concat(t);this.$store.state.chats.stompClient.send(e,{},JSON.stringify({participantId:this.participantId,type:"NEW"}))}}}),p=u,l=(n("3881"),n("2877")),f=Object(l["a"])(p,i,c,!1,null,"64790848",null),v=f.exports,d=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"conversation col"},[n("div",{staticClass:"conversation__toolbar row"},[n("p",[t._v(" "+t._s(t.conversation.id)+" ")]),n("div",{staticClass:"row"},[n("button",{on:{click:t.openConversationSettings}},[t._v("+")]),n("button",{staticClass:"closeButton",on:{click:function(e){return t.closeConversation(t.conversation.id)}}},[t._v(" X ")])])]),n("div",{directives:[{name:"chat-scroll",rawName:"v-chat-scroll"}],staticClass:"conversation__content col"},t._l(t.conversation.content,(function(e,r){return n("div",{key:r,class:["message",{message__own:e.participantId===t.conversation.user}]},[n("p",[t._v(t._s(e.content))])])})),0),n("form",{staticClass:"row",on:{submit:function(e){return e.preventDefault(),t.sendMessage(e)}}},[n("input",{directives:[{name:"model",rawName:"v-model",value:t.message,expression:"message"}],attrs:{type:"text"},domProps:{value:t.message},on:{input:function(e){e.target.composing||(t.message=e.target.value)}}}),n("button",{attrs:{type:"submit"}},[t._v("SEND")])])])},h=[],g=n("2f62"),m={props:["conversation"],data:function(){return{message:""}},computed:Object(o["a"])({},Object(g["b"])({stompClient:function(t){return t.user.stompClient}})),methods:{closeConversation:function(t){this.$store.commit("chats/CLOSE_CONVERSATION",t)},sendMessage:function(){var t="/app/conversation/".concat(this.conversation.id,"/sendmessage");this.$store.state.chats.stompClient.send(t,{},JSON.stringify({participantId:this.conversation.user,content:this.message,type:"CHAT"})),this.message=""},openConversationSettings:function(){this.openModal("conversationSettings",this.conversation)}}},b=m,C=(n("181a"),Object(l["a"])(b,d,h,!1,null,"10e29b18",null)),O=C.exports,_=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"group",on:{click:function(e){return t.openConversation(t.data.id)}}},[t._v(" "+t._s(t.data.name||t.displayedNames)+" ")])},w=[],j=(n("a15b"),n("d81d"),n("fb6a"),{props:{data:Object},data:function(){return{open:!1,users:[]}},computed:{displayedNames:function(){var t=this.data.participants.map((function(t){return t.nickname})).join(", ");return t.length>20?t.slice(0,20)+"...":t}},methods:{openConversation:function(t){var e=this;return Object(a["a"])(regeneratorRuntime.mark((function n(){var r,s;return regeneratorRuntime.wrap((function(n){while(1)switch(n.prev=n.next){case 0:return n.next=2,e.$http.chat.getConversationMessages(t);case 2:r=n.sent,s=r.data,e.$store.commit("chats/CREATE_CONVERSATION",s);case 5:case"end":return n.stop()}}),n)})))()}}}),y=j,x=(n("f4fc"),Object(l["a"])(y,_,w,!1,null,"e71270ac",null)),k=x.exports,E={components:{appConversation:O,ChatUsers:v,ExistingConversation:k},data:function(){return{existingConversations:[]}},computed:Object(o["a"])({},Object(g["b"])({groups:function(t){return t.user.groups},conversations:function(t){return t.chats.conversations}})),created:function(){var t=this;return Object(a["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,t.getExistingConversations();case 2:case"end":return e.stop()}}),e)})))()},methods:{openChatSearch:function(){this.openModal("chatSelect")},getExistingConversations:function(){var t=this;return Object(a["a"])(regeneratorRuntime.mark((function e(){var n,r;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,e.next=3,t.$http.chat.getExistingConversations();case 3:n=e.sent,r=n.data,t.existingConversations=r,e.next=11;break;case 8:e.prev=8,e.t0=e["catch"](0),console.log(e.t0);case 11:case"end":return e.stop()}}),e,null,[[0,8]])})))()}}},N=E,S=(n("9dac"),Object(l["a"])(N,r,s,!1,null,"4014ec08",null));e["default"]=S.exports},d81d:function(t,e,n){"use strict";var r=n("23e7"),s=n("b727").map,a=n("1dde"),o=n("ae40"),i=a("map"),c=o("map");r({target:"Array",proto:!0,forced:!i||!c},{map:function(t){return s(this,t,arguments.length>1?arguments[1]:void 0)}})},dbb4:function(t,e,n){var r=n("23e7"),s=n("83ab"),a=n("56ef"),o=n("fc6a"),i=n("06cf"),c=n("8418");r({target:"Object",stat:!0,sham:!s},{getOwnPropertyDescriptors:function(t){var e,n,r=o(t),s=i.f,u=a(r),p={},l=0;while(u.length>l)n=s(r,e=u[l++]),void 0!==n&&c(p,e,n);return p}})},e439:function(t,e,n){var r=n("23e7"),s=n("d039"),a=n("fc6a"),o=n("06cf").f,i=n("83ab"),c=s((function(){o(1)})),u=!i||c;r({target:"Object",stat:!0,forced:u,sham:!i},{getOwnPropertyDescriptor:function(t,e){return o(a(t),e)}})},f4fc:function(t,e,n){"use strict";var r=n("bdcd"),s=n.n(r);s.a}}]);
//# sourceMappingURL=chat.19c6f902.js.map