<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<select name="industry" id="" class="form-control">
    <option value="通讯" <c:if test="${industry == '通讯'}"> selected</c:if> >通讯</option>
    <option value="教育" <c:if test="${industry == '教育'}"> selected</c:if> >教育</option>
    <option value="IT" <c:if test="${industry == 'IT'}"> selected</c:if> >IT</option>
    <option value="互联网" <c:if test="${industry == '电力'}"> selected</c:if> >互联网</option>
    <option value="游戏" <c:if test="${industry == '游戏'}"> selected</c:if> >游戏</option>
    <option value="金融" <c:if test="${industry == '金融'}"> selected</c:if> >金融</option>
    <option value="其他" <c:if test="${industry == '其他'}"> selected</c:if> >其他</option>
</select>