(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-5082a409"],{6290:function(e,t,n){},"6bb0":function(e,t,n){"use strict";var r=n("6290"),a=n.n(r);a.a},e05c:function(e,t,n){"use strict";n.r(t);var r=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("form",{staticClass:"col",on:{submit:function(t){return t.preventDefault(),e.createLayout(t)}}},[n("label",{attrs:{for:""}},[e._v("name")]),n("input",{directives:[{name:"model",rawName:"v-model",value:e.layout.name,expression:"layout.name"}],attrs:{type:"text",name:"",required:""},domProps:{value:e.layout.name},on:{input:function(t){t.target.composing||e.$set(e.layout,"name",t.target.value)}}}),n("ul",e._l(e.groups,(function(t,r){return n("li",{key:r,class:[{green:e.layout.groups.includes(t.group)}],on:{click:function(n){return e.toggleGroup(t.group)}}},[e._v(" "+e._s(t.group.name)+" ")])})),0),n("button",{attrs:{type:"submit"}},[e._v("create layout")])])},a=[],u=(n("c740"),n("a434"),n("b0c0"),n("96cf"),n("1da1")),o=n("5530"),s=n("2f62"),c=(n("d81d"),24),i=8,l=8,p=c/i,m={create:function(e){var t=e.name,n=e.groups,r=n.map((function(e,t){return{name:e.name,i:e.id,x:t%p*i,y:Math.floor(t/p)*l,w:i,h:l}}));return{name:t,groups:r}}},f={data:function(){return{layout:{name:"",groups:[]}}},computed:Object(o["a"])({},Object(s["b"])({groups:function(e){return e.user.groups}})),methods:{toggleGroup:function(e){var t=this.layout.groups.findIndex((function(t){return t.name===e.name}));t<0?this.layout.groups.push(e):this.layout.groups.splice(t,1)},createLayout:function(){var e=this;return Object(u["a"])(regeneratorRuntime.mark((function t(){var n;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,n=m.create(e.layout),t.next=4,e.$store.dispatch("settings/createLayout",n);case 4:e.$eventBus.$emit("close-modal"),t.next=10;break;case 7:t.prev=7,t.t0=t["catch"](0),e.$alert.display(t.t0);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))()}}},g=f,d=(n("6bb0"),n("2877")),v=Object(d["a"])(g,r,a,!1,null,"f3714c4e",null);t["default"]=v.exports}}]);
//# sourceMappingURL=chunk-5082a409.5853d3c8.js.map