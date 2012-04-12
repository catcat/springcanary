<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <img src="/orcl/images/octocat.png" style="width:32px; height: 32px;"/>

    <h3>Products</h3>

    <table class="data">
    <c:forEach items="${rows}" var="row">
        <tr>
            <td>${row.id}</td>

            <td>
                <span>
                    <a href="/orcl/products/${row.id}.htm">
                        ${row.name}
                    </a>
                </span>
            </td>

        </tr>
    </c:forEach>
    </table>

<a href="/orcl/products/create.htm">
    Create
</a>

&nbsp;

<a href="/orcl/products/fill.htm?count=42">
    Fill
</a>
