<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>Feed ${feed.name}</h2>
<div>
    <style>
        label {
            display: inline-block;
            width:230px;
            color: #005;
        }
    </style>

    <a href="/orcl/products.htm">
        List
    </a>

    <form:form modelAttribute="row" enctype="multipart/form-data">
        <form:hidden path="id" />

        <fieldset>
            <div>
                <label>Name (d=${determinant})</label>

                <form:input path="name" /><form:errors path="name" cssClass="error" />
            </div>

            <!--
            <div>
                <label>Version</label>
                <form:input path="version" /><form:errors path="version" cssClass="error" />
            </div>
            -->
            <div>
                <label>Quality</label>
                <form:input path="quality" /><form:errors path="quality" cssClass="error" />
            </div>

            <div>
                <label>Price</label>
                <form:input path="price" /><form:errors path="price" cssClass="error" />
            </div>

            <div>
                <label>Created</label>
                <form:input path="created" /><form:errors path="created" cssClass="error" />
            </div>
            <div>
                <label>Picture</label>
                <input type="file" name="picture"/>
            </div>

            <div>
                <label>User</label>
                <form:select path="user">
                    <form:options items="${users}" itemValue="id" itemLabel="name"/>
                </form:select>
                <form:errors path="user" cssClass="error" />
            </div>
            <div>
                <input type="submit" id="save" name="action" value="Save" />
            </div>
        </fieldset>

        <table border="0">
            <c:forEach items="${props}" var="prop">
                <tr>
                    <td>
                        &nbsp;${prop.name}
                    </td>

                    <td>
                    <c:choose>
                        <c:when test="${prop.type==null }">
                            <input type="text" name="prop_value[${prop.id}]" value="${prop.singleText.value}"/>
                        </c:when>
                        <c:otherwise>
                            <select name="prop_variant[${prop.id}]">
                                <!--form:options items="${extendedVariants[prop.type.id]}" itemValue="id" itemLabel="value" /-->
                                <option></option>
                                <c:forEach items="${extendedVariants[prop.type.id]}" var="variant">
                                    <option value="${variant.id}" ${(variant.id==prop.singleText.variant.id)?"selected":""}> ${variant.value} </option>
                                </c:forEach>
                            </select>
                        </c:otherwise>
                    </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </table>



        <h2>Native SQL</h2>
        <table border="1">
            <c:forEach items="${propsNativeSql}" var="prop">
                <tr>
                    <td>&nbsp;${prop.name}</td>
                    <!--
                    <td><input type="text" name="prop[${prop.id}]" value="${prop.value}"/></td>
                    -->
                    <td>&nbsp;${prop.id}</td>
                    <td>&nbsp;${prop.value}</td>
                </tr>
            </c:forEach>
        </table>

    </form:form>


    <!--
    <table>

        <c:forEach items="${propsHibernate}" var="prop">
            <tr>
                <td>${prop[0].name}</td>
                <td><input type="text" name="prop[${prop[0].id}]" value="${prop[1].value}"/></td>
            </tr>
        </c:forEach>
    </table>
    <hr/>

    -->

    <img src="/orcl/products/pic/${row.id}"/>

</div>


