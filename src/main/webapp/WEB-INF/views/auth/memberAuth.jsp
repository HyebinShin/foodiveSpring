<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script type="text/javascript">
    let state = `<c:out value="${loginInfo.state}"/>`;

    if (!(state==='1' || state==='2')) {
        self.location = "/user/login";
    }
</script>