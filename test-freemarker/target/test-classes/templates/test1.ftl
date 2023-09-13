<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
<#--注释　-->
Hello ${name}!
<br>
遍历list中的数据    数据模型中的名称为stus
<br>
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
        <td>出生日期</td>
    </tr>
    list遍历语法格式 ：  数据模型名 as 形参名
<#--    空值处理   判断某值为空用 ??  存在变量返回true        -->

    <#if stus ??>
    <#list stus as stu >
        <tr>
            <td>${stu_index + 1}</td>
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td <#if stu.money gt 300 >style="background: cornflowerblue" </#if>>${stu.money}</td>
<#--            <td>${stu.birthday?date}</td>-->
            <td>${stu.birthday?string("YYYY年MM月dd日") }</td>
        </tr>
    </#list>
     <br>
        学生的数量：${stus?size}

    </#if>

</table>
<br>
遍历数据模型中间的stuMap（map数据）
<br>
<#--姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>-->
<#--      (判断为空的值)!''     ''中的内容为（）中值为空时显示    -->
姓名：${(stuMap.stu1.name)!''}<br/>
年龄：${(stuMap.stu1.age)!''}<br/>
<br>
遍历map中的key stuMap?keys就是key列表（是一个list）<br>
<#list stuMap?keys as k>
    ${k}<br>
    姓名：${stuMap[k].name}<br/>
    年龄：${stuMap[k].age}<br/>
</#list>

</body>
</html>