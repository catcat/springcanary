<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

            <h2>New product</h2>
            <form:form modelAttribute="row">
                <form:hidden path="id" />

                    <div>
                        <label>Name</label>
                        <form:input path="name" /><form:errors path="name" cssClass="error" />
                    </div>

                    <div>
                        <label>Quality</label>
                        <form:input path="quality" /><form:errors path="quality" cssClass="error" />
                    </div>

                    <div>
                        <label>User</label>
                        <form:select path="user">
                            <form:options items="${users}" itemValue="id" itemLabel="name"/>
                        </form:select>
                        <form:errors path="user" cssClass="error" />
                    </div>
                    <div>
                        <input type="submit" id="save" name="action" value="Create" />           
                    </div>


            </form:form>
