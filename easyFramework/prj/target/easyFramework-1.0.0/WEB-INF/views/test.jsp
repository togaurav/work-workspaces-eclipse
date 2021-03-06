<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>test</title>
    <META HTTP-EQUIV="CONTENT-TYPE" CONTENT="TEXT/HTML; CHARSET=UTF-8">
</head>
<body>

    <c:forEach items="${dataList}" var="data" varStatus="s">
        <c:out value="${data}" default="guest"/>
        <c:out value="aaa"></c:out>
    </c:forEach>
    <#list dataList as data>
        ${data} <#if data_index!=3>|</#if>
    </#list>
    | <@spring.message code="currentlocale" text="default message"/>
    | <@spring.message code="hello" text="default message" arguments="Luis"/>
    | <@fmt.message key="currentlocale"/>
    <hr/>

    ${Father} | id:${Father.id} | name:${Father.name} | age:${Father.age} <br/>
    <#list Father.children as child>
    ${child} | id:${child.id} | name:${child.name} | father:${child.father}
    </#list>
    <hr/>

    ${dt?datetime} | ${num}
    <hr/>

    <#assign keys = map?keys>
    <#list keys as key>${key} = ${map[key]}; </#list>
    | ${map["key2"]}
    <hr/>

    <#if !aaa?exists>property aaa not exist!</#if>
    <hr/>

    <#--Freemarker显示(不能直接输出list，需要循环输出单条)-->
    <#list Teachers as teacher>
    ${teacher} | id:${teacher.id} | name:${teacher.name} | Students: <#list teacher.students as s>${s} </#list><br>
    </#list>

</body>
</html>