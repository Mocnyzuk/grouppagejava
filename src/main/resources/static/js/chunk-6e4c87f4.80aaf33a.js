(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6e4c87f4"],{"81e9":function(t,e,r){"use strict";var a=r("e039"),n=r.n(a);n.a},e039:function(t,e,r){},e05c:function(t,e,r){"use strict";r.r(e);var a=function(){var t=this,e=t.$createElement,r=t._self._c||e;return r("form",{staticClass:"col",on:{submit:function(e){return e.preventDefault(),t.createLayout(e)}}},[r("b-form-group",[r("label",{attrs:{for:""}},[t._v("Name:")]),r("b-input",{attrs:{type:"text",name:"",required:""},model:{value:t.layout.name,callback:function(e){t.$set(t.layout,"name",e)},expression:"layout.name"}})],1),r("b-list-group",{attrs:{tag:"ul"}},t._l(t.groups,(function(e,a){return r("b-list-group-item",{key:a,class:[{"bg-success":t.layout.groups.includes(e.group)}],attrs:{tag:"li"},on:{click:function(r){return t.toggleGroup(e.group)}}},[t._v(" "+t._s(e.group.name)+" ")])})),1),r("b-button",{staticClass:"mt-3",attrs:{type:"submit",variant:"primary"}},[t._v("Create")])],1)},n=[],u=(r("c740"),r("a434"),r("b0c0"),r("96cf"),r("1da1")),o=r("5530"),s=r("2f62"),c=(r("d81d"),24),i=8,l=8,p=c/i,m={create:function(t){var e=t.name,r=t.groups,a=r.map((function(t,e){return{name:t.name,i:t.id,x:e%p*i,y:Math.floor(e/p)*l,w:i,h:l}}));return{name:e,groups:a}}},f={name:"Create-Layout",data:function(){return{layout:{name:"",groups:[]}}},computed:Object(o["a"])({},Object(s["b"])({groups:function(t){return t.user.groups}})),methods:{toggleGroup:function(t){var e=this.layout.groups.findIndex((function(e){return e.name===t.name}));e<0?this.layout.groups.push(t):this.layout.groups.splice(e,1)},createLayout:function(){var t=this;return Object(u["a"])(regeneratorRuntime.mark((function e(){var r;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.prev=0,r=m.create(t.layout),e.next=4,t.$store.dispatch("settings/createLayout",r);case 4:t.$eventBus.$emit("close-modal"),e.next=10;break;case 7:e.prev=7,e.t0=e["catch"](0),t.$alert.display(e.t0);case 10:case"end":return e.stop()}}),e,null,[[0,7]])})))()}}},g=f,d=(r("81e9"),r("2877")),b=Object(d["a"])(g,a,n,!1,null,"ccd167d4",null);e["default"]=b.exports}}]);
//# sourceMappingURL=chunk-6e4c87f4.80aaf33a.js.map