(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0b8e12"],{3120:function(e,t,r){"use strict";r.r(t);var o=function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("form",{staticClass:"col",on:{submit:function(t){return t.preventDefault(),e.onSubmit(t)}}},[r("b-form-group",[r("label",{attrs:{for:""}},[e._v("Group name:")]),r("b-input",{directives:[{name:"autofocus",rawName:"v-autofocus"}],attrs:{type:"text",name:"",required:""},model:{value:e.group.name,callback:function(t){e.$set(e.group,"name",t)},expression:"group.name"}})],1),r("b-form-group",[r("label",{attrs:{for:""}},[e._v("Nickname:")]),r("b-input",{attrs:{type:"text",required:""},model:{value:e.group.nickname,callback:function(t){e.$set(e.group,"nickname",t)},expression:"group.nickname"}})],1),r("b-form-group",[r("label",{attrs:{for:""}},[e._v("Category:")]),r("b-input",{attrs:{type:"text",name:"",required:""},model:{value:e.group.category,callback:function(t){e.$set(e.group,"category",t)},expression:"group.category"}})],1),r("b-form-group",[r("label",{attrs:{for:""}},[e._v("Description:")]),r("b-form-textarea",{attrs:{type:"textarea",name:"",required:""},model:{value:e.group.description,callback:function(t){e.$set(e.group,"description",t)},expression:"group.description"}})],1),r("b-form-group",[r("label",{attrs:{for:"color"}},[e._v("Color:")]),r("input",{directives:[{name:"model",rawName:"v-model",value:e.group.color,expression:"group.color"}],staticClass:"ml-2",attrs:{name:"color",type:"color"},domProps:{value:e.group.color},on:{input:function(t){t.target.composing||e.$set(e.group,"color",t.target.value)}}})]),r("b-form-checkbox",{staticClass:"form-check-input",attrs:{name:"private",type:"checkbox"},model:{value:e.group.private,callback:function(t){e.$set(e.group,"private",t)},expression:"group.private"}},[e._v("Private ")]),r("b-form-checkbox",{staticClass:"form-check-input",attrs:{name:"accept",type:"checkbox"},model:{value:e.group.accept,callback:function(t){e.$set(e.group,"accept",t)},expression:"group.accept"}},[e._v(" Require accept ")]),r("b-form-checkbox",{staticClass:"form-check-input",attrs:{name:"questionnaire",type:"checkbox"},nativeOn:{change:function(t){return e.toggleQuestionnaire(t)}},model:{value:e.form,callback:function(t){e.form=t},expression:"form"}},[e._v(" Form ")]),e.form?r("b-form-group",[r("label",[e._v("Questions:")]),r("b-list-group",{staticClass:"col"},e._l(e.groupForm.form,(function(t,o){return r("b-list-group-item",{key:o},[e._v(" "+e._s(o)+" ")])})),1),r("b-input-group",{ref:"questions",staticClass:"mt-3"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.question,expression:"question"}],staticClass:"form-control",attrs:{type:"text"},domProps:{value:e.question},on:{input:function(t){t.target.composing||(e.question=t.target.value)}}}),r("div",{staticClass:"input-group-append"},[r("button",{staticClass:"btn btn-primary",on:{click:function(t){return t.preventDefault(),e.addQuestion(t)}}},[e._v(" Add ")])])])],1):e._e(),r("b-button",{staticClass:"mt-4",attrs:{variant:"primary",type:"submit"}},[e._v("Create")])],1)},a=[],n=(r("96cf"),r("1da1")),u={name:"Create-Group",data:function(){return{group:this.createNewGroup(),form:!1,groupForm:{form:{}},question:""}},methods:{createNewGroup:function(){return{nickname:"",name:"",description:"",category:"",imagePath:"",accept:0,private:0,reactionId:1,color:"#ffccaa"}},onSubmit:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,e.$http.group.create(e.group);case 3:e.$eventBus.$emit("close-modal"),t.next=9;break;case 6:t.prev=6,t.t0=t["catch"](0),e.$alert.display(t.t0);case 9:case"end":return t.stop()}}),t,null,[[0,6]])})))()},addQuestion:function(){this.group.groupForm.form[this.question]="",this.question=""},toggleQuestionnaire:function(e){e.target.checked?this.group.groupForm=this.groupForm:delete this.group.groupForm}}},c=u,i=r("2877"),s=Object(i["a"])(c,o,a,!1,null,null,null);t["default"]=s.exports}}]);
//# sourceMappingURL=chunk-2d0b8e12.81b1d400.js.map