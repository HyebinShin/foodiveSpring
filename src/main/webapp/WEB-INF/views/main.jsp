<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="includes/header.jsp"%>
<h1>main Page</h1>

<%@include file="includes/footer.jsp"%>

<!-- modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 classs="modal-title" id="myModalLabel">FOODIVE</h4>
            </div>
            <div class="modal-body">모달 바디</div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        let state = '<c:out value="${loginInfo}"/>';
        console.log("main state: "+state);

        let result = '<c:out value="${result}"/>';

        checkModal(result);

        history.replaceState({}, null, null);

        function checkModal(result) {
            if (result === '' || history.state) {
                return;
            }

            $(".modal-body").html(result);
            $("#myModal").modal("show");
        }
    })
</script>